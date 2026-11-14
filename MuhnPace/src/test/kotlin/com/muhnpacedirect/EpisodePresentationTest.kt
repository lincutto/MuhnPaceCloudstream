package com.muhnpacedirect

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class EpisodePresentationTest {
    private fun file(thumbnail: String? = "/file/yUkgcapt/thumbnail"): JSONObject = JSONObject()
        .put("id", "yUkgcapt")
        .put("name", "[Muhn Pace] Dressrosa 25.mp4")
        .put("mime_type", "video/mp4")
        .apply { thumbnail?.let { put("thumbnail_href", it) } }

    private fun metadata(file: JSONObject) =
        EpisodeCatalog.select("f8HTXmAk", "Dressrosa", JSONArray().put(file)).single()

    @Test
    fun `exact edit thumbnail is passed to Cloudstream without changing playback data`() {
        val episode = MuhnPaceProvider().episodesFromFiles(
            "f8HTXmAk", "Dressrosa", JSONArray().put(file()), "https://example.org/arc-poster.jpg",
        ).single()
        assertEquals("https://pixeldrain.dev/api/file/yUkgcapt/thumbnail?width=256&height=144", episode.posterUrl)
        assertEquals("https://pixeldrain.dev/api/file/yUkgcapt", episode.data)
        assertEquals("Dressrosa • Episode 25", episode.name)
        assertEquals(25, episode.episode)
        assertEquals(1, episode.season)
        assertEquals(
            "Dressrosa • Muhn Pace edit 25. English dub.\nOriginal One Piece episode mapping is unverified.",
            episode.description,
        )
    }

    @Test
    fun `missing or mismatched thumbnails use a deliberate arc fallback`() {
        val fallback = "https://example.org/arc-poster.jpg"
        for (href in listOf(null, "", "/file/differentFile/thumbnail", "https://untrusted.example/image.jpg")) {
            val entry = metadata(file(href))
            assertNull(entry.thumbnailUrl)
            assertEquals(fallback, entry.withPresentation("Dressrosa", fallback).thumbnailUrl)
            assertNull(entry.withPresentation("Dressrosa").thumbnailUrl)
        }
    }

    @Test
    fun `presentation preserves verified mappings and edition information`() {
        val original = metadata(file()).copy(
            edition = EpisodeEdition.FILLER,
            originalEpisodes = OriginalEpisodeMapping(1, 3, "https://example.org/synthetic-evidence"),
        )
        val presented = original.withPresentation("Dressrosa")
        assertEquals(original.fileId, presented.fileId)
        assertEquals(original.collectionId, presented.collectionId)
        assertEquals(original.editNumber, presented.editNumber)
        assertEquals(original.title, presented.title)
        assertEquals(original.originalEpisodes, presented.originalEpisodes)
        assertTrue(presented.description.contains("Filler edit."))
        assertTrue(presented.description.contains("Original One Piece episodes: 1–3."))
        assertFalse(presented.description.contains("unverified"))
    }

    @Test
    fun `special collection description does not repeat the episode label`() {
        val presented = metadata(file()).copy(editNumber = 15, edition = EpisodeEdition.FIXED_GROUP_SHOT)
            .withPresentation("Fishman Island • Episode 15")
        assertEquals("Fishman Island • Muhn Pace edit 15. English dub. Fixed group shot.", presented.summary)
    }

    @Test
    fun `all recovered selected edits use verified thumbnails or arc art for blank frames`() {
        fun resource(name: String) = javaClass.getResourceAsStream(name)!!.bufferedReader().use { JSONObject(it.readText()) }
        val proof = resource("/thumbnail-verification-2026-11-14.json").getJSONArray("episodes")
        val verified = (0 until proof.length()).associate { index ->
            val record = proof.getJSONObject(index)
            assertEquals(200, record.getInt("status"))
            assertEquals(256, record.getInt("width"))
            assertEquals(144, record.getInt("height"))
            assertEquals("image/png", record.getString("content_type"))
            record.getString("file_id") to record
        }
        val collections = resource("/catalogue-2026-11-14.json").getJSONArray("collections")
        var checked = 0
        var blankFrames = 0
        for (index in 0 until collections.length()) {
            val collection = collections.getJSONObject(index)
            val files = collection.getJSONArray("files")
            for (fileIndex in 0 until files.length()) {
                val sourceFile = files.getJSONObject(fileIndex)
                verified[sourceFile.getString("id")]?.let {
                    sourceFile.put("thumbnail_href", it.getString("thumbnail_href"))
                }
            }
            val fallback = ArcPresentations.byCollection.getValue(collection.getString("id")).episodeFallback
            val entries = MuhnPaceProvider().episodesFromFiles(
                collection.getString("id"), collection.getString("arc_title"), files,
                fallback,
            )
            for (episode in entries) {
                val fileId = episode.data.substringAfterLast('/')
                val record = verified.getValue(fileId)
                if (record.getBoolean("blank")) {
                    assertTrue(record.getInt("max_rgb_channel") <= 1)
                    assertNotNull(fallback)
                    assertEquals(fallback, episode.posterUrl)
                    blankFrames++
                } else {
                    assertEquals(record.getString("url"), episode.posterUrl)
                }
                checked++
            }
        }
        assertEquals(274, checked)
        assertEquals(8, blankFrames)
    }
}
