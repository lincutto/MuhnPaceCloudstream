package com.muhnpacedirect

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
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
    override var mainUrl = "https://pixeldrain.com"
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
        newHomePageResponse(request.name, if (page == 1) arcs.map(::arcSearchResponse) else emptyList())

    override suspend fun search(query: String): List<SearchResponse> {
        val words = query.lowercase().trim().split(Regex("\\s+")).filter(String::isNotBlank)
        if (words.isEmpty()) return emptyList()
        return arcs.filter { arc ->
            val searchable = "muhn pace one piece english dubbed ${arc.title}".lowercase()
            words.all { it in searchable }
        }.map(::arcSearchResponse)
    }

    private fun isVideoFile(file: JSONObject): Boolean {
        val name = file.optString("name")
        val mime = file.optString("mime_type")
        return mime.startsWith("video/", ignoreCase = true) ||
            name.endsWith(".mp4", ignoreCase = true) ||
            name.endsWith(".mkv", ignoreCase = true) ||
            name.endsWith(".m4v", ignoreCase = true) ||
            name.endsWith(".webm", ignoreCase = true) ||
            name.endsWith(".mov", ignoreCase = true) ||
            name.endsWith(".avi", ignoreCase = true)
    }

    override suspend fun load(url: String): LoadResponse? {
        val arc = arcs.firstOrNull { url == "$mainUrl/l/${it.listId}" } ?: return null
        val result = JSONObject(app.get("$mainUrl/api/list/${arc.listId}").text)
        if (!result.optBoolean("success", false)) {
            throw ErrorLoadingException(result.optString("message", "Pixeldrain refused the collection request"))
        }
        val files = result.optJSONArray("files")
            ?: throw ErrorLoadingException("Pixeldrain returned no file listing")

        // Pixeldrain's list API documents its files as an ordered array.
        // Keeping that order avoids mis-sorting episode numbers and resolution labels.
        val episodes = (0 until files.length()).mapNotNull { fileIndex ->
            val file = files.optJSONObject(fileIndex) ?: return@mapNotNull null
            val id = file.optString("id")
            if (!FILE_ID.matches(id) || !isVideoFile(file)) return@mapNotNull null
            id to file.optString("name", "Episode ${fileIndex + 1}")
        }.mapIndexed { episodeIndex, (fileId, fileName) ->
            // IMPORTANT: pass a complete HTTPS URL. The previous prototype passed
            // raw IDs through newEpisode(), which silently rewrote them with fixUrl.
            newEpisode("$mainUrl/api/file/$fileId") {
                name = fileName
                episode = episodeIndex + 1
                season = 1
            }
        }

        return newTvSeriesLoadResponse(
            "Muhn Pace • ${arc.title} (English Dub)", url, TvType.TvSeries, episodes
        ) {
            plot = "English-dubbed fan edit. Episodes are listed in the public Pixeldrain collection's original order. " +
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

        callback(newExtractorLink(name, "Pixeldrain • Direct HTTPS", data, ExtractorLinkType.VIDEO) {
            referer = "$mainUrl/"
            quality = Qualities.Unknown.value
        })
        return true
    }

    companion object {
        private val FILE_ID = Regex("^[A-Za-z0-9_-]{1,64}$")
        private val DIRECT_FILE_URL = Regex("^https://pixeldrain\\.com/api/file/[A-Za-z0-9_-]{1,64}$")
    }
}
