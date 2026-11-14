package com.muhnpacedirect

import com.lagradost.cloudstream3.MainPageRequest
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class ArcArtworkTest {
    private fun resource(name: String) = javaClass.getResourceAsStream(name)!!.bufferedReader()
        .use { JSONObject(it.readText()) }

    @Test
    fun `every arc uses its own verified poster and HD background`() {
        val proof = resource("/artwork-verification-2026-11-14.json")
        val commit = proof.getString("artwork_commit")
        assertTrue(commit.matches(Regex("[0-9a-f]{40}")))
        val images = proof.getJSONArray("images")
        assertEquals(32, images.length())
        val byCollection = (0 until images.length()).map { images.getJSONObject(it) }
            .groupBy { it.getString("collection_id") }
        assertEquals(ArcPresentations.byCollection.keys, byCollection.keys)
        val distinctPosters = mutableSetOf<String>()
        for ((id, presentation) in ArcPresentations.byCollection) {
            val records = byCollection.getValue(id).associateBy { it.getString("kind") }
            assertEquals(setOf("poster", "background"), records.keys)
            for ((kind, record) in records) {
                assertEquals(200, record.getInt("status"))
                assertEquals("image/png", record.getString("content_type"))
                assertTrue(record.getString("sha256").matches(Regex("[0-9a-f]{64}")))
                assertEquals(if (kind == "poster") 768 else 1920, record.getInt("width"))
                assertEquals(if (kind == "poster") 1152 else 1080, record.getInt("height"))
                assertTrue(record.getString("url").startsWith(
                    "https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/$commit/assets/artwork/",
                ))
                assertEquals(record.getString("url"), if (kind == "poster") presentation.poster else presentation.background)
            }
            distinctPosters.add(presentation.poster!!)
            assertEquals(presentation.background, presentation.episodeFallback)
        }
        assertEquals(16, distinctPosters.size)
    }

    @Test
    fun `real home and search responses retain identities and expose matching posters`() = runBlocking {
        val provider = MuhnPaceProvider()
        val page = provider.mainPage.single()
        val request = MainPageRequest(page.name, page.data, page.horizontalImages)
        val home = provider.getMainPage(1, request)
        val cards = home.items.single().list
        val snapshot = resource("/catalogue-2026-11-14.json").getJSONArray("collections")
        assertEquals(16, cards.size)
        assertFalse(home.hasNext)
        assertFalse(home.items.single().isHorizontalImages)
        for ((index, card) in cards.withIndex()) {
            val collection = snapshot.getJSONObject(index)
            val id = collection.getString("id")
            assertEquals("https://pixeldrain.dev/l/$id", card.url)
            assertEquals("Muhn Pace • ${collection.getString("arc_title")} (Dub)", card.name)
            assertEquals("Muhn Pace Direct (English Dub)", card.apiName)
            assertEquals(ArcPresentations.byCollection.getValue(id).poster, card.posterUrl)
        }
        val results = provider.search("one piece")
        assertEquals(cards.map { it.url to it.posterUrl }, results.map { it.url to it.posterUrl })
        val dressrosa = provider.search("dressrosa").single()
        assertEquals("https://pixeldrain.dev/l/f8HTXmAk", dressrosa.url)
        assertEquals(ArcPresentations.byCollection.getValue("f8HTXmAk").poster, dressrosa.posterUrl)
        assertTrue(provider.getMainPage(2, request).items.single().list.isEmpty())
    }

    @Test
    fun `missing source thumbnails use the correct arc background for all baseline edits`() {
        val collections = resource("/catalogue-2026-11-14.json").getJSONArray("collections")
        val provider = MuhnPaceProvider()
        var checked = 0
        for (index in 0 until collections.length()) {
            val collection = collections.getJSONObject(index)
            val id = collection.getString("id")
            val presentation = ArcPresentations.byCollection.getValue(id)
            // This preserved source fixture intentionally has no thumbnail_href fields.
            val episodes = provider.episodesFromFiles(
                id, collection.getString("arc_title"), collection.getJSONArray("files"), presentation.episodeFallback,
            )
            for (episode in episodes) {
                assertNotNull(episode.posterUrl)
                assertEquals(presentation.background, episode.posterUrl)
                assertTrue(episode.description!!.contains("Muhn Pace edit ${episode.episode}."))
                checked++
            }
        }
        assertEquals(274, checked)
    }
}
