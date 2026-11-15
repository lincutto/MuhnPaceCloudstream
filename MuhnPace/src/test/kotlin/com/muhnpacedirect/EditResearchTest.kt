package com.muhnpacedirect

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class EditResearchTest {
    private fun entries(): List<EpisodeMetadata> {
        val rows = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!
            .bufferedReader().use { JSONObject(it.readText()).getJSONArray("collections") }
        val dressrosa = (0 until rows.length()).map { rows.getJSONObject(it) }.single { it.getString("id") == "f8HTXmAk" }
        return EpisodeCatalog.select("f8HTXmAk", "Dressrosa", dressrosa.getJSONArray("files"))
            .filter { it.editNumber in 30..37 }
    }

    @Test
    fun `eight exact-file stories use official One Pace names with three derived and five unresolved ranges`() {
        val entries = entries()
        assertEquals(8, entries.size)
        assertEquals(3, entries.count { it.evidenceStatus == MappingEvidence.ONE_PACE_DERIVED })
        assertEquals(5, entries.count { it.evidenceStatus == MappingEvidence.CONFLICTING })
        assertTrue(entries.none { it.originalEpisodes != null })
        assertEquals(8, entries.map { it.research!!.synopsis }.toSet().size)
        assertEquals(8, entries.map { it.withPresentation("Dressrosa").thumbnailUrl }.toSet().size)
        for (entry in entries) {
            assertEquals(expectedEpisodeName(entry.fileId), entry.title)
            assertFalse(entry.title.contains("Muhn Pace"))
            assertFalse(entry.description.contains("Muhn Pace edit"))
            assertTrue(entry.description.startsWith(entry.research!!.synopsis))
            if (entry.evidenceStatus == MappingEvidence.CONFLICTING) {
                assertFalse(entry.title.any { it.isDigit() })
                assertTrue(entry.description.contains("references disagree"))
            } else assertTrue(entry.description.contains("One Pace-derived"))
        }
    }

    @Test
    fun `Law childhood supports partial scene evidence without promoting the full range to directly verified`() {
        val law = entries().single { it.fileId == "Q5RqeqED" }
        assertEquals(listOf(701), law.research!!.sceneConfirmedEpisodes)
        assertEquals(listOf(701, 702), law.research!!.acceptedEpisodes)
        assertEquals(MappingEvidence.ONE_PACE_DERIVED, law.evidenceStatus)
        assertTrue(law.description.contains("Scene confirmed: OP 701 (partial)"))
        assertTrue(law.description.contains("Law, the Boy from the White Town"))
        assertTrue(law.description.contains("OP 702:"))
    }

    @Test
    fun `wrong collection file edition number filename or hash cannot inherit research`() {
        val entry = entries().first()
        val altered = listOf(entry.copy(collectionId = "wrong"), entry.copy(fileId = "wrong"),
            entry.copy(editNumber = 99), entry.copy(sourceFileName = "different.mp4"), entry.copy(sourceSha256 = "0".repeat(64)))
        for (other in altered) {
            assertEquals(MappingEvidence.UNKNOWN, other.evidenceStatus)
            assertEquals("Title unavailable - original episodes unknown", other.title)
            assertEquals("https://example.org/fallback.jpg", other.withPresentation("Dressrosa", "https://example.org/fallback.jpg").thumbnailUrl)
        }
        val presentedThenReused = entry.withPresentation("Dressrosa").copy(fileId = "wrong")
        assertNull(presentedThenReused.withPresentation("Dressrosa").thumbnailUrl)
    }

    @Test
    fun `invalid optional records or broken thumbnails fall back without hiding playable files`() {
        for (json in listOf("bad json", "{}", "{\"entries\":[]}")) assertTrue(EditResearchRegistry.parse(json).isEmpty())
        val data = JSONObject(ResearchDataset.JSON)
        val row = data.getJSONArray("entries").getJSONObject(0)
        row.put("mapping_status", "DIRECTLY_VERIFIED")
        assertEquals(7, EditResearchRegistry.parse(data.toString()).size)
        row.put("mapping_status", "CONFLICTING").put("thumbnail_url", "https://untrusted.example/other.jpg")
        assertNull(EditResearchRegistry.parse(data.toString()).first().thumbnailUrl)
        row.put("accepted_original_episodes", JSONArray(listOf(999)))
        assertEquals(7, EditResearchRegistry.parse(data.toString()).size)
    }

    @Test
    fun `single and disjoint coverage preserve only the stated original numbers`() {
        val base = entries()[1]
        val single = base.copy(originalEpisodes = OriginalEpisodeMapping(7, 7, "https://example.org/synthetic"))
        assertEquals("The Operation Fruit - 7", single.title)
        val disjoint = base.copy(originalEpisodes = OriginalEpisodeMapping(1, 4, "https://example.org/synthetic", listOf(1, 3, 4)))
        assertEquals("The Operation Fruit - 1, 3-4", disjoint.title)
        assertEquals("694, 696-698", originalEpisodeLabel(listOf(694, 696, 697, 698)))
        assertEquals(base.fileId, disjoint.fileId)
        assertEquals(base.editNumber, disjoint.editNumber)
        assertThrows(IllegalArgumentException::class.java) {
            OriginalEpisodeMapping(1, 4, "https://example.org/synthetic", listOf(1, 4, 3))
        }
    }
}
