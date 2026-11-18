package com.muhnpaceeggheaddev

import com.lagradost.cloudstream3.APIHolder
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.muhnpaceeggheaddev.baseline.EditResearchRegistry
import com.muhnpaceeggheaddev.baseline.MuhnPaceProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class EggheadDevTest {
    private val first = ExpansionMetadata.episodes.first()
    // Offline transport fixtures only. No fixture file is registered in the shipped provider.
    private val file = ApprovedFile(SourceType.ONE_PACE, "testFixture", "a".repeat(64), setOf("en"), first.originalEpisodes)

    private fun resource(name: String) = javaClass.getResourceAsStream("/$name")!!.bufferedReader().use { it.readText() }

    private val savedFiles by lazy {
        val collections = JSONObject(resource("catalogue-2026-11-14.json")).getJSONArray("collections")
        (0 until collections.length()).map(collections::getJSONObject)
            .associate { it.getString("id") to it.getJSONArray("files") }
    }

    private suspend fun savedSeries(files: Map<String, JSONArray> = savedFiles) =
        MuhnPaceProvider().run { seriesFromFiles(seriesUrl, files)!! }

    @Test
    fun bothPluginsRegisterWithSeparateNames() {
        val production = com.muhnpacedirect.MuhnPacePlugin().apply { filename = "test/MuhnPace.cs3" }
        val development = EggheadDevPlugin().apply { filename = "test/MuhnPaceEggheadDev.cs3" }
        try {
            production.load()
            development.load()
            val v7 = APIHolder.getApiFromNameNull("Muhn Pace Rework (by lincutto)")!!
            val dev = APIHolder.getApiFromNameNull("Muhn Pace Rework • Egghead Dev (by lincutto)")!!
            assertTrue(v7 is com.muhnpacedirect.MuhnPaceProvider)
            assertTrue(dev is EggheadDevProvider)
            assertEquals(production.filename, v7.sourcePlugin)
            assertEquals(development.filename, dev.sourcePlugin)
        } finally {
            APIHolder.allProviders.filter { it.sourcePlugin in setOf(production.filename, development.filename) }
                .forEach { APIHolder.removePluginMapping(it); APIHolder.allProviders.remove(it) }
        }
    }

    @Test
    fun reviewedStoryOrderAndUnreleasedSeason() {
        val rows = ExpansionMetadata.episodes
        assertEquals((1..45).toList(), rows.map { it.number })
        assertEquals(setOf(17), rows.map { it.season }.toSet())
        assertEquals((1086..1155).toSet(), rows.flatMap { it.originalEpisodes }.toSet())
        assertEquals((1086..1130).toSet(), rows.take(22).flatMap { it.originalEpisodes }.toSet())
        assertEquals(listOf(1108, 1110, 1111), rows[11].originalEpisodes)
        assertEquals(listOf(1129, 1130), rows[21].originalEpisodes)
        assertEquals(listOf(1131), rows[22].originalEpisodes)
        assertEquals(listOf(1137, 1138), rows[28].originalEpisodes)
        assertEquals(listOf(1139, 1140), rows[29].originalEpisodes)
        assertEquals((1141..1155).toList(), rows.filter { it.preferred == SourceType.ORIGINAL }.map { it.originalEpisodes.single() })
        val catalogue = ExpansionCatalogue()
        assertEquals(rows.take(30), catalogue.availableEpisodes)
        for (story in catalogue.availableEpisodes) {
            val source = catalogue.choices(story).single()
            assertEquals(story.preferred, source.type)
            assertEquals(story.originalEpisodes, source.originalEpisodes)
        }
        assertEquals(catalogue.availableEpisodes.map { it.key }.toSet(), reviewedEggheadReleases.keys)
        assertEquals(30, reviewedEggheadReleases.values.map { it.file.id }.toSet().size)
    }

    @Test
    fun stableSlotsRequireEnglishAndCompleteCoverage() {
        val sources = SourceType.entries.reversed().map { file.copy(type = it) }
        val partial = file.copy(type = SourceType.ORIGINAL, originalEpisodes = listOf(1086))
        val japanese = file.copy(audioTracks = setOf("ja"))
        val dual = file.copy(audioTracks = setOf("ja", "en"))
        val third = ExpansionMetadata.episodes[2]
        val catalogue = ExpansionCatalogue(mapOf(first.key to (sources + listOf(partial, japanese, dual)),
            third.key to listOf(file.copy(originalEpisodes = third.originalEpisodes))))
        assertEquals(SourceType.entries, catalogue.choices(first).map { it.type })
        assertEquals(listOf(first, third), catalogue.availableEpisodes)
        assertTrue(ExpansionCatalogue(mapOf("17/2" to sources)).choices(first).isEmpty())
    }

    private fun info(source: ApprovedFile = file) = JSONObject().put("success", true).put("id", source.id).put("hash_sha256", source.sha256)
        .put("mime_type", "video/mp4").put("size", 1024L).put("availability", "").put("abuse_type", "")
        .put("can_download", true).put("allow_video_player", true)

    private fun response(url: String, code: Int, body: ByteArray, mime: String = "application/json"): Response =
        Response.Builder().request(Request.Builder().url(url).build()).protocol(Protocol.HTTP_1_1)
            .code(code).message("fixture").header("Content-Type", mime).header("Content-Range", "bytes 0-11/1024")
            .body(body.toResponseBody()).build()

    @Test
    fun playbackReturnsOneNativeEnglishLinkAfterAnEquivalentFallback() = runBlocking {
        val preferred = file.copy(type = SourceType.MUHN_PACE, id = "fixtureUnavailable")
        val catalogue = ExpansionCatalogue(mapOf(first.key to listOf(file, preferred)))
        val requested = mutableListOf<String>()
        val adapter = PixeldrainDubAdapter { url, headers ->
            requested += url
            when {
                url.endsWith("/${preferred.id}/info") -> response(url, 403, byteArrayOf())
                url.endsWith("/info") -> {
                    assertTrue(headers.isEmpty())
                    response(url, 200, info().toString().toByteArray())
                }
                else -> {
                    assertEquals(mapOf("Range" to "bytes=0-11"), headers)
                    response(url, 206, byteArrayOf(0, 0, 0, 24) + "ftypisom".toByteArray(), "video/mp4")
                }
            }
        }
        val provider = EggheadDevProvider(catalogue, adapter)
        val links = mutableListOf<ExtractorLink>()
        assertTrue(provider.loadLinks("${provider.seriesUrl}/17/1", false, {}, links::add))
        val url = "https://pixeldrain.dev/api/file/${file.id}"
        assertEquals(listOf("https://pixeldrain.dev/api/file/${preferred.id}/info", "$url/info", url), requested)
        val link = links.single()
        assertEquals(provider.name, link.source)
        assertEquals("One Pace • English dub", link.name)
        assertEquals(ExtractorLinkType.VIDEO, link.type)
        assertEquals(url, link.url)
        assertTrue(link.headers.isEmpty())
    }

    @Test
    fun restrictionsUnexpectedMediaAndNetworkFailuresEmitNothing() = runBlocking {
        val cases = listOf(info().put("availability", "hotlink_detected"), info().put("can_download", false),
            info().put("allow_video_player", false), info().put("id", "wrongFixture"),
            info().put("hash_sha256", "b".repeat(64)), info().put("mime_type", "text/html"),
            info().put("abuse_type", "copyright"), JSONObject())
        for (row in cases) {
            val adapter = PixeldrainDubAdapter { url, _ ->
                assertTrue(url.endsWith("/info"))
                response(url, 200, row.toString().toByteArray())
            }
            assertNull(adapter.resolve(file, "test"))
        }
        for (code in listOf(200, 206, 302, 403, 429)) {
            val adapter = PixeldrainDubAdapter { url, _ ->
                if (url.endsWith("/info")) response(url, 200, info().toString().toByteArray())
                else response(url, code, "not an MP4!!".toByteArray(), "video/mp4")
            }
            assertNull(adapter.resolve(file, "test"))
        }
        assertNull(PixeldrainDubAdapter { _, _ -> throw IOException("offline") }.resolve(file, "test"))
        assertNull(PixeldrainDubAdapter { _, _ -> error("must not request media") }
            .resolve(file.copy(audioTracks = setOf("ja")), "test"))
    }

    @Test
    fun shippedProviderRejects1155UnapprovedFilesAndUnlistedRoutes() = runBlocking {
        val provider = EggheadDevProvider(adapter = PixeldrainDubAdapter { _, _ -> error("unexpected network") })
        val urls = listOf("https://anikoto.wtf/one-piece-episode-1155/", "${provider.seriesUrl}/17/45",
            "${provider.seriesUrl}/17/31", "${provider.seriesUrl}/18/1", "https://pixeldrain.dev/api/file/HZ9VRHCV")
        assertFalse(provider.loadLinks("${provider.seriesUrl}/17/1", true, {}, { fail("unexpected cast link") }))
        for (url in urls) assertFalse(provider.loadLinks(url, false, {}, { fail("unexpected link") }))
        assertNull(provider.load("https://pixeldrain.dev/muhnpace/one-piece"))
    }

    @Test
    fun isolatedHomeAndCataloguePreserveReviewedTitlesIdentitiesAndLinks() = runBlocking {
        val dev = EggheadDevProvider()
        val original = savedSeries()
        val result = dev.developmentSeries(savedSeries())
        val page = dev.mainPage.single()
        val card = dev.getMainPage(1, MainPageRequest(page.name, page.data, page.horizontalImages)).items.single().list.single()
        assertEquals(dev.seriesUrl, card.url)
        assertEquals(card.url, dev.search("one piece").single().url)
        assertEquals(dev.name, card.apiName)
        assertEquals(card.url, result.uniqueUrl)
        assertNotEquals(original.uniqueUrl, result.uniqueUrl)
        assertNotEquals(original.apiName, result.apiName)
        assertEquals(original.posterUrl, result.posterUrl)
        assertEquals(original.backgroundPosterUrl, result.backgroundPosterUrl)
        val seasons = result.seasonNames!!.associateBy { it.season }
        assertEquals((1..9).toList() + (11..15) + 17, seasons.keys.toList())
        assertTrue(seasons.values.all { it.name!!.contains(" Arc") && it.displaySeason == null })
        assertEquals("Wano Arc", seasons.getValue(15).name)
        assertEquals("Egghead Arc", seasons.getValue(17).name)
        assertEquals(card.url, dev.search("Wano Arc").single().url)
        assertEquals(card.url, dev.search("egghead").single().url)

        val frozen = JSONArray(resource("v3-episode-contract.json"))
        val legacy = result.episodes.filter { it.season != 17 }
        val before = (0 until frozen.length()).map(frozen::getJSONObject)
        val retained = before.filterNot { it.getString("file_id") == "DujFEHwH" }
        val originalsByRoute = original.episodes.associateBy { it.data }
        assertEquals(274, before.size)
        assertEquals(before.map { it.getString("file_id") }, original.episodes.map { it.data.substringAfterLast('/') })
        assertEquals(before.map { it.getString("file_id") }.sorted(), EditResearchRegistry.entries.map { it.fileId }.sorted())
        assertEquals(273, legacy.size)
        assertEquals(retained.map { it.getString("file_id") }, legacy.map { it.data.substringAfterLast('/') })
        assertEquals(303, result.episodes.size)
        assertEquals((1..51).toList(), legacy.filter { it.season == 15 }.map { it.episode })
        assertEquals(result.episodes.size, result.episodes.map { it.data }.toSet().size)
        assertEquals(result.episodes.size, result.episodes.map { it.season to it.episode }.toSet().size)
        legacy.forEachIndexed { index, ep ->
            val row = retained[index]
            val season = row.getInt("season")
            val number = row.getInt("episode")
            val old = originalsByRoute.getValue(ep.data)
            assertEquals(row.getString("name"), old.name)
            assertEquals(if (season == 16) 15 else season, ep.season)
            assertEquals(if (season == 16) 26 + number else number, ep.episode)
            assertEquals("https://pixeldrain.dev/api/file/${row.getString("file_id")}", ep.data)
            assertEquals(old.data, ep.data)
            assertEquals(old.description, ep.description)
            assertEquals(old.posterUrl, ep.posterUrl)
            val title = old.name!!.substringBeforeLast(" - ")
            val coverage = old.name!!.substringAfterLast(" - ")
            val expected = when {
                season == 16 -> title
                coverage.all(Char::isDigit) -> "$title • Episode $coverage"
                else -> "$title • Episodes ${coverage.replace('-', '–')}"
            }
            assertEquals(expected, ep.name)
            val links = mutableListOf<ExtractorLink>()
            assertTrue(dev.loadLinks(ep.data, false, {}, links::add))
            assertEquals(ep.data, links.single().url)
            assertEquals(ExtractorLinkType.VIDEO, links.single().type)
        }

        val egghead = result.episodes.filter { it.season == 17 }
        assertEquals((1..30).toList(), egghead.map { it.episode })
        assertEquals("New Emperors • Episodes 1086–1088", egghead.first().name)
        assertEquals("Future Island Egghead • Episodes 1089–1090", egghead[2].name)
        assertEquals("Escape Limit • Episodes 1108, 1110–1111", egghead[11].name)
        assertEquals(30, egghead.map { it.description }.toSet().size)
        assertEquals(30, egghead.map { it.posterUrl }.toSet().size)
        for (ep in egghead) {
            val release = reviewedEggheadReleases.getValue("17/${ep.episode}")
            assertEquals("${dev.seriesUrl}/17/${ep.episode}", ep.data)
            assertEquals(release.synopsis, ep.description)
            assertEquals(release.thumbnailUrl, ep.posterUrl)
            if (ep.episode!! <= 22) {
                assertEquals(release.releaseName.substringAfter(" - "), ep.name!!.substringBefore(" • "))
            }
        }

        // Missing remote files must not shift saved keys on either side of the Wano join.
        val subset = savedFiles.toMutableMap()
        for ((list, missing) in mapOf("QZw3Ejpy" to "6dQ6oqti", "ZQhstabS" to "A7NRZuaf")) {
            val rows = savedFiles.getValue(list)
            subset[list] = JSONArray((0 until rows.length()).map(rows::getJSONObject).filter { it.getString("id") != missing })
        }
        val reduced = dev.developmentSeries(savedSeries(subset))
        assertEquals(result.episodes.filterNot { it.data.substringAfterLast('/') in setOf("6dQ6oqti", "A7NRZuaf") },
            reduced.episodes)
    }

    @Test
    fun mergedWanoRetainsAcceptedCoverageAndOnlyTheStoryTitleForUnknowns() = runBlocking {
        val rows = EggheadDevProvider().developmentSeries(savedSeries()).episodes.filter { it.season == 15 }
        val reviewed = EditResearchRegistry.entries.filter { it.collectionId in setOf("QZw3Ejpy", "sF9stHJo") }
        assertEquals(reviewed.map { it.fileId }, rows.map { it.data.substringAfterLast('/') })
        assertEquals((1..51).toList(), rows.map { it.episode })
        assertEquals(26, reviewed.count { it.acceptedEpisodes.isNotEmpty() })
        assertEquals("Enma • Episodes 955–956", rows[25].name)
        assertEquals("Robin and Her Friends", rows[26].name)
        assertEquals("Nami and Zeus", rows[27].name)
        for ((row, edit) in rows.drop(26).zip(reviewed.drop(26))) {
            assertTrue(edit.acceptedEpisodes.isEmpty())
            assertEquals(edit.displayTitle.substringBeforeLast(" - "), row.name)
        }
    }

    @Test
    fun fishManDuplicateHasTheSameMediaAndOnlyTheMainSeasonIsListed() = runBlocking {
        val edits = EditResearchRegistry.entries.associateBy { it.fileId }
        val main = edits.getValue("G3sZ216k")
        val alternate = edits.getValue("DujFEHwH")
        assertEquals(main.sourceSha256, alternate.sourceSha256)
        assertEquals(main.sourceFileName, alternate.sourceFileName)
        assertEquals(listOf(552, 553), main.acceptedEpisodes)
        assertEquals(main.acceptedEpisodes, alternate.acceptedEpisodes)
        val dev = EggheadDevProvider()
        val series = dev.developmentSeries(savedSeries())
        assertEquals(listOf("Fish-Man Island Arc"), series.seasonNames!!.mapNotNull { it.name }.filter { "Fish-Man" in it })
        val episode = series.episodes.single { it.data.endsWith("/G3sZ216k") }
        assertEquals(9 to 13, episode.season to episode.episode)
        assertFalse(series.episodes.any { it.data.endsWith("/DujFEHwH") || it.season == 10 })
        for (id in listOf(main.fileId, alternate.fileId)) {
            val url = "https://pixeldrain.dev/api/file/$id"
            val links = mutableListOf<ExtractorLink>()
            assertTrue(dev.loadLinks(url, false, {}, links::add))
            assertEquals(url, links.single().url)
        }
    }

    @Test
    fun eggheadUsesPublishedTitlesAndPreservesCommunityReleaseIdentities() = runBlocking {
        val series = EggheadDevProvider().developmentSeries(savedSeries())
        val titles = series.episodes.filter { it.season == 17 && it.episode!! >= 22 }.map { it.name }
        assertEquals(listOf(
            "Kuma Bear • Episodes 1129–1130",
            "A Fleeting Moment of Happiness - Kumachi and Ginny • Episode 1131",
            "A Pledge to Ginny - Kuma Becomes a Father • Episode 1132",
            "To Save His Daughter - Kuma the Timid Pacifist • Episode 1133",
            "Cruel Fate - Kuma's Decision as a Father • Episode 1134",
            "To the Sea Where My Father is! The Future Bonney Chooses • Episode 1135",
            "Kuma's Life • Episode 1136",
            "Thank You, Daddy • Episodes 1137–1138",
            "The Height of Folly • Episodes 1139–1140",
        ), titles)
        val releases = (23..30).map { reviewedEggheadReleases.getValue("17/$it") }
        assertTrue(releases.all { it.file.type == SourceType.COMMUNITY_EDIT })
        assertEquals((1131..1136).map { "Kuma Cut $it" } + listOf("Shaved Egghead 1137+8", "Shaved Egghead 1139+40"),
            releases.map { it.releaseName })
        assertEquals(listOf("usp6W28S", "2A2TWcz8"), releases.takeLast(2).map { it.file.id })
        val reserved = ExpansionMetadata.episodes.filter { it.preferred == SourceType.ORIGINAL }
        assertEquals((1131..1136).toSet() + (1141..1155), eggheadAnimeTitles.keys)
        assertTrue(reserved.all { it.title == eggheadAnimeTitles.getValue(it.originalEpisodes.single()) })
        assertTrue(series.episodes.none { it.season == 17 && it.episode!! > 30 })
    }

    @Test
    fun catalogueTitlesUseCanonicalCoverageAndKeepShortPublishedTitles() = runBlocking {
        val rows = EggheadDevProvider().developmentSeries(savedSeries()).episodes
        assertEquals("Monster • Episodes 290, 293", rows.single { it.data.endsWith("/zEmPxD6p") }.name)
        for (ep in rows) {
            val title = requireNotNull(ep.name)
            if (" • " !in title) {
                assertTrue(ep.season == 15 && ep.episode!! in 27..51)
                continue
            }
            val label = title.substringAfterLast(" • ")
            val groups = label.substringAfter(' ').split(", ")
            var previous = 0
            for (group in groups) {
                val endpoints = group.split('–').map(String::toInt)
                assertTrue(title, endpoints.size in 1..2 && endpoints.first() > previous)
                if (endpoints.size == 2) assertTrue(title, endpoints.last() > endpoints.first())
                previous = endpoints.last()
            }
            val word = if (groups.size == 1 && '–' !in groups.single()) "Episode" else "Episodes"
            assertEquals(title, word, label.substringBefore(' '))
        }
    }

    @Test
    fun staleSourceIdentityCannotAcquireAnAcceptedRangeOrHideAPlayableRow() = runBlocking {
        val rows = JSONArray(savedFiles.getValue("QZw3Ejpy").toString())
        val first = (0 until rows.length()).map(rows::getJSONObject).first { it.getString("id") == "6dQ6oqti" }
        first.put("hash_sha256", "0".repeat(64))
        val result = EggheadDevProvider().developmentSeries(savedSeries(savedFiles + ("QZw3Ejpy" to rows)))
        val changed = result.episodes.single { it.data.endsWith("/6dQ6oqti") }
        assertEquals("One Piece", changed.name)
        assertEquals(303, result.episodes.size)
    }

    @Test
    fun allReviewedReleasesUseTheNativeV2Adapter() = runBlocking {
        val catalogue = ExpansionCatalogue()
        for (story in catalogue.availableEpisodes) {
            val source = catalogue.choices(story).single()
            val url = "https://pixeldrain.dev/api/file/${source.id}"
            val requests = mutableListOf<String>()
            val provider = EggheadDevProvider(catalogue, PixeldrainDubAdapter { request, headers ->
                requests += request
                if (request.endsWith("/info")) {
                    assertTrue(headers.isEmpty())
                    val metadata = info(source).put("embed_domains", JSONArray().put("onepace.net"))
                    response(request, 200, metadata.toString().toByteArray())
                } else {
                    assertEquals(mapOf("Range" to "bytes=0-11"), headers)
                    response(request, 206, byteArrayOf(0, 0, 0, 32) + "ftypisom".toByteArray(), "video/mp4")
                }
            })
            val links = mutableListOf<ExtractorLink>()
            assertTrue(provider.loadLinks("${provider.seriesUrl}/${story.key}", false, {}, links::add))
            val link = links.single()
            assertEquals(listOf("$url/info", url), requests)
            assertEquals(url, link.url)
            assertEquals(ExtractorLinkType.VIDEO, link.type)
            assertEquals("${story.preferred.label} • English dub", link.name)
            assertEquals(1080, link.quality)
            assertTrue(link.headers.isEmpty())
            assertTrue(link.referer.isEmpty())
        }
        val firstFile = catalogue.choices(first).single()
        assertEquals("HZ9VRHCV", firstFile.id)
        assertEquals("b396740f0b72951c7676958674dc60e34e418a6852b8dbf0aec60c602c5ef082", firstFile.sha256)
    }
}
