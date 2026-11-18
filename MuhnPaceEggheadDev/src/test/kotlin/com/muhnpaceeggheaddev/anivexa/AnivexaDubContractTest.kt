package com.muhnpaceeggheaddev.anivexa

import com.muhnpaceeggheaddev.anivexa.AnivexaDubContract.Candidate
import com.muhnpaceeggheaddev.anivexa.AnivexaDubContract.Format
import com.muhnpaceeggheaddev.anivexa.AnivexaDubContract.Inspection
import com.muhnpaceeggheaddev.anivexa.AnivexaDubContract.Reply
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class AnivexaDubContractTest {
    private val contract = AnivexaDubContract(21, 21, "One Piece", 1155)
    private val listingPath = "/episodes/anikoto/21"
    private val watchPath = "/watch/anikoto/21/dub/anikoto-1155"
    private val media = "https://media.example/video?signature=fixture"
    private val now = 1000L

    private fun row() = JSONObject().put("id", watchPath.removePrefix("/")).put("audio", "dub").put("number", 1155)

    private fun listing(rows: List<JSONObject> = listOf(row())) = JSONObject().put("type", "filtered").put("anikoto",
        JSONObject().put("meta", JSONObject().put("title", "ONE PIECE").put("source", "anikoto").put("malId", 21))
            .put("episodes", JSONObject().put("dub", JSONArray(rows))))

    private fun stream(type: String = "hls", url: String = media) =
        JSONObject().put("type", type).put("url", url).put("server", "fixture").put("referer", "https://player.example/")

    private fun watch(vararg streams: JSONObject) = JSONObject()
        .put("anilistId", 21).put("malId", 21).put("episode", 1155).put("audio", "dub")
        .put("headers", JSONObject().put("User-Agent", "Fixture player").put("referer", "https://first.example/"))
        .put("streams", JSONArray(streams.toList()))

    private suspend fun select(body: JSONObject, inspect: suspend (Candidate) -> Inspection?): Candidate? =
        contract.select(now, { path ->
            Reply(200, when (path) {
                listingPath -> listing().toString()
                watchPath -> body.toString()
                else -> error("Unexpected API route: $path")
            })
        }, inspect)

    @Test
    fun selectedStreamRetainsItsOwnHeadersAndCaptions() = runBlocking {
        val caption = JSONObject().put("url", "https://captions.example/en.vtt").put("label", "English CC")
            .put("srclang", "en").put("source", "fixture")
        val otherCaption = JSONObject(caption.toString()).put("source", "other").put("url", "https://other.example/es.vtt")
        for (format in Format.entries) {
            val source = stream(format.name.lowercase()).put("subtitles", JSONArray().put(caption))
            val result = select(watch(stream("embed", "https://player.example/watch"), source)
                .put("subtitles", JSONArray().put(caption).put(otherCaption))) {
                assertEquals(media, it.url)
                Inspection(206, format, setOf("eng", "en-US"))
            }
            assertNotNull(result)
            assertEquals(format, result!!.format)
            assertEquals(mapOf("User-Agent" to "Fixture player", "Referer" to "https://player.example/"), result.headers)
            assertEquals("English CC", result.captions.single().label)
            assertEquals("https://captions.example/en.vtt", result.captions.single().url)
        }
    }

    @Test
    fun episodeIndexMustIdentifyTheUniqueOriginalDubBeforeAWatchRequest() = runBlocking {
        val invalid = listOf(listing(emptyList()), listing(listOf(row(), row())),
            listing(listOf(row().put("number", 45))), listing(listOf(row().put("number", 1155.5))),
            listing(listOf(row().put("audio", "sub"))), listing(listOf(row().put("sourceNumber", 45))),
            listing(listOf(row().put("id", "watch/anikoto/21/dub/anikoto-45"))),
            listing().apply { getJSONObject("anikoto").getJSONObject("meta").put("title", "One Piece Film") },
            listing().apply { getJSONObject("anikoto").getJSONObject("meta").put("malId", 1) },
            listing().apply { getJSONObject("anikoto").put("error", "provider unavailable") })
        for (index in invalid) assertNull(contract.select(now, { path ->
            assertEquals(listingPath, path)
            Reply(200, index.toString())
        }, { error("No media inspection expected") }))
    }

    @Test
    fun wrongShowAudioEpisodeAndObsoleteSchemaYieldNoMedia() = runBlocking {
        for (body in listOf(watch(stream()).put("anilistId", 1), watch(stream()).put("malId", 1),
            watch(stream()).put("episode", 45), watch(stream()).put("audio", "sub"),
            JSONObject().put("sdub", watch(stream())), watch(stream("embed")), watch(stream("dash")),
            watch(stream(url = "http://media.example/video")), watch(stream()).put("error", "missing provider"))) {
            assertNull(select(body) { error("No native candidate expected") })
        }
    }

    @Test
    fun dubbedLabelAndEnglishCaptionsCannotEstablishAudioOrMediaType() = runBlocking {
        val body = watch(stream()).put("subtitles", JSONArray().put(JSONObject().put("label", "English")
            .put("url", "https://captions.example/en.vtt")))
        for (evidence in listOf(null, Inspection(200, null, setOf("en")), Inspection(200, Format.MP4, setOf("en")),
            Inspection(200, Format.HLS, emptySet()), Inspection(200, Format.HLS, setOf("ja")),
            Inspection(200, Format.HLS, setOf("en", "ja")), Inspection(403, Format.HLS, setOf("en")))) {
            assertNull(select(body) { evidence })
        }
    }

    @Test
    fun disabledDeploymentUnavailableProviderAndUnreadableResponsesFailClosed() = runBlocking {
        for (failure in listOf(Reply(402, """{"error":{"code":"402","message":"Payment required"}}"""),
            Reply(404, "{}"), Reply(429, ""), Reply(200, "<html>"), Reply(200, """{"error":"unavailable"}"""))) {
            assertNull(contract.select(now, { failure }, { error("No media expected") }))
            assertNull(contract.select(now, { if (it == listingPath) Reply(200, listing().toString()) else failure },
                { error("No media expected") }))
        }
        assertNull(contract.select(now, { throw IOException("offline") }, { error("No media expected") }))
        assertNull(select(watch(stream())) { throw IOException("media unavailable") })
    }

    @Test
    fun aLaterSelectionResolvesAgainAfterExpiry() = runBlocking {
        val requests = mutableListOf<String>()
        var currentUrl = media
        var expiresAt = now
        suspend fun onSelection() = contract.select(now, { path ->
            requests += path
            Reply(200, if (path == listingPath) listing().toString() else watch(stream(url = currentUrl)).toString())
        }, { Inspection(200, Format.HLS, setOf("en"), expiresAt) })
        assertNull(onSelection())
        currentUrl = "https://media.example/video?signature=fresh"
        expiresAt = now + 10
        assertEquals(currentUrl, onSelection()!!.url)
        assertEquals(listOf(listingPath, watchPath, listingPath, watchPath), requests)
    }
}
