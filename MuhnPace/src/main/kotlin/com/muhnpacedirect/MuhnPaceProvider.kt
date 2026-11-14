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
class MuhnPaceProvider : MainAPI() {
    override var name = "Muhn Pace Direct (English Dub)"
    override var mainUrl = "https://pixeldrain.dev"
    override var lang = "en"
    override val supportedTypes = setOf(TvType.TvSeries)
    override val hasMainPage = true

    private data class Arc(val title: String, val listId: String)

    // 16 collection links, in the same order as the community dub watch guide.
    // These IDs are links to collections, NOT to individual videos.
    private val arcs = listOf(
        Arc("Enies Lobby", "6VyLVkB2"),
        Arc("Post Enies Lobby", "jVhFqouN"),
        Arc("Thriller Bark", "NwJKTg21"),
        Arc("Sabaody Archipelago", "KztkSCuY"),
        Arc("Amazon Lily", "TuZJgwRL"),
        Arc("Impel Down", "4AN9Xxht"),
        Arc("Marineford", "oa8oZvZx"),
        Arc("Post Marineford", "MH7AQkgr"),
        Arc("Fishman Island", "ZQhstabS"),
        Arc("Fishman Island • Episode 15", "kuoQYqfR"),
        Arc("Punk Hazard", "A1jFtPvQ"),
        Arc("Dressrosa", "f8HTXmAk"),
        Arc("Zou", "totP9Xmm"),
        Arc("Whole Cake Island", "AyvkmFFZ"),
        Arc("Wano • Acts 1 & 2", "QZw3Ejpy"),
        Arc("Wano • Act 3", "sF9stHJo"),
    )

    override val mainPage = mainPageOf("muhn" to "Muhn Pace • English dub arcs")

    private fun arcSearchResponse(arc: Arc): SearchResponse =
        newTvSeriesSearchResponse(
            "Muhn Pace • ${arc.title} (Dub)",
            "$mainUrl/l/${arc.listId}",
            TvType.TvSeries,
        )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse =
        newHomePageResponse(request.name, if (page == 1) arcs.map(::arcSearchResponse) else emptyList(), hasNext = false)

    override suspend fun search(query: String): List<SearchResponse> {
        val words = query.lowercase().trim().split(Regex("\\s+")).filter(String::isNotBlank)
        if (words.isEmpty()) return emptyList()
        return arcs.filter { arc ->
            val searchable = "muhn pace one piece english dubbed ${arc.title}".lowercase()
            words.all { it in searchable }
        }.map(::arcSearchResponse)
    }

    internal fun episodesFromFiles(collectionId: String, arcTitle: String, files: JSONArray): List<Episode> =
        EpisodeCatalog.select(collectionId, arcTitle, files).map { metadata ->
            val fileId = metadata.fileId
            newEpisode("$mainUrl/api/file/$fileId") {
                name = metadata.title
                description = metadata.description
                episode = metadata.editNumber
                season = 1
            }
        }

    override suspend fun load(url: String): LoadResponse? {
        val arc = arcs.firstOrNull { url == "$mainUrl/l/${it.listId}" || url == "https://pixeldrain.com/l/${it.listId}" } ?: return null
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

        // Keep source edit numbers (including gaps) and sort numerically within this collection.
        val episodes = episodesFromFiles(arc.listId, arc.title, files)
        if (episodes.isEmpty()) throw ErrorLoadingException("Pixeldrain has no matching English-dub episodes in this collection.")

        return newTvSeriesLoadResponse(
            "Muhn Pace • ${arc.title} (English Dub)", url, TvType.TvSeries, episodes
        ) {
            plot = "English-dubbed fan edit. Episodes are sorted by Muhn Pace edit number; gaps in the source numbering are preserved. " +
                "Original One Piece episode mappings are shown only when verified. " +
                "Direct HTTPS playback depends on Pixeldrain's access, hotlinking and transfer restrictions."
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
