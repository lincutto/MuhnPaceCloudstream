package com.muhnpacedirect

import org.json.JSONArray
import org.json.JSONObject

internal enum class MappingEvidence { DIRECTLY_VERIFIED, ONE_PACE_DERIVED, CONFLICTING, UNKNOWN }

/** Preserve disjoint episode numbers; never fill a gap to make a convenient range. */
internal fun originalEpisodeLabel(numbers: List<Int>): String {
    require(numbers.isNotEmpty() && numbers.all { it > 0 })
    val sorted = numbers.distinct().sorted()
    val groups = mutableListOf<String>()
    var first = sorted.first()
    var last = first
    for (number in sorted.drop(1)) {
        if (number == last + 1) last = number else {
            groups += if (first == last) "$first" else "$first-$last"
            first = number; last = number
        }
    }

    groups += if (first == last) "$first" else "$first-$last"
    return groups.joinToString(", ")
}

internal data class AnimeEpisodeTitle(val number: Int, val title: String, val sourceUrl: String)

/** Weaker evidence stays separate from the strict Phase 3 verified registry. */
internal data class EditResearch(
    val collectionId: String,
    val fileId: String,
    val sourceFileName: String,
    val sourceSha256: String,
    val editNumber: Int,
    val displayTitle: String,
    val synopsis: String,
    val mappingStatus: MappingEvidence,
    val candidateEpisodes: List<Int>,
    val acceptedEpisodes: List<Int>,
    val sceneConfirmedEpisodes: List<Int>,
    val originalTitles: List<AnimeEpisodeTitle>,
    val thumbnailUrl: String?,
    val titleSourceUrl: String,
) {
    init {
        require(collectionId.isNotBlank() && fileId.isNotBlank() && sourceFileName.isNotBlank())
        require(Regex("[a-f0-9]{64}").matches(sourceSha256))
        require(editNumber > 0 && displayTitle.isNotBlank() && synopsis.isNotBlank())
        // Candidates cannot promote themselves to VERIFIED. Only EpisodeMappingRegistry admits direct verification.
        require(mappingStatus != MappingEvidence.DIRECTLY_VERIFIED)
        require(candidateEpisodes.all { it > 0 } && candidateEpisodes.distinct().sorted() == candidateEpisodes)
        require(sceneConfirmedEpisodes.all { it in candidateEpisodes })
        require((mappingStatus == MappingEvidence.UNKNOWN) == candidateEpisodes.isEmpty())
        require(acceptedEpisodes == if (mappingStatus == MappingEvidence.ONE_PACE_DERIVED) candidateEpisodes else emptyList<Int>())
        require(titleSourceUrl.startsWith("https://"))
        require(originalTitles.all { it.number in candidateEpisodes && it.title.isNotBlank() && it.sourceUrl.startsWith("https://") })
    }

    fun matches(metadata: EpisodeMetadata): Boolean = collectionId == metadata.collectionId &&
        fileId == metadata.fileId && editNumber == metadata.editNumber && sourceFileName == metadata.sourceFileName &&
        (metadata.sourceSha256 == null || sourceSha256 == metadata.sourceSha256)

    fun description(verified: OriginalEpisodeMapping?): String {
        val coverage = if (verified != null) {
            "Verified original coverage: OP ${verified.label}. Fan edits may use only portions of those episodes."
        } else {
            val scene = if (sceneConfirmedEpisodes.isEmpty()) "" else
                "Scene confirmed: OP ${originalEpisodeLabel(sceneConfirmedEpisodes)} (partial). "
            scene + when (mappingStatus) {
                MappingEvidence.ONE_PACE_DERIVED -> "Original anime coverage: ${originalEpisodeLabel(acceptedEpisodes)} (One Pace-derived; the English adaptation may cut or rearrange portions)."
                MappingEvidence.CONFLICTING -> "Original episode range unresolved: published references disagree."
                else -> "Original episode coverage is unknown."
            }
        }
        val includeTitles = verified?.includedEpisodes ?: acceptedEpisodes
        val titles = originalTitles.filter { it.number in includeTitles }
            .joinToString("\n") { "OP ${it.number}: ${it.title}" }.takeIf { it.isNotBlank() }
        return listOfNotNull(synopsis, coverage, titles).joinToString("\n\n")
    }
}

internal object EditResearchRegistry {
    private fun JSONArray.numbers() = (0 until length()).map { getInt(it) }

    // JSON is generated and bundled as a string: no remote metadata requests at browse time.
    internal val entries: List<EditResearch> by lazy { parse(ResearchDataset.JSON) }

    internal fun parse(serialized: String): List<EditResearch> {
        val data = runCatching { JSONObject(serialized) }.getOrNull() ?: return emptyList()
        val titles = data.optJSONArray("original_titles") ?: return emptyList()
        val referenceTitles = (0 until titles.length()).mapNotNull { index ->
            runCatching {
                val title = titles.getJSONObject(index)
                AnimeEpisodeTitle(title.getInt("number"), title.getString("title"), title.getString("source_url"))
            }.getOrNull()
        }
        val rows = data.optJSONArray("entries") ?: return emptyList()
        return (0 until rows.length()).mapNotNull { index ->
            // An invalid optional record must not make a playable collection disappear.
            runCatching {
                val row = rows.getJSONObject(index)
                val candidates = row.getJSONArray("candidate_original_episodes").numbers()
                val thumb = row.optString("thumbnail_url").takeIf {
                    it == ResearchDataset.ARTWORK_BASE + "/dressrosa/edit-${row.getInt("edit_number")}.jpg"
                }
                EditResearch(row.getString("collection_id"), row.getString("file_id"),
                    row.getString("source_filename"), row.getString("source_sha256"), row.getInt("edit_number"),
                    row.getString("display_title"), row.getString("synopsis"),
                    MappingEvidence.valueOf(row.getString("mapping_status")), candidates,
                    row.getJSONArray("accepted_original_episodes").numbers(),
                    row.getJSONArray("scene_confirmed_episodes").numbers(),
                    referenceTitles.filter { it.number in candidates }, thumb, row.getString("title_source_url"))
            }.getOrNull()
        }
    }

    fun find(metadata: EpisodeMetadata): EditResearch? = entries.singleOrNull { it.matches(metadata) }

    fun rejectSourceThumbnail(fileId: String): Boolean = entries.any { it.fileId == fileId }
}
