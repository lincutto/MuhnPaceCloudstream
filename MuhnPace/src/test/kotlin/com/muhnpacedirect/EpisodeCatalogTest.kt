package com.muhnpacedirect

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class EpisodeCatalogTest {
    private fun file(name: String, id: String = "testFile", mime: String = "video/mp4") =
        JSONObject().put("name", name).put("id", id).put("mime_type", mime)

    private fun select(vararg files: JSONObject, arc: String = "Dressrosa") =
        EpisodeCatalog.select("collection", arc, JSONArray(files.toList()))

    @Test
    fun `all recovered selections retain their playback and watch history inputs`() {
        val snapshot = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!
            .bufferedReader().use { JSONObject(it.readText()) }
        val collections = snapshot.getJSONArray("collections")
        val provider = MuhnPaceProvider()
        assertEquals(16, collections.length())
        assertEquals("Muhn Pace Direct (English Dub)", provider.name)
        assertEquals("https://pixeldrain.dev", provider.mainUrl)
        var total = 0
        for (index in 0 until collections.length()) {
            val collection = collections.getJSONObject(index)
            val id = collection.getString("id")
            val title = collection.getString("arc_title")
            val files = collection.getJSONArray("files")
            val expected = collection.getJSONArray("expected_episodes")
            val metadata = EpisodeCatalog.select(id, title, files)
            val episodes = provider.episodesFromFiles(id, title, files)
            assertEquals(title, expected.length(), episodes.size)
            assertEquals(episodes.size, metadata.size)
            for (episodeIndex in episodes.indices) {
                val baseline = expected.getJSONObject(episodeIndex)
                val episode = episodes[episodeIndex]
                val entry = metadata[episodeIndex]
                assertEquals(title, "https://pixeldrain.dev/api/file/${baseline.getString("file_id")}", episode.data)
                assertEquals(title, baseline.getInt("number"), episode.episode)
                assertEquals(title, 1, episode.season)
                assertEquals(id, entry.collectionId)
                assertEquals(entry.title, episode.name)
                assertFalse(entry.title.contains('['))
                assertFalse(entry.title.endsWith(".mp4"))
                assertNull("No mapping evidence exists for ${entry.fileId}", entry.originalEpisodes)
                assertTrue(episode.description!!.contains("unverified"))
            }
            total += episodes.size
        }
        assertEquals(274, total)
    }

    @Test
    fun `numeric order is independent of file order and zero padding`() {
        val episodes = select(
            file("[Muhn Pace] Dressrosa 10.mp4", "ten"),
            file("[Muhn Pace] Dressrosa 02.mp4", "two"),
            file("[Muhn Pace] Dressrosa 1.mp4", "one"),
        )
        assertEquals(listOf(1, 2, 10), episodes.map { it.editNumber })
        assertEquals(listOf("one", "two", "ten"), episodes.map { it.fileId })
        assertEquals("Dressrosa • Episode 2", episodes[1].title)
        assertEquals("[Muhn Pace] Dressrosa 02.mp4", episodes[1].sourceFileName)
    }

    @Test
    fun `mid arc numbers and gaps are never renumbered`() {
        val episodes = select(
            file("[Muhn Pace] Wano 95.mp4", "last"),
            file("[Muhn Pace] Wano 71.mp4", "first"),
            arc = "Wano • Act 3",
        )
        assertEquals(listOf(71, 95), episodes.map { it.editNumber })
        assertEquals("Wano • Act 3 • Episode 71", episodes.first().title)
    }

    @Test
    fun `bracketed chapters versions and resolutions are not anime mappings or edit numbers`() {
        val name = "[V3][DUB][632-633] Fishman Island 15 (FIXED GROUP SHOT) [1080p].mp4"
        val episode = select(file(name), arc = "Fishman Island • Episode 15").single()
        assertEquals(15, episode.editNumber)
        assertEquals("Fishman Island • Episode 15 (Fixed group shot)", episode.title)
        assertEquals(EpisodeEdition.FIXED_GROUP_SHOT, episode.edition)
        assertNull(episode.originalEpisodes)
        assertFalse(episode.description.contains("632"))
    }

    @Test
    fun `regular editions win while equal priority retains source order`() {
        val episodes = select(
            file("[Muhn Pace] Dressrosa 01 Alt.mp4", "alt"),
            file("[Muhn Pace] Dressrosa 01 Fillerver.mp4", "filler"),
            file("[Muhn Pace] Dressrosa 01.mp4", "regular"),
            file("[Muhn Pace] Dressrosa 01.mp4", "secondRegular"),
        )
        assertEquals("regular", episodes.single().fileId)
        assertEquals(EpisodeEdition.REGULAR, episodes.single().edition)
        val reversed = select(
            file("[Muhn Pace] Dressrosa 01.mp4", "regular"),
            file("[Muhn Pace] Dressrosa 01 Alt.mp4", "alt"),
        )
        assertEquals("regular", reversed.single().fileId)
    }

    @Test
    fun `lone filler and alternate editions remain available and clearly labelled`() {
        val episodes = select(
            file("[Muhn Pace] Wano 94 - Filler.mp4", "filler"),
            file("[Muhn Pace] Wano 80 Alt.mp4", "alt"),
            arc = "Wano • Act 3",
        )
        assertEquals(listOf(80, 94), episodes.map { it.editNumber })
        assertEquals("Wano • Act 3 • Episode 94 (Filler edit)", episodes.last().title)
        assertEquals("Wano • Act 3 • Episode 80 (Alternate edit)", episodes.first().title)
        val tied = select(
            file("[Muhn Pace] Dressrosa 1 Filler.mp4", "first"),
            file("[Muhn Pace] Dressrosa 1 Alt.mp4", "second"),
        )
        assertEquals("first", tied.single().fileId)
    }

    @Test
    fun `subtitles comparisons unlabelled files and notes are excluded`() {
        assertTrue(select(
            file("[Muhn Pace][SUB] Dressrosa 1.mp4"),
            file("[DUB][SUB] Dressrosa 1.mp4"),
            file("[V3] Dressrosa 1.mp4"),
            file("[DUB] Dressrosa 1 Comparison.mp4"),
            file("[Muhn Pace] Dressrosa 1.txt", mime = "text/plain"),
            file("For more Dub on this arc use One Pace"),
        ).isEmpty())
    }

    @Test
    fun `malformed entries ids and numbers cannot become playable episodes`() {
        val files = JSONArray().put(JSONObject.NULL).put(42).put("not a file").put(JSONObject())
        for (id in listOf("", "../file", "a/b", "x?download", "x#fragment", " ", "a".repeat(65))) {
            files.put(file("[Muhn Pace] Dressrosa 1.mp4", id))
        }
        for (number in listOf("0", "000", "-1", "1.5", "10000", "1-2", "1080p")) {
            files.put(file("[Muhn Pace] Dressrosa $number.mp4"))
        }
        assertTrue(EpisodeCatalog.select("collection", "Dressrosa", files).isEmpty())
        assertTrue(select(file("[DUB][1080p][632-633] Dressrosa.mp4")).isEmpty())
    }

    @Test
    fun `video MIME and supported extensions retain existing acceptance`() {
        val episodes = select(
            file("[muhnpace] Dressrosa 1.MKV", "alpha_-1", "application/octet-stream"),
            file("[DUB] Dressrosa 2", "mimeOnly", "VIDEO/MP4"),
        )
        assertEquals(listOf(1, 2), episodes.map { it.editNumber })
        assertEquals("alpha_-1", episodes.first().fileId)
    }

    @Test
    fun `source arc aliases use catalogue titles for display`() {
        assertEquals("Post Marineford • Episode 7", select(
            file("[Muhn Pace] Post War 7.mp4"), arc = "Post Marineford",
        ).single().title)
        assertEquals("Thriller Bark • Episode 1", select(
            file("[Muhn Pace] Thrillerbark 01.mp4"), arc = "Thriller Bark",
        ).single().title)
    }

    @Test
    fun `other arc numbers and unexpected numeric suffixes are not used`() {
        assertTrue(select(
            file("[Muhn Pace] Wano 71.mp4"),
            file("[Muhn Pace] Dressrosa 01 720p.mp4"),
            file("[DUB] Dressrosa Act 3 71.mp4"),
        ).isEmpty())
    }

    @Test
    fun `synthetic verified mappings require a range and source and do not affect identity`() {
        val unknown = select(file("[Muhn Pace] Dressrosa 01.mp4", "stableId")).single()
        // Synthetic numbers exercise rendering only; these are NOT catalogue evidence.
        val verified = unknown.copy(originalEpisodes = OriginalEpisodeMapping(1, 3, "https://example.org/test-evidence"))
        assertTrue(unknown.description.contains("unverified"))
        assertEquals("Original One Piece episodes: 1–3. Source: https://example.org/test-evidence", verified.description)
        assertEquals(unknown.fileId, verified.fileId)
        assertEquals(unknown.editNumber, verified.editNumber)
        assertEquals("1", OriginalEpisodeMapping(1, 1, "https://example.org/test-evidence").label)
        assertThrows(IllegalArgumentException::class.java) { OriginalEpisodeMapping(0, 3, "https://example.org") }
        assertThrows(IllegalArgumentException::class.java) { OriginalEpisodeMapping(3, 1, "https://example.org") }
        assertThrows(IllegalArgumentException::class.java) { OriginalEpisodeMapping(1, 3, "") }
    }
}
