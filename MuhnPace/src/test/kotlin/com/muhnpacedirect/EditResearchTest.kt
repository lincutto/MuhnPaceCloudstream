package com.muhnpacedirect

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class EditResearchTest {
    @Test
    fun `all Dressrosa records populate supported episode fields without changing navigation`() {
        val rows = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!
            .bufferedReader().use { JSONObject(it.readText()).getJSONArray("collections") }
        val collection = (0 until rows.length()).map { rows.getJSONObject(it) }
            .single { it.getString("id") == "f8HTXmAk" }
        val episodes = MuhnPaceProvider().episodesFromFiles("f8HTXmAk", "Dressrosa", collection.getJSONArray("files"))
        assertEquals(48, episodes.size)
        assertEquals(48, episodes.map { it.description }.toSet().size)
        assertEquals(48, episodes.map { it.posterUrl }.toSet().size)
        assertEquals("To Russian, with Love - 714-716", episodes[37].name)
        assertEquals("Zoro vs. Pica - 716-719", episodes[38].name)
        assertEquals("The Curse of Heart - 714-715, 720-723", episodes[39].name)
        for ((index, ep) in episodes.withIndex()) {
            assertEquals(index + 1, ep.episode)
            assertEquals(1, ep.season) // Legacy route remains season 1.
            assertTrue(ep.name!!.contains(" - "))
            assertTrue(ep.description!!.length in 35..500)
            assertTrue(ep.posterUrl!!.endsWith("/${ep.data.substringAfterLast('/')}.jpg"))
        }
    }

    @Test
    fun `a disputed or unknown range keeps the story title without exposing research notes`() {
        val base = entries()[1]
        for (status in listOf(MappingEvidence.CONFLICTING, MappingEvidence.UNKNOWN)) {
            val research = base.research!!.copy(mappingStatus = status, acceptedEpisodes = emptyList(),
                candidateEpisodes = if (status == MappingEvidence.UNKNOWN) emptyList() else listOf(699, 700),
                originalTitles = emptyList())
            val changed = base.copy(research = research)
            assertEquals("The Operation Fruit", changed.title)
            assertEquals(research.synopsis, changed.description)
            assertEquals(base.fileId, changed.fileId)
            assertEquals(base.editNumber, changed.editNumber)
        }
    }

    private fun entries(): List<EpisodeMetadata> {
        val rows = javaClass.getResourceAsStream("/catalogue-2026-11-14.json")!!
            .bufferedReader().use { JSONObject(it.readText()).getJSONArray("collections") }
        val dressrosa = (0 until rows.length()).map { rows.getJSONObject(it) }.single { it.getString("id") == "f8HTXmAk" }
        return EpisodeCatalog.select("f8HTXmAk", "Dressrosa", dressrosa.getJSONArray("files"))
            .filter { it.editNumber in 30..37 }
    }

    @Test
    fun `eight recovered stories retain scenes while project-specific evidence resolves derived ranges`() {
        val entries = entries()
        assertEquals(8, entries.size)
        assertEquals(8, entries.count { it.evidenceStatus == MappingEvidence.ONE_PACE_DERIVED })
        assertEquals(0, entries.count { it.evidenceStatus == MappingEvidence.CONFLICTING })
        assertTrue(entries.none { it.originalEpisodes != null })
        assertEquals(8, entries.map { it.research!!.synopsis }.toSet().size)
        assertEquals(8, entries.map { it.withPresentation("Dressrosa").thumbnailUrl }.toSet().size)
        for (entry in entries) {
            expectedEpisodeName(entry.fileId)?.let { assertEquals(it, entry.title) }
            assertFalse(entry.title.contains("Muhn Pace"))
            assertFalse(entry.description.contains("Muhn Pace edit"))
            assertTrue(entry.description.startsWith(entry.research!!.synopsis))
            assertEquals(entry.research!!.synopsis, entry.description)
            assertFalse(entry.description.contains("One Pace-derived"))
        }
    }

    @Test
    fun `Law childhood supports partial scene evidence without promoting the full range to directly verified`() {
        val law = entries().single { it.fileId == "Q5RqeqED" }
        assertEquals(listOf(701), law.research!!.sceneConfirmedEpisodes)
        assertEquals(listOf(701, 702), law.research!!.acceptedEpisodes)
        assertEquals(MappingEvidence.ONE_PACE_DERIVED, law.evidenceStatus)
        assertTrue(law.research!!.originalTitles.any { it.number == 702 })
        assertFalse(law.description.contains("Scene confirmed"))
    }

    @Test
    fun `wrong collection file edition number filename or hash cannot inherit research`() {
        val entry = entries().first()
        val altered = listOf(entry.copy(collectionId = "wrong"), entry.copy(fileId = "wrong"),
            entry.copy(editNumber = 99), entry.copy(sourceFileName = "different.mp4"), entry.copy(sourceSha256 = "0".repeat(64)))
        for (other in altered) {
            assertEquals(MappingEvidence.UNKNOWN, other.evidenceStatus)
            assertEquals("One Piece", other.title)
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
        assertEquals(EditResearchRegistry.entries.size - 1, EditResearchRegistry.parse(data.toString()).size)
        row.put("mapping_status", "CONFLICTING").put("accepted_original_episodes", JSONArray()).put("thumbnail_url", "https://untrusted.example/other.jpg")
        assertNull(EditResearchRegistry.parse(data.toString()).first().thumbnailUrl)
        row.put("accepted_original_episodes", JSONArray(listOf(999)))
        assertEquals(EditResearchRegistry.entries.size - 1, EditResearchRegistry.parse(data.toString()).size)
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
