package com.muhnpacedirect

import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class ArcPresentationTest {
    @Test
    fun `every preserved collection has a distinct useful synopsis`() {
        val snapshot = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!.bufferedReader()
            .use { JSONObject(it.readText()) }.getJSONArray("collections")
        val ids = (0 until snapshot.length()).map { snapshot.getJSONObject(it).getString("id") }.toSet()
        assertEquals(ids, ArcPresentations.byCollection.keys)
        assertEquals(16, ArcPresentations.byCollection.values.map { it.synopsis }.toSet().size)
        for (presentation in ArcPresentations.byCollection.values) {
            assertTrue(presentation.synopsis.length in 100..400)
            assertTrue(presentation.tags.containsAll(listOf("Anime", "Adventure")))
            assertFalse(presentation.description(48).contains("hotlink"))
            assertTrue(presentation.description(48).contains("48 Muhn Pace edits available"))
            assertTrue(presentation.description(1).contains("1 Muhn Pace edit available"))
        }
    }

    @Test
    fun `arc coverage is clearly distinguished from an individual edit mapping`() {
        val presentation = ArcPresentation(
            synopsis = "Synthetic test synopsis.",
            originalArcEpisodes = OriginalEpisodeMapping(1, 3, "https://example.org/synthetic-arc-source"),
        )
        assertTrue(presentation.description(2).contains("Original One Piece arc: episodes 1–3."))
        assertTrue(presentation.description(2).contains("Individual edit mappings may be unverified."))
    }

    @Test
    fun `missing background uses poster and missing poster uses configured fallback`() {
        val fallback = "https://example.org/fallback.jpg"
        val missing = ArcPresentation("Test", fallbackPosterUrl = fallback)
        assertEquals(fallback, missing.poster)
        assertEquals(fallback, missing.background)
        assertEquals(fallback, missing.episodeFallback)
        val poster = "https://example.org/arc.jpg"
        assertEquals(poster, missing.copy(posterUrl = poster).background)
        val background = "https://example.org/background.jpg"
        assertEquals(background, missing.copy(posterUrl = poster, backgroundUrl = background).episodeFallback)
    }
}
