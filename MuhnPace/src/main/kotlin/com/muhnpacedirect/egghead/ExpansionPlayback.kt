package com.muhnpacedirect.egghead

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.DataInputStream
import java.io.IOException

/** Enter a file only after confirming external-player usage, its audio tracks and its complete story cut. */
internal data class ApprovedFile(
    val type: SourceType,
    val id: String,
    val sha256: String,
    val audioTracks: Set<String>,
    val originalEpisodes: List<Int>,
    val quality: Int = Qualities.Unknown.value,
) {
    init {
        require(Regex("[A-Za-z0-9_-]{1,64}").matches(id))
        require(Regex("[a-f0-9]{64}").matches(sha256))
    }
}

internal class ExpansionCatalogue(
    // Each key signs off equivalence to that particular cut. Matching anime numbers alone is insufficient.
    private val filesByStory: Map<String, List<ApprovedFile>> =
        reviewedEggheadReleases.mapValues { listOf(it.value.file) },
) {
    fun choices(episode: ExpansionEpisode): List<ApprovedFile> = filesByStory[episode.key].orEmpty()
        .filter { it.audioTracks == setOf("en") && it.originalEpisodes == episode.originalEpisodes }
        .sortedBy { it.type.ordinal }

    // Missing releases leave their reserved navigation slots open.
    val availableEpisodes = ExpansionMetadata.episodes.filter { choices(it).isNotEmpty() }
}

private const val PIXELDRAIN = "https://pixeldrain.dev/api/file"

internal class PixeldrainDubAdapter(
    private val get: suspend (String, Map<String, String>) -> Response = { url, headers ->
        app.get(url, headers = headers, allowRedirects = false, cacheTime = 0, timeout = 10).okhttpResponse
    },
) {
    private suspend fun fileInfo(url: String): JSONObject? = get("$url/info", emptyMap()).use {
        if (it.code == 200) JSONObject(it.body.string()) else null
    }

    private suspend fun hasMp4Header(url: String, size: Long): Boolean {
        return get(url, mapOf("Range" to "bytes=0-11")).use { response ->
            if (response.code != 206 || response.header("Content-Range") != "bytes 0-11/$size" ||
                response.header("Content-Type")?.substringBefore(';') != "video/mp4") return@use false
            val bytes = ByteArray(12)
            DataInputStream(response.body.byteStream()).readFully(bytes)
            bytes.copyOfRange(4, 8).contentEquals("ftyp".toByteArray(Charsets.US_ASCII))
        }
    }

    suspend fun resolve(file: ApprovedFile, providerName: String): ExtractorLink? {
        if (file.audioTracks != setOf("en")) return null
        val url = "$PIXELDRAIN/${file.id}"
        try {
            val row = fileInfo(url) ?: return null
            if (!row.getBoolean("success") || row.getString("id") != file.id ||
                row.getString("hash_sha256") != file.sha256 || row.getString("mime_type") != "video/mp4" ||
                row.getString("availability").isNotEmpty() || row.getString("abuse_type").isNotEmpty() ||
                !row.getBoolean("can_download") || !row.getBoolean("allow_video_player")) return null
            // Native HTTPS playback uses no embed, referrer, cookies or host override.
            val size = row.getLong("size")
            if (size < 12 || !hasMp4Header(url, size)) return null
        } catch (_: IOException) {
            return null
        } catch (_: JSONException) {
            return null
        }
        return newExtractorLink(providerName, "${file.type.label} • English dub", url,
            ExtractorLinkType.VIDEO) { quality = file.quality }
    }
}
