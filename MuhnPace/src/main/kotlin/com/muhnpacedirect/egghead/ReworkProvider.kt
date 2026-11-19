package com.muhnpacedirect.egghead

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.muhnpacedirect.EditResearch
import com.muhnpacedirect.EditResearchRegistry
import com.muhnpacedirect.MuhnPaceProvider
import com.muhnpacedirect.SeriesCatalogue
import com.muhnpacedirect.originalEpisodeLabel
import org.json.JSONArray

internal class ReworkProvider(
    private val catalogue: ExpansionCatalogue = ExpansionCatalogue(),
    private val adapter: PixeldrainDubAdapter = PixeldrainDubAdapter(),
) : MuhnPaceProvider() {
    private val legacyEdits by lazy { EditResearchRegistry.entries.associateBy { it.fileId } }
    private val expansionByUrl = catalogue.availableEpisodes.associateBy { "$seriesUrl/${it.key}" }

    private val arcNames = SeriesCatalogue.arcs.filterNot {
        it.listId == WANO_ACT_3 || it.listId == FISHMAN_ALTERNATE
    }.associate { arc ->
        arc.season to when (arc.listId) {
            WANO_ACTS_1_2 -> "Wano Arc"
            "jVhFqouN" -> "Post-Enies Lobby Arc"
            "MH7AQkgr" -> "Post-War Arc"
            "ZQhstabS" -> "Fish-Man Island Arc"
            else -> "${arc.seasonTitle} Arc"
        }
    } + catalogue.availableEpisodes.map { it.season }.distinct().associateWith {
        ExpansionMetadata.seasonNames.getValue(it)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val term = query.trim()
        val matchesArc = term.isNotEmpty() && arcNames.values.any { it.contains(term, ignoreCase = true) }
        return if (matchesArc) listOf(seriesSearchResponse()) else super.search(query)
    }

    override suspend fun seriesFromFiles(
        url: String,
        filesByCollection: Map<String, JSONArray>,
    ): TvSeriesLoadResponse? = super.seriesFromFiles(url, filesByCollection)?.apply {
        plot = "${SeriesCatalogue.DESCRIPTION} Egghead features One Pace, Kuma Cut and Shaved Egghead edits."
        episodes = episodes.mapNotNull { legacy ->
            val fileId = legacy.data.substringAfterLast('/')
            // Only this reviewed second copy of Fish-Man edit 15 is hidden.
            if (fileId == FISHMAN_DUPLICATE) return@mapNotNull null
            val edit = legacyEdits[fileId]
            if (edit == null) {
                // An unreviewed file has no approved catalogue or playback identity.
                Log.w("MuhnPace", "Unreviewed Pixeldrain file $fileId at S${legacy.season} E${legacy.episode}")
                return@mapNotNull null
            }
            legacy.apply {
                name = legacyTitle(name.orEmpty(), edit)
                if (edit.collectionId == WANO_ACT_3) {
                    season = WANO_SEASON
                    episode = WANO_ACT_3_OFFSET + requireNotNull(episode)
                }
            }
        } + expansionByUrl.map { (route, story) ->
            val release = reviewedEggheadReleases[story.key]
            newEpisode(route) {
                season = story.season
                episode = story.number
                name = titleWithCoverage(story.title, story.originalEpisodes)
                description = release?.synopsis
                posterUrl = release?.thumbnailUrl
            }
        }
        seasonNames = arcNames.map { (season, title) -> SeasonData(season, title, displaySeason = null) }
    }

    private fun titleWithCoverage(title: String, numbers: List<Int>): String {
        val label = if (numbers.size == 1) "Episode" else "Episodes"
        return "$title • $label ${originalEpisodeLabel(numbers).replace('-', '–')}"
    }

    internal fun legacyTitle(title: String, edit: EditResearch): String {
        val accepted = edit.acceptedEpisodes
        if (accepted.isNotEmpty()) {
            // V7 emits coverage only after its filename, edit number and hash checks.
            val suffix = " - ${originalEpisodeLabel(accepted)}"
            return if (title.endsWith(suffix)) titleWithCoverage(title.removeSuffix(suffix), accepted) else title
        }
        if (edit.collectionId != WANO_ACT_3 || title != edit.displayTitle) return title
        // V7 matched the selected source. Discard Phase 4B's provisional label even if it looks valid.
        val storyTitle = title.replace(PROVISIONAL_SUFFIX, "")
        val numbers = reviewedWanoCoverage[edit.fileId to edit.sourceSha256] ?: return storyTitle
        return titleWithCoverage(storyTitle, numbers)
    }

    private companion object {
        const val FISHMAN_ALTERNATE = "kuoQYqfR"
        const val FISHMAN_DUPLICATE = "DujFEHwH"
        const val WANO_ACTS_1_2 = "QZw3Ejpy"
        const val WANO_ACT_3 = "sF9stHJo"
        const val WANO_SEASON = 15
        // The reviewed first collection owns slots 1–26, even when a file is temporarily missing.
        const val WANO_ACT_3_OFFSET = 26
        val PROVISIONAL_SUFFIX = Regex(""" - [0-9]+(?:[-–][0-9]+)?(?:, [0-9]+(?:[-–][0-9]+)?)*$""")
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val story = expansionByUrl[data]
        if (story == null) {
            // Direct routes belong to v7 episodes. A new file must pass its own source review.
            if (data.substringAfterLast('/') !in legacyEdits) return false
            return super.loadLinks(data, isCasting, subtitleCallback, callback)
        }
        if (isCasting) return false
        for (file in catalogue.choices(story)) {
            val link = adapter.resolve(file, name) ?: continue
            callback(link)
            return true
        }
        return false
    }
}
