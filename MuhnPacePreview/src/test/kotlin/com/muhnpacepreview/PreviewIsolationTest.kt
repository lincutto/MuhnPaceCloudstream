package com.muhnpacepreview

import com.lagradost.cloudstream3.APIHolder
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.muhnpacedirect.MuhnPacePlugin
import com.muhnpacedirect.MuhnPaceProvider
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class PreviewIsolationTest {
    private fun collections() = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!
        .bufferedReader().use { JSONObject(it.readText()).getJSONArray("collections") }

    @Test
    fun `both real plugins register distinct providers without replacing production`() {
        val production = MuhnPacePlugin()
        val preview = MuhnPacePreviewPlugin()
        production.filename = "production/MuhnPace.cs3"
        preview.filename = "preview/MuhnPacePreview.cs3"
        try {
            production.load()
            preview.load()
            val original = APIHolder.getApiFromNameNull("Muhn Pace Direct (English Dub)")!!
            val isolated = APIHolder.getApiFromNameNull("Muhn Pace v3 Preview")!!
            assertTrue(original is MuhnPaceProvider)
            assertTrue(isolated is MuhnPacePreviewProvider)
            assertNotEquals(original.javaClass.name, isolated.javaClass.name)
            assertEquals(production.filename, original.sourcePlugin)
            assertEquals(preview.filename, isolated.sourcePlugin)
        } finally {
            APIHolder.allProviders.filter { it.sourcePlugin in setOf(production.filename, preview.filename) }
                .forEach { APIHolder.removePluginMapping(it); APIHolder.allProviders.remove(it) }
        }
    }

    @Test
    fun `all baseline edits retain metadata and streams with disjoint production history keys`() = runBlocking {
        val provider = MuhnPacePreviewProvider()
        val collections = collections()
        val search = provider.search("one piece").associateBy { it.url }
        assertEquals(16, search.size)
        val page = provider.mainPage.first()
        assertEquals(16, provider.getMainPage(1, MainPageRequest(page.name, page.data, page.horizontalImages)).items.single().list.size)
        val thumbnailProof = javaClass.getResourceAsStream("/thumbnail-verification-2026-11-14.json")!!
            .bufferedReader().use { JSONObject(it.readText()).getJSONArray("episodes") }
        val thumbnails = (0 until thumbnailProof.length()).associate {
            val record = thumbnailProof.getJSONObject(it)
            record.getString("file_id") to record
        }
        val originalIds = mutableSetOf<Int>()
        val previewIds = mutableSetOf<Int>()
        var count = 0
        for (i in 0 until collections.length()) {
            val collection = collections.getJSONObject(i)
            val id = collection.getString("id")
            val expected = collection.getJSONArray("expected_episodes")
            val files = collection.getJSONArray("files")
            for (fileIndex in 0 until files.length()) {
                val file = files.getJSONObject(fileIndex)
                thumbnails[file.getString("id")]?.let { file.put("thumbnail_href", it.getString("thumbnail_href")) }
            }
            val numbers = (0 until expected.length()).map { expected.getJSONObject(it).getInt("number") }
            for (host in listOf("pixeldrain.dev", "pixeldrain.com")) {
                val originalUrl = "https://$host/l/$id"
                val url = "https://$host/muhnpace-v3-preview/l/$id"
                val load = provider.loadFromFiles(url, collection.getJSONArray("files"))!!
                assertNull(provider.loadFromFiles(originalUrl, collection.getJSONArray("files")))
                assertEquals(url, load.uniqueUrl)
                assertEquals(url, load.url)
                assertEquals("Muhn Pace v3 Preview", load.apiName)
                assertEquals(TvType.TvSeries, load.type)
                assertTrue(load.name.endsWith("(Muhn Pace Preview)"))
                assertEquals(ArcPresentations.byCollection.getValue(id).poster, load.posterUrl)
                assertEquals(ArcPresentations.byCollection.getValue(id).background, load.backgroundPosterUrl)
                assertTrue(load.plot!!.contains("Player numbers identify Muhn Pace edits."))
                assertEquals(search.getValue("https://pixeldrain.dev/muhnpace-v3-preview/l/$id").name, load.name)
                assertEquals(expected.length(), load.episodes.size)
                for ((index, episode) in load.episodes.withIndex()) {
                    assertEquals("https://pixeldrain.dev/api/file/${expected.getJSONObject(index).getString("file_id")}", episode.data)
                    assertEquals(numbers[index], episode.episode)
                    assertEquals(1, episode.season)
                    assertTrue(episode.name!!.contains(" • Muhn Pace ${episode.episode}"))
                    assertTrue(episode.description!!.contains("mapping is unverified"))
                    assertTrue(episode.posterUrl!!.startsWith("https://"))
                    val thumbnail = thumbnails.getValue(expected.getJSONObject(index).getString("file_id"))
                    assertEquals(if (thumbnail.getBoolean("blank")) ArcPresentations.byCollection.getValue(id).episodeFallback
                        else thumbnail.getString("url"), episode.posterUrl)
                }
                // Exact inspected Cloudstream getId / TV episode-ID calculations, including
                // legacy host and provider-not-loaded cases. Include series IDs in collision check.
                for (mainUrl in listOf("https://pixeldrain.dev", "https://pixeldrain.com", "")) {
                    fun addKeys(target: MutableSet<Int>, source: String) {
                        val parent = source.replace(mainUrl, "").replace("/", "").hashCode()
                        target.add(parent)
                        numbers.forEach { target.add(parent + 100000 + it + 1) }
                    }
                    addKeys(originalIds, originalUrl)
                    addKeys(previewIds, load.uniqueUrl)
                }
            }
            count += expected.length()
        }
        assertEquals(274, count)
        assertTrue("Preview must never write any baseline production series/episode key",
            originalIds.intersect(previewIds).isEmpty())
    }

    @Test
    fun `preview preserves direct stream callback and rejects invalid input`() = runBlocking {
        val provider = MuhnPacePreviewProvider()
        val links = mutableListOf<ExtractorLink>()
        assertTrue(provider.loadLinks("https://pixeldrain.dev/api/file/yUkgcapt", false, {}, links::add))
        assertEquals("https://pixeldrain.dev/api/file/yUkgcapt", links.single().url)
        assertEquals("Muhn Pace v3 Preview", links.single().source)
        assertFalse(provider.loadLinks("https://example.org/file/yUkgcapt", false, {}, links::add))
    }
}
