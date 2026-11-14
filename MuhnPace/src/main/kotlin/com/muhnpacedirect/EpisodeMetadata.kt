package com.muhnpacedirect

import org.json.JSONArray
import org.json.JSONObject

/** A verified anime-episode range, never a manga chapter range or an inferred offset. */
internal data class OriginalEpisodeMapping(
    val first: Int,
    val last: Int,
    val sourceUrl: String,
) {
    init {
        require(first > 0 && last >= first)
        require(sourceUrl.startsWith("https://") && sourceUrl.length > 8)
    }

    val label: String get() = if (first == last) "$first" else "$first–$last"
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
    val title: String,
    val edition: EpisodeEdition,
    val originalEpisodes: OriginalEpisodeMapping? = null,
    val thumbnailUrl: String? = null,
    val summary: String? = null,
) {
    val description: String
        get() = listOfNotNull(summary, originalEpisodes?.let {
            "Original One Piece episodes: ${it.label}. Source: ${it.sourceUrl}"
        } ?: "Original One Piece episode mapping is unverified.").joinToString("\n")

    /** Describe only facts known about this edit; never borrow another episode's synopsis. */
    fun withPresentation(arcTitle: String, fallbackPosterUrl: String? = null): EpisodeMetadata {
        val arcName = arcTitle.substringBefore(" • Episode ")
        val editionNote = if (edition.label.isEmpty()) "" else " ${edition.label}."
        return copy(
            summary = "$arcName • Muhn Pace edit $editNumber. English dub.$editionNote",
            thumbnailUrl = thumbnailUrl ?: fallbackPosterUrl,
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

    // No verified file-to-anime mappings survived recovery. Keep unknowns null.
    // Add entries only with evidence for the exact file/edit; see docs/V3_HANDOVER.md.
    private val originalMappings: Map<String, OriginalEpisodeMapping> = emptyMap()

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
        val displayArc = arcTitle.substringBefore(" • Episode ")
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
        val suffix = if (edition.label.isEmpty()) "" else " (${edition.label})"
        return EpisodeMetadata(
            collectionId = collectionId,
            fileId = fileId,
            sourceFileName = fileName,
            editNumber = number,
            title = "$displayArc • Episode $number$suffix",
            edition = edition,
            originalEpisodes = originalMappings[fileId],
            // Only use a thumbnail advertised for this exact file. Missing/foreign links
            // fall back to the arc artwork in the presentation layer.
            thumbnailUrl = file.optString("thumbnail_href")
                .takeIf { it == "/file/$fileId/thumbnail" }
                ?.let { "https://pixeldrain.dev/api$it?width=256&height=144" },
        )
    }

    private fun isVideo(file: JSONObject, name: String): Boolean =
        file.optString("mime_type").startsWith("video/", ignoreCase = true) ||
            name.substringAfterLast('.', "").lowercase() in videoExtensions
}
