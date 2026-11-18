package com.muhnpaceeggheaddev.anivexa

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.util.TreeMap

/** Offline contract for a future authorised source. There is no HTTP implementation or plugin registration. */
internal class AnivexaDubContract(
    private val aniListId: Int,
    private val malId: Int,
    private val seriesTitle: String,
    private val originalEpisode: Int,
) {
    enum class Format { MP4, HLS }

    data class Caption(val url: String, val label: String, val language: String?)
    data class Candidate(val url: String, val format: Format, val headers: Map<String, String>, val captions: List<Caption>)

    /** Evidence about this URL's media and audio, independent of API labels and caption languages. */
    data class Inspection(
        val status: Int,
        val clearMediaFormat: Format?,
        val audioLanguages: Set<String>,
        val expiresAtEpochSecond: Long? = null,
    ) {
        fun accepts(format: Format, now: Long): Boolean =
            status in setOf(200, 206) && clearMediaFormat == format &&
                (expiresAtEpochSecond == null || expiresAtEpochSecond > now) &&
                audioLanguages.isNotEmpty() && audioLanguages.all {
                    it.substringBefore('-').lowercase() in setOf("en", "eng")
                }
    }

    data class Reply(val status: Int, val body: String) {
        fun document(): JSONObject? = if (status == 200) {
            try { JSONObject(body).takeUnless { it.has("error") } } catch (_: JSONException) { null }
        } else null
    }

    private val episodePath = "watch/anikoto/$aniListId/dub/anikoto-$originalEpisode"

    private fun listed(series: JSONObject): Boolean {
        val provider = series.optJSONObject("anikoto") ?: return false
        val meta = provider.optJSONObject("meta") ?: return false
        if (provider.has("error") || meta.opt("source") != "anikoto" || meta.opt("malId") != malId ||
            !seriesTitle.equals(meta.optString("title").trim(), ignoreCase = true)) return false
        val rows = provider.optJSONObject("episodes")?.optJSONArray("dub").objects()
        val row = rows.singleOrNull { it.opt("number") == originalEpisode } ?: return false
        return row.opt("audio") == "dub" && row.opt("id") == episodePath &&
            (!row.has("sourceNumber") || row.opt("sourceNumber") == originalEpisode)
    }

    /** Call on selection. Neither episode rows nor signed stream URLs are cached here. */
    suspend fun select(
        nowEpochSecond: Long,
        request: suspend (String) -> Reply,
        inspect: suspend (Candidate) -> Inspection?,
    ): Candidate? {
        val watch = try {
            val listing = request("/episodes/anikoto/$aniListId").document() ?: return null
            if (!listed(listing)) return null
            request("/$episodePath").document() ?: return null
        } catch (_: IOException) {
            return null
        }
        if (watch.opt("anilistId") != aniListId || watch.opt("malId") != malId ||
            watch.opt("episode") != originalEpisode || watch.opt("audio") != "dub") return null

        for (stream in watch.optJSONArray("streams").objects()) {
            val candidate = candidate(watch, stream) ?: continue
            val evidence = try { inspect(candidate) } catch (_: IOException) { null }
            if (evidence?.accepts(candidate.format, nowEpochSecond) == true) return candidate
        }
        return null
    }

    private fun candidate(watch: JSONObject, stream: JSONObject): Candidate? {
        val format = when (stream.opt("type")) {
            "hls" -> Format.HLS
            "mp4" -> Format.MP4
            else -> return null
        }
        val url = https(stream.opt("url")) ?: return null
        val headers = TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER)
        val common = watch.optJSONObject("headers") ?: return null
        for (key in common.keys()) {
            val value = common.opt(key) as? String ?: return null
            // Preserve the provider's public playback headers. No credentials or arbitrary header forwarding.
            if (key.lowercase() !in setOf("referer", "user-agent", "origin", "accept") ||
                value.any { it == '\r' || it == '\n' }) return null
            headers[key] = value
        }
        if (stream.has("referer")) headers["Referer"] = https(stream.opt("referer")) ?: return null

        val server = stream.opt("server") as? String
        val captions = (stream.optJSONArray("subtitles").objects() +
            watch.optJSONArray("subtitles").objects().filter { !it.has("source") || it.opt("source") == server })
            .mapNotNull {
                val captionUrl = https(it.opt("url")) ?: return@mapNotNull null
                val lang = it.opt("srclang") as? String
                Caption(captionUrl, it.opt("label") as? String ?: lang.orEmpty(), lang)
            }.distinctBy { it.url }
        return Candidate(url, format, headers, captions)
    }

    private fun JSONArray?.objects(): List<JSONObject> =
        if (this == null) emptyList() else (0 until length()).mapNotNull(::optJSONObject)

    private fun https(value: Any?): String? {
        if (value !is String) return null
        return try {
            val uri = URI(value)
            value.takeIf { uri.scheme == "https" && uri.host != null && uri.rawUserInfo == null && uri.rawFragment == null }
        } catch (_: URISyntaxException) {
            null
        }
    }
}
