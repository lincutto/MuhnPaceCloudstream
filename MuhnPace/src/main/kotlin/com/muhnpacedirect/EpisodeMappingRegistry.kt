package com.muhnpacedirect

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/** Human-reviewed exact-edit evidence, not a table borrowed from another fan edit. */
internal data class VerifiedEditMapping(
    val collectionId: String,
    val fileId: String,
    val editNumber: Int,
    val sourceFileName: String,
    val originalEpisodes: OriginalEpisodeMapping,
    val verifiedOn: String,
    val qualification: String,
) {
    init {
        require(collectionId.isNotBlank() && fileId.isNotBlank() && sourceFileName.isNotBlank())
        require(editNumber > 0)
        require(qualification.isNotBlank()) // Record partial coverage/uncertainty, or explicitly none.
        require(Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}").matches(verifiedOn))
        val position = ParsePosition(0)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("UTC")
        }
        require(dateFormat.parse(verifiedOn, position) != null && position.index == verifiedOn.length)
    }
}

/** No inference, offsets or network lookups. Missing, changed or conflicting evidence stays unknown. */
internal class EpisodeMappingRegistry(entries: List<VerifiedEditMapping>) {
    private val entries = entries.toList()

    fun find(collectionId: String, fileId: String, editNumber: Int, sourceFileName: String): OriginalEpisodeMapping? =
        entries.singleOrNull {
            it.collectionId == collectionId && it.fileId == fileId &&
                it.editNumber == editNumber && it.sourceFileName == sourceFileName
        }?.originalEpisodes
}
