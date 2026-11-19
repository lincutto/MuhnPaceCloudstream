package com.muhnpacedirect

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONArray
import org.json.JSONObject

/**
 * An independent, read-only catalogue for the public Muhn Pace English dub lists.
 * No BitTorrent, premium API, login, proxy, or debrid integration.
 *
 * Pixeldrain may block direct playback from third-party applications; the provider
 * does not attempt to evade its access controls, paywalls, or transfer restrictions.
 */
open class MuhnPaceProvider : MainAPI() {
    override var name = "Muhn Pace Rework (by lincutto)"
    override var mainUrl = "https://pixeldrain.dev"
    override var lang = "en"
    override val supportedTypes = setOf(TvType.TvSeries)
    override val hasMainPage = true

    private val arcs = SeriesCatalogue.arcs
    internal val seriesUrl: String get() = "$mainUrl/muhnpace/one-piece"
    private fun isSeriesUrl(url: String) = url == seriesUrl || url == "https://pixeldrain.com/muhnpace/one-piece"
    private fun arcDisplayTitle(arc: CatalogueArc) = "One Piece • ${arc.seasonTitle} (Muhn Pace)"

    override val mainPage = mainPageOf("muhn" to "One Piece • Muhn Pace (English dub)")

    protected fun seriesSearchResponse(): SearchResponse =
        newTvSeriesSearchResponse(SeriesCatalogue.TITLE, seriesUrl, TvType.TvSeries) {
            posterUrl = SeriesCatalogue.poster
        }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse =
        newHomePageResponse(request.name, if (page == 1) listOf(seriesSearchResponse()) else emptyList(), hasNext = false)

    override suspend fun search(query: String): List<SearchResponse> {
        val words = query.lowercase().trim().split(Regex("\\s+")).filter(String::isNotBlank)
        val searchable = "muhn pace one piece english dubbed " + arcs.joinToString(" ") { it.seasonTitle.lowercase() }
        return if (words.isNotEmpty() && words.all { it in searchable }) listOf(seriesSearchResponse()) else emptyList()
    }

    internal fun episodesFromFiles(
        collectionId: String,
        arcTitle: String,
        files: JSONArray,
        fallbackPosterUrl: String? = null,
    ): List<Episode> =
        EpisodeCatalog.select(collectionId, arcTitle, files).map { metadata ->
            episodeFromMetadata(metadata, fallbackPosterUrl)
        }

    internal fun episodeFromMetadata(metadata: EpisodeMetadata, fallbackPosterUrl: String? = null): Episode {
        val presented = metadata.withPresentation(metadata.arcTitle, fallbackPosterUrl)
        val fileId = metadata.fileId
        return newEpisode("$mainUrl/api/file/$fileId") {
            name = metadata.title
            description = presented.description
            posterUrl = presented.thumbnailUrl
            episode = metadata.editNumber
            season = 1
        }
    }

    private fun arcForUrl(url: String): CatalogueArc? = arcs.firstOrNull {
        url == "$mainUrl/l/${it.listId}" || url == "https://pixeldrain.com/l/${it.listId}"
    }

    override suspend fun load(url: String): LoadResponse? {
        if (isSeriesUrl(url)) {
            // All seasons must resolve; never silently publish an incomplete catalogue.
            val files = arcs.chunked(4).flatMap { batch ->
                batch.amap { arc -> arc.listId to fetchFiles(arc) }
            }.toMap()
            return seriesFromFiles(url, files)
        }
        // Legacy routes are retained for previously saved Continue Watching/history entries only.
        val arc = arcForUrl(url) ?: return null
        return loadFromFiles(url, fetchFiles(arc))
    }

    private suspend fun fetchFiles(arc: CatalogueArc): JSONArray {
        val response = app.get("$mainUrl/api/list/${arc.listId}")
        val result = try {
            JSONObject(response.text)
        } catch (_: org.json.JSONException) {
            throw ErrorLoadingException("Pixeldrain returned an unreadable collection (HTTP ${response.code}).")
        }
        if (!result.optBoolean("success", false)) {
            throw ErrorLoadingException(result.optString("message", "Pixeldrain refused the collection request"))
        }
        val files = result.optJSONArray("files")
            ?: throw ErrorLoadingException("Pixeldrain returned no file listing")

        return files
    }

    /** The actual load-response builder, also exercised offline against the preserved catalogue. */
    internal suspend fun loadFromFiles(url: String, files: JSONArray): TvSeriesLoadResponse? {
        val arc = arcForUrl(url) ?: return null
        // Keep source edit numbers (including gaps) and sort numerically within this collection.
        val presentation = ArcPresentations.byCollection.getValue(arc.listId)
        val episodes = episodesFromFiles(arc.listId, arc.title, files, presentation.episodeFallback)
        if (episodes.isEmpty()) throw ErrorLoadingException("Pixeldrain has no matching English-dub episodes in this collection.")

        return newTvSeriesLoadResponse(
            arcDisplayTitle(arc), url, TvType.TvSeries, episodes
        ) {
            posterUrl = presentation.poster
            backgroundPosterUrl = presentation.background
            plot = presentation.description(episodes.size)
            tags = presentation.tags
        }
    }

    /** Same builders and native Episode/SeasonData fields used by live loading and offline regressions. */
    internal open suspend fun seriesFromFiles(url: String, filesByCollection: Map<String, JSONArray>): TvSeriesLoadResponse? {
        if (!isSeriesUrl(url)) return null
        val episodes = arcs.flatMap { arc ->
            val files = filesByCollection[arc.listId]
                ?: throw ErrorLoadingException("Missing ${arc.seasonTitle} collection; please retry.")
            val selected = EpisodeCatalog.select(arc.listId, arc.title, files)
            if (selected.isEmpty()) throw ErrorLoadingException("No playable videos in ${arc.seasonTitle}; please retry.")
            selected.map { metadata ->
                episodeFromMetadata(metadata, ArcPresentations.byCollection.getValue(arc.listId).episodeFallback).apply {
                    season = arc.season
                    // Stable origin avoids renumbering later entries when a source file temporarily disappears.
                    episode = arc.navigationIndex(metadata.editNumber)
                }
            }
        }
        return newTvSeriesLoadResponse(SeriesCatalogue.TITLE, url, TvType.TvSeries, episodes) {
            posterUrl = SeriesCatalogue.poster
            backgroundPosterUrl = SeriesCatalogue.background
            plot = SeriesCatalogue.DESCRIPTION
            tags = listOf("Anime", "Action", "Adventure", "Fantasy", "English dub")
            addSeasonNames(arcs.map { SeasonData(it.season, it.seasonTitle, it.season) })
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        // Prevent unexpectedly accepting an arbitrary remote URL as a stream.
        if (!DIRECT_FILE_URL.matches(data)) return false
        val url = "$mainUrl/api/file/${data.substringAfterLast('/')}"

        callback(newExtractorLink(name, "Pixeldrain • Direct HTTPS", url, ExtractorLinkType.VIDEO) {
            quality = Qualities.Unknown.value
        })
        return true
    }

    companion object {
        private val DIRECT_FILE_URL = Regex("^https://pixeldrain\\.(?:dev|com)/api/file/[A-Za-z0-9_-]{1,64}$")
    }
}
