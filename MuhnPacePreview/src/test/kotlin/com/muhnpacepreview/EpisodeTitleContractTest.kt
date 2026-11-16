package com.muhnpacepreview

import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.utils.ExtractorLink
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

/** Frozen v3 acceptance data, independent of the runtime title/index formatters.
 * These tests check provider inputs, NOT whether Cloudstream hides its own labels.
 */
class EpisodeTitleContractTest {
    private fun resource(name: String) = javaClass.getResourceAsStream("/$name")!!
        .bufferedReader().use { it.readText() }

    @Test
    fun `all 274 actual preview names and identity inputs retain the validated v3 contract`() = runBlocking {
        val rows = JSONObject(resource("catalogue-2026-11-14.json")).getJSONArray("collections")
        val files = (0 until rows.length()).associate {
            val row = rows.getJSONObject(it)
            row.getString("id") to row.getJSONArray("files")
        }
        val expected = JSONArray(resource("v3-episode-contract.json"))
        val provider = MuhnPacePreviewProvider()
        val load = provider.seriesFromFiles(provider.seriesUrl, files)!!
        assertEquals("https://pixeldrain.dev/muhnpace-v3-preview/one-piece", load.url)
        assertEquals(load.url, load.uniqueUrl)
        assertEquals("Muhn Pace v3 Preview", load.apiName)
        assertEquals(TvType.TvSeries, load.type)
        assertEquals((1..16).toList(), load.seasonNames!!.map { it.season })
        assertEquals(274, expected.length())
        assertEquals(expected.length(), load.episodes.size)
        val parent = "muhnpace-v3-previewone-piece".hashCode()
        val ids = mutableSetOf<Int>()
        for ((index, ep) in load.episodes.withIndex()) {
            val row = expected.getJSONObject(index)
            assertEquals(row.getString("name"), ep.name)
            assertFalse(Regex("^\\s*\\d+[.]\\s").containsMatchIn(ep.name!!))
            assertFalse(ep.name!!.contains("Muhn Pace", ignoreCase = true))
            assertFalse(ep.name!!.any { Character.getType(it) == Character.FORMAT.toInt() })
            assertEquals(row.getInt("season"), ep.season)
            assertEquals(row.getInt("episode"), ep.episode)
            assertEquals("https://pixeldrain.dev/api/file/${row.getString("file_id")}", ep.data)
            assertFalse(ep.description.isNullOrBlank())
            val expectedId = parent + row.getInt("season") * 100000 + row.getInt("episode") + 1
            assertEquals(expectedId, parent + ep.season!! * 100000 + ep.episode!! + 1)
            ids += expectedId
            val links = mutableListOf<ExtractorLink>()
            assertTrue(provider.loadLinks(ep.data, false, {}, links::add))
            assertEquals(ep.data, links.single().url)
        }
        assertEquals(274, ids.size)
        assertEquals(load.episodes, load.episodes.sortedBy { it.season!! * 10000 + it.episode!! })
        assertEquals(listOf("Gatz - 728-730", "Heaven and Earth - 730-733", "Prostration - 734-736"),
            load.episodes.filter { it.season == 12 && it.episode in 43..45 }.map { it.name })

        // Legacy routes keep source edit numbers, including the non-one season origins.
        for (i in 0 until rows.length()) {
            val collection = rows.getJSONObject(i)
            val url = "https://pixeldrain.dev/muhnpace-v3-preview/l/${collection.getString("id")}"
            val legacy = provider.loadFromFiles(url, collection.getJSONArray("files"))!!
            assertEquals(url, legacy.uniqueUrl)
            val contract = (0 until expected.length()).map { expected.getJSONObject(it) }
                .filter { it.getInt("season") == i + 1 }
            assertEquals(contract.size, legacy.episodes.size)
            legacy.episodes.zip(contract).forEach { (ep, row) ->
                assertEquals(1, ep.season)
                assertEquals(row.getInt("edit"), ep.episode)
                assertEquals(row.getString("name"), ep.name)
                assertEquals("https://pixeldrain.dev/api/file/${row.getString("file_id")}", ep.data)
            }
        }
    }
}
