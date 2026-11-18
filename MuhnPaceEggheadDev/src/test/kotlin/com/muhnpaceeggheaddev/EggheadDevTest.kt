package com.muhnpaceeggheaddev

import com.lagradost.cloudstream3.APIHolder
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.muhnpaceeggheaddev.baseline.MuhnPaceProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class EggheadDevTest {
    private val first = ExpansionMetadata.episodes.first()
    // Offline transport fixtures only. No fixture file is registered in the shipped provider.
    private val file = ApprovedFile(SourceType.ONE_PACE, "testFixture", "a".repeat(64), setOf("en"), first.originalEpisodes)

    private fun resource(name: String) = javaClass.getResourceAsStream("/$name")!!.bufferedReader().use { it.readText() }

    @Test
    fun bothPluginsRegisterWithSeparateNames() {
        val production = com.muhnpacedirect.MuhnPacePlugin().apply { filename = "test/MuhnPace.cs3" }
        val development = EggheadDevPlugin().apply { filename = "test/MuhnPaceEggheadDev.cs3" }
        try {
            production.load()
            development.load()
            val v7 = APIHolder.getApiFromNameNull("Muhn Pace Rework (by lincutto)")!!
            val dev = APIHolder.getApiFromNameNull("Muhn Pace Rework • Egghead Dev (by lincutto)")!!
            assertTrue(v7 is com.muhnpacedirect.MuhnPaceProvider)
            assertTrue(dev is EggheadDevProvider)
            assertEquals(production.filename, v7.sourcePlugin)
            assertEquals(development.filename, dev.sourcePlugin)
        } finally {
            APIHolder.allProviders.filter { it.sourcePlugin in setOf(production.filename, development.filename) }
                .forEach { APIHolder.removePluginMapping(it); APIHolder.allProviders.remove(it) }
        }
    }

    @Test
    fun reviewedStoryOrderAndUnreleasedSeason() {
        val rows = ExpansionMetadata.episodes
        assertEquals((1..45).toList(), rows.map { it.number })
        assertEquals(setOf(17), rows.map { it.season }.toSet())
        assertEquals((1086..1155).toSet(), rows.flatMap { it.originalEpisodes }.toSet())
        assertEquals((1086..1130).toSet(), rows.take(22).flatMap { it.originalEpisodes }.toSet())
        assertEquals(listOf(1108, 1110, 1111), rows[11].originalEpisodes)
        assertEquals(listOf(1129, 1130), rows[21].originalEpisodes)
        assertEquals(listOf(1131), rows[22].originalEpisodes)
        assertEquals(listOf(1137, 1138), rows[28].originalEpisodes)
        assertEquals(listOf(1139, 1140), rows[29].originalEpisodes)
        assertEquals((1141..1155).toList(), rows.filter { it.preferred == SourceType.ORIGINAL }.map { it.originalEpisodes.single() })
        assertTrue(ExpansionCatalogue().availableEpisodes.isEmpty())
    }

    @Test
    fun stableSlotsRequireEnglishAndCompleteCoverage() {
        val sources = SourceType.entries.reversed().map { file.copy(type = it) }
        val partial = file.copy(type = SourceType.ORIGINAL, originalEpisodes = listOf(1086))
        val japanese = file.copy(audioTracks = setOf("ja"))
        val dual = file.copy(audioTracks = setOf("ja", "en"))
        val third = ExpansionMetadata.episodes[2]
        val catalogue = ExpansionCatalogue(mapOf(first.key to (sources + listOf(partial, japanese, dual)),
            third.key to listOf(file.copy(originalEpisodes = third.originalEpisodes))))
        assertEquals(SourceType.entries, catalogue.choices(first).map { it.type })
        assertEquals(listOf(first), catalogue.availableEpisodes)
        assertTrue(ExpansionCatalogue(mapOf("17/2" to sources)).choices(first).isEmpty())
    }

    private fun info() = JSONObject().put("success", true).put("id", file.id).put("hash_sha256", file.sha256)
        .put("mime_type", "video/mp4").put("size", 1024L).put("availability", "").put("abuse_type", "")
        .put("can_download", true).put("allow_video_player", true)

    private fun response(url: String, code: Int, body: ByteArray, mime: String = "application/json"): Response =
        Response.Builder().request(Request.Builder().url(url).build()).protocol(Protocol.HTTP_1_1)
            .code(code).message("fixture").header("Content-Type", mime).header("Content-Range", "bytes 0-11/1024")
            .body(body.toResponseBody()).build()

    @Test
    fun playbackReturnsOneNativeEnglishLinkAfterAnEquivalentFallback() = runBlocking {
        val preferred = file.copy(type = SourceType.MUHN_PACE, id = "fixtureUnavailable")
        val catalogue = ExpansionCatalogue(mapOf(first.key to listOf(file, preferred)))
        val requested = mutableListOf<String>()
        val adapter = PixeldrainDubAdapter { url, headers ->
            requested += url
            when {
                url.endsWith("/${preferred.id}/info") -> response(url, 403, byteArrayOf())
                url.endsWith("/info") -> {
                    assertTrue(headers.isEmpty())
                    response(url, 200, info().toString().toByteArray())
                }
                else -> {
                    assertEquals(mapOf("Range" to "bytes=0-11"), headers)
                    response(url, 206, byteArrayOf(0, 0, 0, 24) + "ftypisom".toByteArray(), "video/mp4")
                }
            }
        }
        val provider = EggheadDevProvider(catalogue, adapter)
        val links = mutableListOf<ExtractorLink>()
        assertTrue(provider.loadLinks("${provider.seriesUrl}/17/1", false, {}, links::add))
        val url = "https://pixeldrain.dev/api/file/${file.id}"
        assertEquals(listOf("https://pixeldrain.dev/api/file/${preferred.id}/info", "$url/info", url), requested)
        val link = links.single()
        assertEquals(provider.name, link.source)
        assertEquals("One Pace • English dub", link.name)
        assertEquals(ExtractorLinkType.VIDEO, link.type)
        assertEquals(url, link.url)
        assertTrue(link.headers.isEmpty())
    }

    @Test
    fun restrictionsUnexpectedMediaAndNetworkFailuresEmitNothing() = runBlocking {
        val cases = listOf(info().put("embed_domains", JSONArray().put("onepace.net")),
            info().put("availability", "hotlink_detected"), info().put("can_download", false),
            info().put("allow_video_player", false), info().put("id", "wrongFixture"),
            info().put("hash_sha256", "b".repeat(64)), info().put("mime_type", "text/html"),
            info().put("abuse_type", "copyright"), JSONObject())
        for (row in cases) {
            val adapter = PixeldrainDubAdapter { url, _ ->
                assertTrue(url.endsWith("/info"))
                response(url, 200, row.toString().toByteArray())
            }
            assertNull(adapter.resolve(file, "test"))
        }
        for (code in listOf(200, 206, 302, 403, 429)) {
            val adapter = PixeldrainDubAdapter { url, _ ->
                if (url.endsWith("/info")) response(url, 200, info().toString().toByteArray())
                else response(url, code, "not an MP4!!".toByteArray(), "video/mp4")
            }
            assertNull(adapter.resolve(file, "test"))
        }
        assertNull(PixeldrainDubAdapter { _, _ -> throw IOException("offline") }.resolve(file, "test"))
        assertNull(PixeldrainDubAdapter { _, _ -> error("must not request media") }
            .resolve(file.copy(audioTracks = setOf("ja")), "test"))
    }

    @Test
    fun shippedProviderRejects1155UnapprovedFilesAndUnlistedRoutes() = runBlocking {
        val provider = EggheadDevProvider(adapter = PixeldrainDubAdapter { _, _ -> error("unexpected network") })
        val urls = listOf("https://anikoto.wtf/one-piece-episode-1155/", "${provider.seriesUrl}/17/45",
            "${provider.seriesUrl}/17/1", "${provider.seriesUrl}/18/1", "https://pixeldrain.dev/api/file/HZ9VRHCV")
        for (url in urls) assertFalse(provider.loadLinks(url, false, {}, { fail("unexpected link") }))
        assertNull(provider.load("https://pixeldrain.dev/muhnpace/one-piece"))
    }

    @Test
    fun isolatedHomeAndSeriesPreserveAll274V7EpisodesAndLinks() = runBlocking {
        val v7 = MuhnPaceProvider()
        val dev = EggheadDevProvider()
        val collections = JSONObject(resource("catalogue-2026-11-14.json")).getJSONArray("collections")
        val files = (0 until collections.length()).map(collections::getJSONObject)
            .associate { it.getString("id") to it.getJSONArray("files") }
        val original = v7.seriesFromFiles(v7.seriesUrl, files)!!
        val result = dev.developmentSeries(v7.seriesFromFiles(v7.seriesUrl, files)!!)
        val page = dev.mainPage.single()
        val card = dev.getMainPage(1, MainPageRequest(page.name, page.data, page.horizontalImages)).items.single().list.single()
        assertEquals(dev.seriesUrl, card.url)
        assertEquals(card.url, dev.search("one piece").single().url)
        assertEquals(dev.name, card.apiName)
        assertEquals(card.url, result.uniqueUrl)
        assertNotEquals(original.uniqueUrl, result.uniqueUrl)
        assertNotEquals(original.apiName, result.apiName)
        assertEquals(original.seasonNames, result.seasonNames)
        assertEquals(original.posterUrl, result.posterUrl)
        assertEquals(original.backgroundPosterUrl, result.backgroundPosterUrl)
        assertEquals(original.episodes, result.episodes)
        val frozen = JSONArray(resource("v3-episode-contract.json"))
        assertEquals(274, result.episodes.size)
        assertEquals(274, frozen.length())
        result.episodes.forEachIndexed { index, ep ->
            val row = frozen.getJSONObject(index)
            assertEquals(row.getString("name"), ep.name)
            assertEquals(row.getInt("season"), ep.season)
            assertEquals(row.getInt("episode"), ep.episode)
            assertEquals("https://pixeldrain.dev/api/file/${row.getString("file_id")}", ep.data)
            val links = mutableListOf<ExtractorLink>()
            assertTrue(dev.loadLinks(ep.data, false, {}, links::add))
            assertEquals(ep.data, links.single().url)
            assertEquals(ExtractorLinkType.VIDEO, links.single().type)
        }
        val candidate = EggheadDevProvider(ExpansionCatalogue(mapOf(first.key to listOf(file))))
        val expanded = candidate.developmentSeries(v7.seriesFromFiles(v7.seriesUrl, files)!!)
        assertEquals(original.episodes, expanded.episodes.dropLast(1))
        assertEquals("Egghead", expanded.seasonNames!!.last().name)
        assertEquals("${candidate.seriesUrl}/17/1", expanded.episodes.last().data)
    }
}
