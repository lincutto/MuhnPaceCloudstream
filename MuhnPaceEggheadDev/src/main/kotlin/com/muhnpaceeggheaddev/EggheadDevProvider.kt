package com.muhnpaceeggheaddev

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
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
    private val baselineIds by lazy { EditResearchRegistry.entries.map { it.fileId }.toSet() }
    internal val seriesUrl get() = "$mainUrl/muhnpace-egghead-dev/one-piece"
    private val expansionByUrl = catalogue.availableEpisodes.associateBy { "$seriesUrl/${it.key}" }

    private fun card() = newTvSeriesSearchResponse(SeriesCatalogue.TITLE, seriesUrl, TvType.TvSeries) {
        posterUrl = SeriesCatalogue.poster
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse =
        newHomePageResponse(request.name, if (page == 1) listOf(card()) else emptyList(), hasNext = false)

    override suspend fun search(query: String): List<SearchResponse> {
        val availableSeason = catalogue.availableEpisodes.any {
            ExpansionMetadata.seasonNames.getValue(it.season).equals(query.trim(), ignoreCase = true)
        }
        return if (availableSeason || baseline.search(query).isNotEmpty()) listOf(card()) else emptyList()
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
        episodes += expansionByUrl.map { (route, story) ->
            newEpisode(route) {
                season = story.season
                episode = story.number
                name = story.title
                description = "Original anime: ${originalEpisodeLabel(story.originalEpisodes)}"
            }
        }
        seasonNames = seasonNames.orEmpty() + catalogue.availableEpisodes.map { it.season }.distinct().map {
            SeasonData(it, ExpansionMetadata.seasonNames.getValue(it), it)
        }
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
