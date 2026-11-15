package com.muhnpacedirect

import com.lagradost.cloudstream3.ErrorLoadingException
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class SeriesCatalogueTest {
    private fun snapshot() = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!
        .bufferedReader().use { JSONObject(it.readText()).getJSONArray("collections") }
    private fun files() = snapshot().let { rows -> (0 until rows.length()).associate {
        val row = rows.getJSONObject(it); row.getString("id") to row.getJSONArray("files")
    } }

    @Test
    fun `native series contains sixteen named chronological collection seasons and all baseline streams`() = runBlocking {
        val provider = MuhnPaceProvider()
        val load = provider.seriesFromFiles(provider.seriesUrl, files())!!
        assertEquals("One Piece (Muhn Pace)", load.name)
        assertEquals(provider.seriesUrl, load.uniqueUrl)
        assertEquals(274, load.episodes.size)
        assertEquals((1..16).toList(), load.seasonNames!!.map { it.season })
        assertEquals((1..16).toList(), load.seasonNames!!.map { it.displaySeason })
        assertEquals("Dressrosa", load.seasonNames!![11].name)
        assertEquals("Fishman Island • Alternate scene", load.seasonNames!![9].name)
        assertTrue(load.syncData.isEmpty()) // Never borrow normal-anime skip times or external numbering.
        val rows = snapshot()
        val expectedStreams = mutableListOf<String>()
        for (i in 0 until rows.length()) {
            val row = rows.getJSONObject(i)
            val expected = row.getJSONArray("expected_episodes")
            val season = load.episodes.filter { it.season == i + 1 }
            assertEquals(expected.length(), season.size)
            assertEquals((1..expected.length()).toList(), season.map { it.episode })
            for ((index, ep) in season.withIndex()) {
                val fileId = expected.getJSONObject(index).getString("file_id")
                expectedStreams += "https://pixeldrain.dev/api/file/$fileId"
                assertEquals(expectedStreams.last(), ep.data)
                assertEquals(expectedEpisodeName(fileId), ep.name)
                assertFalse(ep.description!!.contains("Muhn Pace edit"))
                assertTrue(ep.posterUrl!!.startsWith("https://"))
            }
        }
        // Same sort and dedup inputs as inspected ResultViewModel2; no mocked Episode objects.
        val hostSorted = load.episodes.sortedBy { it.season!! * 10000 + it.episode!! }
        assertEquals(expectedStreams, hostSorted.map { it.data })
        val parent = load.uniqueUrl.replace(provider.mainUrl, "").replace("/", "").hashCode()
        assertEquals(274, hostSorted.map { parent + it.season!! * 100000 + it.episode!! + 1 }.toSet().size)
        assertEquals(274, load.getTotalEpisodeIndex(25, 16))
        assertTrue(load.plot!!.contains(ArcPresentations.byCollection.getValue("f8HTXmAk").synopsis))
    }

    @Test
    fun `missing source entry cannot shift later navigation or resume keys`() = runBlocking {
        val provider = MuhnPaceProvider()
        val original = files()
        val full = provider.seriesFromFiles(provider.seriesUrl, original)!!.episodes
        val reduced = original.toMutableMap()
        val first = original.getValue("6VyLVkB2")
        reduced["6VyLVkB2"] = JSONArray((0 until first.length()).map { first.getJSONObject(it) }
            .filterNot { it.getString("id") == full.first().data.substringAfterLast('/') })
        val after = provider.seriesFromFiles(provider.seriesUrl, reduced)!!.episodes
        assertEquals(273, after.size)
        val positions = full.associate { it.data to (it.season to it.episode) }
        assertTrue(after.all { positions[it.data] == (it.season to it.episode) })
    }

    @Test
    fun `an unavailable whole season fails explicitly rather than hiding files`() = runBlocking {
        val provider = MuhnPaceProvider()
        for (broken in listOf(files() - "f8HTXmAk", files() + ("f8HTXmAk" to JSONArray()))) {
            try { provider.seriesFromFiles(provider.seriesUrl, broken); fail("Incomplete series accepted") }
            catch (_: ErrorLoadingException) { }
        }
        assertNull(provider.seriesFromFiles("https://example.org/muhnpace/one-piece", files()))
        assertNull(provider.seriesFromFiles("https://pixeldrain.dev/muhnpace-v3-preview/one-piece", files()))
        assertNotNull(provider.seriesFromFiles("https://pixeldrain.com/muhnpace/one-piece", files()))
    }

    @Test
    fun `anime ranges are titles only so partial research cannot reorder the season`() = runBlocking {
        val provider = MuhnPaceProvider()
        val dressrosa = provider.seriesFromFiles(provider.seriesUrl, files())!!.episodes.filter { it.season == 12 }
        assertEquals((1..48).toList(), dressrosa.map { it.episode })
        assertEquals("White Town - 701-702", dressrosa[31].name)
        assertEquals(32, dressrosa[31].episode)
        assertFalse(dressrosa.any { it.episode == 701 })
        assertEquals(1, SeriesCatalogue.arcs.last().navigationIndex(71))
    }
}
