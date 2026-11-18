package com.muhnpaceeggheaddev

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
    internal val seriesUrl get() = "$mainUrl/muhnpace-egghead-dev/one-piece"
    private val expansionByUrl = catalogue.availableEpisodes.associateBy { "$seriesUrl/${it.key}" }

    private fun card() = newTvSeriesSearchResponse(SeriesCatalogue.TITLE, seriesUrl, TvType.TvSeries) {
        posterUrl = SeriesCatalogue.poster
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse =
        newHomePageResponse(request.name, if (page == 1) listOf(card()) else emptyList(), hasNext = false)

    private val arcNames = SeriesCatalogue.arcs.filter { it.listId != WANO_ACT_3 }.associate { arc ->
        arc.season to when (arc.listId) {
            WANO_ACTS_1_2 -> "Wano Arc"
            "jVhFqouN" -> "Post-Enies Lobby Arc"
            "MH7AQkgr" -> "Post-War Arc"
            "ZQhstabS" -> "Fish-Man Island Arc"
            "kuoQYqfR" -> "Fish-Man Island Arc • Alternate scene"
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
        if (url != seriesUrl) return null
        val series = baseline.load(baseline.seriesUrl) as? TvSeriesLoadResponse ?: return null
        return developmentSeries(series)
    }

    internal fun developmentSeries(series: TvSeriesLoadResponse): TvSeriesLoadResponse = series.apply {
        url = seriesUrl
        uniqueUrl = seriesUrl
        apiName = this@EggheadDevProvider.name
        plot = "${SeriesCatalogue.DESCRIPTION} Egghead features One Pace, Kuma Cut and Shaved Egghead edits."
        episodes = episodes.mapNotNull { legacy ->
            val edit = legacyEdits[legacy.data.substringAfterLast('/')] ?: return@mapNotNull null
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

    private fun legacyTitle(title: String, edit: EditResearch): String {
        val accepted = edit.acceptedEpisodes
        // The v7 builder attaches this suffix only after matching the exact source record.
        if (accepted.isNotEmpty() && title == "${edit.displayTitle} - ${originalEpisodeLabel(accepted)}") {
            return titleWithCoverage(edit.displayTitle, accepted)
        }
        // Act 3 has provisional reference numbers embedded in its editorial titles.
        val story = if (edit.collectionId == WANO_ACT_3) title.replace(PROVISIONAL_SUFFIX, "") else title
        return "$story • Anime episodes unverified"
    }

    private companion object {
        const val WANO_ACTS_1_2 = "QZw3Ejpy"
        const val WANO_ACT_3 = "sF9stHJo"
        const val WANO_SEASON = 15
        // The reviewed first collection owns slots 1–26, even when a file is temporarily missing.
        const val WANO_ACT_3_OFFSET = 26
        val PROVISIONAL_SUFFIX = Regex(""" - [0-9]+(?:-[0-9]+)?$""")
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
