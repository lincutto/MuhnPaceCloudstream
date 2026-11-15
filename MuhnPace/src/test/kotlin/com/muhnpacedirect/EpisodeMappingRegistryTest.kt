package com.muhnpacedirect

import org.junit.Assert.*
import org.junit.Test

class EpisodeMappingRegistryTest {
    // Synthetic evidence only; this is never inserted into the production registry.
    private val evidence = VerifiedEditMapping(
        collectionId = "testCollection", fileId = "testFile", editNumber = 25,
        sourceFileName = "[Muhn Pace] Dressrosa 25.mp4",
        originalEpisodes = OriginalEpisodeMapping(1, 3, "https://example.org/synthetic-evidence"),
        verifiedOn = "2026-11-15", qualification = "Synthetic partial-episode coverage for testing only.",
    )

    private fun EpisodeMappingRegistry.lookup(e: VerifiedEditMapping = evidence) =
        find(e.collectionId, e.fileId, e.editNumber, e.sourceFileName)

    @Test
    fun `evidence applies only to the exact collection file edit and source filename`() {
        val registry = EpisodeMappingRegistry(listOf(evidence))
        assertEquals(evidence.originalEpisodes, registry.lookup())
        assertNull(registry.lookup(evidence.copy(collectionId = "otherCollection")))
        assertNull(registry.lookup(evidence.copy(fileId = "otherFile")))
        assertNull(registry.lookup(evidence.copy(editNumber = 26)))
        assertNull(registry.lookup(evidence.copy(sourceFileName = "[Muhn Pace] Dressrosa 25 Alt.mp4")))
    }

    @Test
    fun `missing or conflicting evidence stays unknown`() {
        assertNull(EpisodeMappingRegistry(emptyList()).lookup())
        val conflicting = evidence.copy(originalEpisodes = OriginalEpisodeMapping(2, 4, "https://example.org/other"))
        assertNull(EpisodeMappingRegistry(listOf(evidence, conflicting)).lookup())
    }

    @Test
    fun `evidence requires identities verification date source and qualification`() {
        assertThrows(IllegalArgumentException::class.java) { evidence.copy(collectionId = "") }
        assertThrows(IllegalArgumentException::class.java) { evidence.copy(fileId = "") }
        assertThrows(IllegalArgumentException::class.java) { evidence.copy(editNumber = 0) }
        assertThrows(IllegalArgumentException::class.java) { evidence.copy(sourceFileName = "") }
        assertThrows(IllegalArgumentException::class.java) { evidence.copy(qualification = " ") }
        for (date in listOf("", "15/11/2026", "2026-02-30", "2026-13-01")) {
            assertThrows(IllegalArgumentException::class.java) { evidence.copy(verifiedOn = date) }
        }
        assertThrows(IllegalArgumentException::class.java) { OriginalEpisodeMapping(1, 3, "") }
    }
}
