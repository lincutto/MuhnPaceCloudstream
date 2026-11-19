package com.muhnpaceeggheaddev

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.muhnpaceeggheaddev.baseline.EditResearch
import com.muhnpaceeggheaddev.baseline.EditResearchRegistry
import com.muhnpaceeggheaddev.baseline.MuhnPaceProvider
import com.muhnpaceeggheaddev.baseline.SeriesCatalogue
import com.muhnpaceeggheaddev.baseline.originalEpisodeLabel

internal class EggheadDevProvider(
    private val catalogue: ExpansionCatalogue = ExpansionCatalogue(),
    private val adapter: PixeldrainDubAdapter = PixeldrainDubAdapter(),
    private val loadBaseline: suspend (MuhnPaceProvider) -> TvSeriesLoadResponse? =
        { it.load(it.seriesUrl) as? TvSeriesLoadResponse },
) : MainAPI() {
    override var name = "Muhn Pace Rework • Egghead Dev (by lincutto)"
    override var mainUrl = "https://pixeldrain.dev"
    override var lang = "en"
    override val supportedTypes = setOf(TvType.TvSeries)
    override val hasMainPage = true
    override val mainPage = mainPageOf("muhn-egghead-dev" to "One Piece • Muhn Pace (English dub)")

    private val baseline = MuhnPaceProvider().also { it.name = name }
    private val legacyEdits by lazy { EditResearchRegistry.entries.associateBy { it.fileId } }
    private val baselineIds by lazy { legacyEdits.keys }
    internal val previousSeriesUrl get() = "$mainUrl/muhnpace-egghead-dev/one-piece"
    internal val seriesUrl get() = "$mainUrl/muhnpace-egghead-dev/one-piece-anime-numbers"
    // The series URL identifies the history namespace. Episode data still selects the same cut.
    private val expansionByUrl = catalogue.availableEpisodes.associateBy { "$previousSeriesUrl/${it.key}" }

    private fun card() = newTvSeriesSearchResponse(SeriesCatalogue.TITLE, seriesUrl, TvType.TvSeries) {
        posterUrl = SeriesCatalogue.poster
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse =
        newHomePageResponse(request.name, if (page == 1) listOf(card()) else emptyList(), hasNext = false)

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
        return if (matchesArc || baseline.search(query).isNotEmpty()) listOf(card()) else emptyList()
    }

    override suspend fun load(url: String): TvSeriesLoadResponse? {
        if (url != seriesUrl && url != previousSeriesUrl) return null
        return loadBaseline(baseline)?.let { developmentSeries(it, url) }
    }

    internal fun developmentSeries(series: TvSeriesLoadResponse, pageUrl: String = seriesUrl): TvSeriesLoadResponse = series.apply {
        val useAnimeNumbers = pageUrl == seriesUrl
        url = pageUrl
        uniqueUrl = pageUrl
        apiName = this@EggheadDevProvider.name
        plot = "${SeriesCatalogue.DESCRIPTION} Egghead features One Pace, Kuma Cut and Shaved Egghead edits."
        episodes = episodes.mapNotNull { legacy ->
            val fileId = legacy.data.substringAfterLast('/')
            // Only this reviewed second copy of Fish-Man edit 15 is hidden.
            if (fileId == FISHMAN_DUPLICATE) return@mapNotNull null
            val edit = legacyEdits[fileId]
            if (edit == null) {
                // V3 admits reviewed files only. Surface source drift without attaching another edit's facts.
                Log.w("EggheadDev", "Unreviewed Pixeldrain file $fileId at S${legacy.season} E${legacy.episode}")
                return@mapNotNull null
            }
            legacy.apply {
                if (edit.collectionId == WANO_ACT_3) {
                    season = WANO_SEASON
                    episode = WANO_ACT_3_OFFSET + requireNotNull(episode)
                }
                if (useAnimeNumbers) {
                    val coverage = edit.acceptedEpisodes.ifEmpty {
                        reviewedWanoCoverage[edit.fileId to edit.sourceSha256].orEmpty()
                    }
                    episode = originalNumber(edit.fileId, coverage) ?: episode
                }
                name = legacyTitle(name.orEmpty(), edit, episode)
            }
        } + expansionByUrl.map { (route, story) ->
            val release = reviewedEggheadReleases[story.key]
            newEpisode(route) {
                season = story.season
                episode = if (useAnimeNumbers) originalNumber(story.key, story.originalEpisodes) else story.number
                name = titleWithCoverage(story.title, story.originalEpisodes, episode)
                description = release?.synopsis
                posterUrl = release?.thumbnailUrl
            }
        }
        seasonNames = arcNames.map { (season, title) -> SeasonData(season, title, displaySeason = null) }
    }

    internal fun titleWithCoverage(title: String, numbers: List<Int>, nativeNumber: Int?): String {
        val single = numbers.distinct().singleOrNull()
        if (single != null) {
            // Cloudstream already prints the native number. Old saved routes can use a different one.
            return if (single == nativeNumber) title else "Episode $single - $title"
        }
        return "${originalEpisodeLabel(numbers).replace('-', '–')} - $title"
    }

    private fun originalNumber(key: String, coverage: List<Int>): Int? =
        EARLIER_ORIGINALS[key]?.takeIf { it in coverage } ?: coverage.lastOrNull()

    internal fun legacyTitle(title: String, edit: EditResearch, nativeNumber: Int?): String {
        val accepted = edit.acceptedEpisodes
        if (accepted.isNotEmpty()) {
            // V7 emits coverage only after its filename, edit number and hash checks.
            val suffix = " - ${originalEpisodeLabel(accepted)}"
            return if (title.endsWith(suffix)) titleWithCoverage(title.removeSuffix(suffix), accepted, nativeNumber) else title
        }
        if (edit.collectionId != WANO_ACT_3 || title != edit.displayTitle) return title
        // V7 matched the selected source. Discard Phase 4B's provisional label even if it looks valid.
        val storyTitle = title.replace(PROVISIONAL_SUFFIX, "")
        val numbers = reviewedWanoCoverage[edit.fileId to edit.sourceSha256] ?: return storyTitle
        return titleWithCoverage(storyTitle, numbers, nativeNumber)
    }

    private companion object {
        // Cloudstream sorts and deduplicates by the same episode number it shows. These
        // are the latest covered originals that fit before the next edit in each arc.
        // Key by release identity so a missing neighbour cannot change anyone's resume ID.
        val EARLIER_ORIGINALS = mapOf(
            "fn7rEMkN" to 423, // The Underwater Prison, Impel Down
            "uAu7YXhH" to 438, // Friend
            "jmda4ZHb" to 539, // At the Sea Forest! Arlong and Hody
            "dP3DaScp" to 566, // Phantom
            "vSkNFYho" to 710, // Bellamy the Pirate
            "26bBWNGx" to 803, // The Iron Mask
            "iUiKz18s" to 855, // Wave Room
            "3VFYgpqk" to 919, // Void
            "17/1" to 1087,   // New Emperors
        )
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
            if (data.substringAfterLast('/') !in baselineIds) return false
            return baseline.loadLinks(data, isCasting, subtitleCallback, callback)
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
