package com.muhnpacedirect

import org.json.JSONArray
import org.json.JSONObject

/** A verified anime-episode range, never a manga chapter range or an inferred offset. */
internal data class OriginalEpisodeMapping(
    val first: Int,
    val last: Int,
    val sourceUrl: String,
    val includedEpisodes: List<Int> = (first..last).toList(),
) {
    init {
        require(first > 0 && last >= first)
        require(sourceUrl.startsWith("https://") && sourceUrl.length > 8)
        require(includedEpisodes.isNotEmpty() && includedEpisodes == includedEpisodes.distinct().sorted())
        require(includedEpisodes.first() == first && includedEpisodes.last() == last)
    }

    val label: String get() = originalEpisodeLabel(includedEpisodes)
}

internal enum class EpisodeEdition(val label: String) {
    REGULAR(""), ALTERNATE("Alternate edit"), FILLER("Filler edit"), FIXED_GROUP_SHOT("Fixed group shot");

    val isVariant: Boolean get() = this == ALTERNATE || this == FILLER
}

/** Display metadata is separate from the file ID used for playback and watch history. */
internal data class EpisodeMetadata(
    val collectionId: String,
    val fileId: String,
    val sourceFileName: String,
    val editNumber: Int,
    val arcTitle: String,
    val edition: EpisodeEdition,
    val originalEpisodes: OriginalEpisodeMapping? = null,
    val thumbnailUrl: String? = null,
    val summary: String? = null,
    val sourceSha256: String? = null,
    val research: EditResearch? = null,
) {
    private val matchedResearch: EditResearch? get() = research?.takeIf { it.matches(this) }
    val evidenceStatus: MappingEvidence get() = if (originalEpisodes != null) MappingEvidence.DIRECTLY_VERIFIED
        else matchedResearch?.mappingStatus ?: MappingEvidence.UNKNOWN
    /** Provenance stays internal; only accepted coverage supplies viewer-facing numbers. */
    val title: String
        get() {
            val storyTitle = matchedResearch?.displayTitle ?: "One Piece"
            val coverage = originalEpisodes?.label ?: matchedResearch?.let {
                when (it.mappingStatus) {
                    MappingEvidence.ONE_PACE_DERIVED -> originalEpisodeLabel(it.acceptedEpisodes)
                    else -> null
                }
            }
            // The host supplies the navigation index. Never repeat internal edit numbers here.
            return coverage?.let { "$storyTitle - $it" } ?: storyTitle
        }

    val description: String
        get() = matchedResearch?.synopsis.orEmpty()

    /** Describe only facts known about this edit; never borrow another episode's synopsis. */
    fun withPresentation(arcTitle: String, fallbackPosterUrl: String? = null): EpisodeMetadata {
        return copy(
            summary = matchedResearch?.synopsis,
            thumbnailUrl = matchedResearch?.thumbnailUrl ?: thumbnailUrl?.takeUnless {
                fileId in knownBlankThumbnailIds || EditResearchRegistry.rejectSourceThumbnail(fileId) ||
                    it.startsWith(ResearchDataset.ARTWORK_BASE) ||
                    it.contains("/assets/phase4b/episodes/")
            } ?: fallbackPosterUrl,
        )
    }

    companion object {
        // Decoded previews verified 2026-11-14: every RGB channel is 0 or 1.
        // Use arc artwork for these frames without extra image probes while browsing.
        // Evidence and hashes: thumbnail-verification-2026-11-14.json.
        private val knownBlankThumbnailIds = setOf(
            "67SvndHA", "CcKSPHJZ", "QG7THm8t", "U8HMFkPE", "zEmPxD6p", // Enies Lobby 11–15
            "fn7rEMkN", // Impel Down 1
            "qogFLkFu", "r8HSo1Gp", // Post Marineford 7–8
        )
    }
}

