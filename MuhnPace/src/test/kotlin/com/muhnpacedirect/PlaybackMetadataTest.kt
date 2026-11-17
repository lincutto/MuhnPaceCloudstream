package com.muhnpacedirect

import com.lagradost.cloudstream3.ErrorLoadingException
import com.lagradost.cloudstream3.TvType
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class PlaybackMetadataTest {
    private fun collections() = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!
        .bufferedReader().use { JSONObject(it.readText()).getJSONArray("collections") }

    @Test
    fun `real load responses provide player and resume labels without changing history inputs`() = runBlocking {
        val provider = MuhnPaceProvider()
        val collections = collections()
        var checked = 0
        for (i in 0 until collections.length()) {
            val collection = collections.getJSONObject(i)
            val id = collection.getString("id")
            val arcTitle = collection.getString("arc_title")
            val expectedName = "One Piece • ${SeriesCatalogue.arcs.single { it.listId == id }.seasonTitle} (Muhn Pace)"
            val files = collection.getJSONArray("files")
            // Keep even legacy URLs exactly as before: normalizing them would change history keys.
            for (host in listOf("pixeldrain.dev", "pixeldrain.com")) {
                val url = "https://$host/l/$id"
                val load = provider.loadFromFiles(url, files)!!
                assertEquals(expectedName, load.name)
                assertEquals(url, load.url)
                assertEquals(url, load.uniqueUrl)
                assertEquals("Muhn Pace Rework (by lincutto)", load.apiName)
                assertEquals(TvType.TvSeries, load.type)
                assertNull(load.seasonNames)
                assertTrue(load.syncData.isEmpty()) // No anime-ID lookup that could overwrite edit metadata.
                val presentation = ArcPresentations.byCollection.getValue(id)
                assertEquals(presentation.poster, load.posterUrl)
                assertEquals(presentation.background, load.backgroundPosterUrl)
                assertEquals(presentation.tags, load.tags)
                assertEquals(presentation.description(load.episodes.size), load.plot)
                assertTrue(load.plot!!.substringBefore('\n').contains("English dub."))
                assertTrue(load.plot!!.contains(presentation.synopsis))
                val expected = collection.getJSONArray("expected_episodes")
                assertEquals(expected.length(), load.episodes.size)
                for ((index, episode) in load.episodes.withIndex()) {
                    val baseline = expected.getJSONObject(index)
                    assertEquals("https://pixeldrain.dev/api/file/${baseline.getString("file_id")}", episode.data)
                    assertEquals(baseline.getInt("number"), episode.episode)
                    assertEquals(1, episode.season)
                    expectedEpisodeName(baseline.getString("file_id"))?.let { assertEquals(it, episode.name) }
                    assertFalse(episode.description!!.contains("Muhn Pace edit"))
                    assertEquals(EditResearchRegistry.entries.singleOrNull { it.fileId == baseline.getString("file_id") }?.thumbnailUrl ?: presentation.episodeFallback, episode.posterUrl)
                }
            }
            checked += collection.getJSONArray("expected_episodes").length()
        }
        assertEquals(274, checked)
    }

    @Test
    fun `synthetic verified mapping reaches real Cloudstream episode fields without renumbering`() {
        val files = JSONArray().put(JSONObject().put("id", "syntheticFile")
            .put("name", "[Muhn Pace] Dressrosa 25.mp4").put("mime_type", "video/mp4"))
        val unknown = EpisodeCatalog.select("syntheticCollection", "Dressrosa", files).single()
        // Test evidence only: these numbers are not a claim about Dressrosa edit 25 or episode 684.
        val mapped = unknown.copy(originalEpisodes = OriginalEpisodeMapping(1, 3, "https://example.org/synthetic"))
        val provider = MuhnPaceProvider()
        val baseline = provider.episodeFromMetadata(unknown)
        val episode = provider.episodeFromMetadata(mapped)
        assertEquals("One Piece - 1-3", episode.name)
        assertEquals("", episode.description)
        assertFalse(episode.description!!.contains("Muhn Pace edit"))
        assertFalse(episode.description!!.contains("unverified"))
        assertEquals(baseline.data, episode.data)
        assertEquals(25, episode.episode)
        assertEquals(baseline.season, episode.season)
        assertEquals(baseline.posterUrl, episode.posterUrl)
    }

    @Test
    fun `arbitrary remote mapping fields and descriptions cannot become evidence`() {
        val files = JSONArray().put(JSONObject()
            .put("id", "yUkgcapt").put("name", "[Muhn Pace][683-686] Dressrosa 25.mp4")
            .put("mime_type", "video/mp4").put("description", "One Piece episodes 683-686")
            .put("original_episodes", "683-686").put("title", "Invented story title"))
        val episode = MuhnPaceProvider().episodesFromFiles("f8HTXmAk", "Dressrosa", files).single()
        assertEquals("One Piece", episode.name)
        assertEquals("", episode.description)
        assertFalse(episode.description!!.contains("683"))
        assertFalse(episode.description!!.contains("Invented"))
    }

    @Test
    fun `load builder retains invalid URL and empty list behaviour`() = runBlocking {
        val provider = MuhnPaceProvider()
        assertNull(provider.loadFromFiles("https://example.org/l/f8HTXmAk", JSONArray()))
        assertNull(provider.loadFromFiles("https://pixeldrain.dev/l/unknown", JSONArray()))
        try {
            provider.loadFromFiles("https://pixeldrain.dev/l/f8HTXmAk", JSONArray())
            fail("Empty playable catalogue must still report an error")
        } catch (_: ErrorLoadingException) { }
    }
}