/** Pure catalogue selection shared by the provider and the offline JVM tests. */
internal object EpisodeCatalog {
    private val fileIdPattern = Regex("^[A-Za-z0-9_-]{1,64}$")
    private val muhnTag = Regex("^\\[muhn\\s*pace\\]", RegexOption.IGNORE_CASE)
    private val bracketTag = Regex("\\[[^\\]]*\\]")
    private val whitespace = Regex("\\s+")
    private val videoExtensions = setOf("mp4", "mkv", "m4v", "webm", "mov", "avi")

    // Phase 3 research found no verified exact-file mappings. Keep unknowns null.
    // Never import One Pace tables or trust arbitrary remote description/filename numbers.
    private val originalMappings = EpisodeMappingRegistry(emptyList())

    fun select(collectionId: String, arcTitle: String, files: JSONArray): List<EpisodeMetadata> =
        (0 until files.length()).mapNotNull { index ->
            files.optJSONObject(index)?.let { parse(collectionId, arcTitle, it) }
        }.groupBy { it.editNumber }.map { (_, edits) ->
            // Preserve the old tie-break: first regular edit, otherwise first listed variant.
            edits.firstOrNull { !it.edition.isVariant } ?: edits.first()
        }.sortedBy { it.editNumber }

    private fun parse(collectionId: String, arcTitle: String, file: JSONObject): EpisodeMetadata? {
        val fileId = file.optString("id")
        val fileName = file.optString("name")
        if (!fileIdPattern.matches(fileId) || !isVideo(file, fileName)) return null
        if (fileName.contains("[SUB]", ignoreCase = true) ||
            !(muhnTag.containsMatchIn(fileName) || fileName.contains("[DUB]", ignoreCase = true))
        ) return null

        val extension = fileName.substringAfterLast('.', "").lowercase()
        val stem = if (extension in videoExtensions) fileName.substringBeforeLast('.') else fileName
        val clean = bracketTag.replace(stem, " ").replace(whitespace, " ").trim()
        val sourceArc = when (val title = arcTitle.substringBefore(" •")) {
            "Post Marineford" -> "Post War"
            "Thriller Bark" -> "Thrillerbark"
            else -> title
        }
        // Match the edit number after the known arc, not a version, resolution or chapter tag.
        val pattern = Regex(
            "^${Regex.escape(sourceArc)}\\s+(?:-\\s+)?([0-9]{1,3})(?:\\s+(.*))?$",
            RegexOption.IGNORE_CASE,
        )
        val match = pattern.matchEntire(clean) ?: return null
        val number = match.groupValues[1].toIntOrNull()?.takeIf { it > 0 } ?: return null
        val edition = when (match.groupValues[2].trim().removePrefix("-").trim().lowercase()) {
            "" -> EpisodeEdition.REGULAR
            "alt" -> EpisodeEdition.ALTERNATE
            "filler", "fillerver" -> EpisodeEdition.FILLER
            "(fixed group shot)" -> EpisodeEdition.FIXED_GROUP_SHOT
            else -> return null // Do not turn comparisons or unknown extras into episodes.
        }
        val metadata = EpisodeMetadata(
            collectionId = collectionId,
            fileId = fileId,
            sourceFileName = fileName,
            editNumber = number,
            arcTitle = arcTitle,
            edition = edition,
            sourceSha256 = file.optString("hash_sha256").takeIf { it.isNotBlank() },
            originalEpisodes = originalMappings.find(collectionId, fileId, number, fileName),
            // Only use a thumbnail advertised for this exact file. Missing/foreign links
            // fall back to the arc artwork in the presentation layer.
            thumbnailUrl = file.optString("thumbnail_href")
                .takeIf { it == "/file/$fileId/thumbnail" }
                ?.let { "https://pixeldrain.dev/api$it?width=256&height=144" },
        )
        return metadata.copy(research = EditResearchRegistry.find(metadata))
    }

    private fun isVideo(file: JSONObject, name: String): Boolean =
        file.optString("mime_type").startsWith("video/", ignoreCase = true) ||
            name.substringAfterLast('.', "").lowercase() in videoExtensions
}
