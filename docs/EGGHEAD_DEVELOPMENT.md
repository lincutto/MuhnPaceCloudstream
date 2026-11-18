# Egghead development

Branch `codex/egghead-expansion` starts from production v7 at `ce052db400fa6d56fc4b54864ae9f26dc9fd6b65`. The opt-in module is `MuhnPaceEggheadDev`, version 1. It relocates the v7 provider at build time and registers one development provider with its own series URL and progress keys. It requests the original sixteen lists and preserves their episode data, artwork and links.

## Source review, 17–18 November 2026

| Source | Release evidence | External-player finding |
| --- | --- | --- |
| AniKoto | The supplied page for 1180 and the episode 1155 page returned HTTP 200. The latter identifies episode 1155 in the page markup. The user reports English playback in another Cloudstream provider. | The observed site uses WordPress, `#ani_detail` and a WordPress AJAX endpoint. Neither episode page contains a direct video or iframe. The reference provider uses a different episode-list and server API, with separate dub flags. Its configured host redirected to `anikototv.bz`, where One Piece has a different series ID. The reference episode-list request and the observed site's watch script returned 403. Investigation stopped there. |
| Reference code and streams | The reference repository declares GPLv3 for its source-built plugins. Its MegaPlay resolver processes protected player tokens and signed media requests. | No reference code was copied. The observed terms page supplied no external playback grant and its footer attributes sources to third parties. No authorised AniKoto stream for 1155 was established. |
| One Pace | The standard 1080p English Dub links for all 22 official releases match the supplied Pixeldrain list, edit numbers and filenames. The official per-edit anime mappings span 1086–1130. The leading numbers in the filenames are manga chapters. | Every file in this collection sets `embed_domains` to `["onepace.net"]`. The development provider omits them. No alternate host, forged referrer, or rehosting is used. |
| Kuma Cut and Shaved Egghead | The editors confirm six single-episode Kuma edits for 1131–1136 and two Shaved edits for 1137–1138 and 1139–1140. Their dub streaming list contains these eight MP4s. The 1134 and 1135 filenames lack an individual dub tag. The sheet marks the releases through 1140 as dual audio and subsequent edits through 1155 as lacking dual audio. | A normal anonymous byte-range request for Kuma 1131 returned HTTP 206 with video/mp4. The guide links the host's viewer. The file info shows availability but supplies no confirmation of permission for this extension's anonymous hotlinking. Pixeldrain specifies premium uploader or downloader conditions. The media response establishes reachability only. |
| Original English dub | Toei and Crunchyroll announced the completed Egghead dub, including release of 1144–1155 on Crunchyroll. | No permitted direct English stream was established for the original fallback. A service watch page cannot be passed to the built-in player as a video. |

### Review sources

- [Observed episode 1155](https://anikoto.wtf/one-piece-episode-1155/), [observed terms](https://anikoto.wtf/terms/), [reference provider, resolver and extractors](https://github.com/yonkou0/CloudStreamRepo/tree/main/AniKotoAnime/src/main/kotlin/com/anikoto), [reference licence](https://github.com/yonkou0/CloudStreamRepo/blob/main/LICENSE).
- [One Pace Egghead release metadata](https://onepace.net/en/watch#egghead), [English collection metadata and file restrictions](https://pixeldrain.dev/api/list/nKaeVsMp), [Pixeldrain API](https://pixeldrain.dev/api), [Pixeldrain hotlink policy](https://pixeldrain.net/about).
- [Community guide](https://steamcommunity.com/sharedfiles/filedetails/?id=3685024934), [editors’ release sheet](https://docs.google.com/spreadsheets/d/1ODt-b2L9PyAr0xEWhj1yIOqp__crBFVXhvejblvP_mU/edit), [community dub list](https://pixeldrain.dev/api/list/ef1wn8dY), [editors’ change notes in the download collection](https://pixeldrain.com/l/w3Fs9nnQ).
- [Toei and Crunchyroll's English dub announcement](https://www.toei-animation-usa.com/press-releases/pressrelease-2-17-26.pdf).

## Catalogue and playback

The release metadata reserves 45 stable Season 17 positions across anime episodes 1086–1155. The player currently exposes **zero** of these positions. Season 18 is deferred.

One Pace’s edits follow scene order. Neighbouring cuts can contain different scenes from the same anime episode. Edit 12 uses 1108 and 1110–1111. One Pace 21–22 already cover material in the community list’s 1127–1130 entries. Those entries are excluded. The editors describe rearranged and shortened scenes, including 1136 post-credits footage from the next recap. Scene-by-scene completeness across the handoff has not been independently verified.

`ExpansionMetadata` holds the season position, title, original episode numbers and preferred edit type. To enable a source, record an external-playback-approved file under its reviewed story key in `ExpansionCatalogue`, pin its ID and SHA-256, and confirm its audio tracks and scene coverage. Only English-only files are eligible. An original fallback must deliver the entire curated cut's equivalent material as one playable file. Two Cloudstream links are alternative videos, so they cannot represent successive halves of an edit.

The provider exposes the continuous prefix with approved sources, and rechecks candidates at playback in Muhn Pace, One Pace, community edit, original order. The Pixeldrain adapter checks current file metadata and an anonymous 12-byte MP4 range before it emits a native video link. Transport failures, host restrictions, changed files and unknown audio produce no link. Historical v7 episodes retain their existing direct playback path.

## Development validation and Fire TV

Use the existing JDK 17, Android 35 and Gradle 8.12 setup:

```sh
gradle -PeggheadDev=true :MuhnPaceEggheadDev:testDebugUnitTest :MuhnPaceEggheadDev:make --no-daemon
python3 scripts/assemble_egghead_dev.py
```

The packaging script verifies the DEX and plugin identity and prepares `MuhnPaceEggheadDev/build/fire-tv/`. CI uploads that directory after the tests and package checks pass. The files are for the development branch’s `dev-install/` directory only.

The Android build from `06c09390689e25a0bda1dedc5934fac2975af789` passed 25 repository checks and seven Kotlin tests. The resulting `MuhnPaceEggheadDev.cs3` is 106,586 bytes, with SHA-256 `f2e8d360514a8f0a6c6decf92ae1c5dcacd54f32a3ae97bc3f56367873176004`. Resolver success and failure tests use offline HTTP fixtures. [Build and test reports](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/44205036756)

Open **Cloudstream Beta → Settings → Extensions → Add repository**. Enter the development URL below.

```text
https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/codex/egghead-expansion/dev-install/repo.json
```

Install **Muhn Pace Rework • Egghead Dev (by lincutto)**. Keep v7 installed. Open the development provider and test Season 1 episode 1, then Season 16 episode 25. Seek, exit, and resume in the development provider. Check that your original v7 progress is intact and that Egghead has no selectable row while its sources are unapproved. These checks are pending device testing.

Before promotion, obtain an approved native playback path, verify English on-device, and review the One Pace 22 → Kuma 1131 and Shaved 1140 → original 1141 handoffs. Episode 1155 is still a target for a future positive resolution test.

## Anivexa Anime-API feasibility, 18 November 2026

**English-dubbed original episode 1155 remains unresolved.** The AniList catalogue identifies series 21 as *ONE PIECE*. Toei confirms the English release through 1155. Anivexa supplied neither a live episode row connecting these identities nor media for this episode. The API trial used original episode 1155, which is reserved development position S17E45. [AniList series 21](https://anilist.co/anime/21/) · [Toei announcement](https://www.toei-animation-usa.com/press-releases/pressrelease-2-17-26.pdf)

| Public check | Result |
| --- | --- |
| Anisurge's published base, `https://anivexa-api.vercel.app/` | HTTP 402, Vercel `DEPLOYMENT_DISABLED` |
| `GET /episodes/anikoto/21` on that base | HTTP 402 with an error object reporting “Payment required” |
| `GET /watch/anikoto/21/dub/anikoto-1155` on that base | Same deployment error, before a provider watch response |
| `https://anikoto.cz/`, the fork's default provider site | HTTP 403, Cloudflare access denied |

The fork implements AniKoto plus eleven other provider routes. Both API repositories document self-hosting. Neither declares a code licence or links a maintained public service with permission for this extension to play the underlying streams. The example adapter's MIT licence covers that repository's code. No third-party code was incorporated or deployed. The disabled deployment applies to the API host, so probing the other providers there would not establish a playback path. [Fork and routing](https://github.com/Anikage-Group/Anime-API/blob/b1c4f9bf42692cf86abf6cde9fa2dd463cc06635/index.js) · [Upstream](https://github.com/walterwhite-69/Anivexa-API/tree/421fb8d9c62aed5e6cca17a63526292e5240f21a) · [Example adapter](https://github.com/Anisurge/extensions/blob/c0aec3a615af3a33a41c366a349664b5dc46a6ec/extensions/anivexa-anikoto/anivexa-anikoto.asx)

The fork expects a show slug, a numeric show ID, and an AJAX episode list with per-episode dub flags. The earlier `anikoto.wtf` examination found WordPress AJAX and different identifiers. A normal request to `.cz` stopped at its access restriction. No interchangeable backend or permission to use the provider's blocked-request recovery and third-party player extraction was established. [AniKoto provider](https://github.com/Anikage-Group/Anime-API/blob/b1c4f9bf42692cf86abf6cde9fa2dd463cc06635/providers/anikoto.js)

### Response contract

The following fields were verified in the source. The only live JSON returned by the example deployment was its error response.

- The filtered list puts the show identity in `anikoto.meta` and the original episode number, relative watch ID and audio flag in `anikoto.episodes.dub[]`. A provider error can also appear inside a successful HTTP response. [Filtered response](https://github.com/Anikage-Group/Anime-API/blob/b1c4f9bf42692cf86abf6cde9fa2dd463cc06635/core/episode-cache.js#L205) · [Episode rows](https://github.com/Anikage-Group/Anime-API/blob/b1c4f9bf42692cf86abf6cde9fa2dd463cc06635/providers/anikoto.js#L430)
- The watch response has top-level `anilistId`, `malId`, `episode`, `audio`, `streams`, `subtitles` and `headers`. The AniKoto handler can return HLS, an embed, or an empty stream list. Each HLS entry carries its own referrer and can carry captions. Caption URLs use `url`. The Anisurge example looks under `sdub` or `ssub`, selects the first stream, and reads caption `file`. A `dub` label does not identify an English audio track. [Watch response](https://github.com/Anikage-Group/Anime-API/blob/b1c4f9bf42692cf86abf6cde9fa2dd463cc06635/providers/anikoto.js#L637) · [Anisurge example](https://github.com/Anisurge/extensions/blob/c0aec3a615af3a33a41c366a349664b5dc46a6ec/extensions/anivexa-anikoto/anivexa-anikoto.asx)

The independent Kotlin contract lives in the development module's test source set. Its two suspend callbacks separate fetching a fresh episode/watch response from inspecting a candidate's media and audio. It cross-checks both identities, preserves public playback headers and matching captions, and requires positive evidence of clear MP4 or HLS with English-only audio. Expired URLs, unknown audio, embed pages and provider failures yield no candidate. The tests use synthetic media evidence. The current AniKoto handler emits HLS or embeds; the MP4 case exercises the native handoff contract only.

Six focused JVM tests passed. There is no live Anivexa transport, registered source, or new test package. The previous Android build and its catalogue regression results still apply to the installable v1 package above. Fire TV playback of 1155 through the Muhn Pace development plugin remains unverified.

To resume the live proof, obtain an operational API base whose operator can confirm external-player use of the English media for 1155. Verify the exact dub row, watch identity, real media response and audio on that service before enabling a source or expanding the episode range.

## NivinCNC AniKoto v12, 18 November 2026

The user confirmed English playback of original One Piece episode **1155** in Cloudstream Beta’s native player using NivinCNC’s installed AniKoto v12. The same installation subsequently listed **1160** under English dub. Only 1155 has a reported playback test. Neither observation came from the Muhn Pace provider.

| Published package check | Result |
| --- | --- |
| Repository index | `CNC Repo(All Language)`, provider `AniKoto`, author `NivinCNC`, version 12, Cloudstream API version 1 |
| Downloaded artifact | `AniKoto.cs3`, 66,384 bytes. Its SHA-256 matched the index, `5ce4df1afde0bff6ebb41926c9ef1b70293117d1b50e9045315ef23d2cfebc03` |
| Contents | ZIP integrity passed. The package contains `classes.dex` and `manifest.json`. The manifest identifies `com.anikoto.AnikotoPlugin` and version 12 |
| Source and licence | The publisher’s public `master` tree contains repository material and logos. The `builds` branch publishes compiled plugins. Neither tree contains AniKoto Kotlin source or a licence for reusing its implementation. The README attributes the streams to third parties. |

[Publisher’s index](https://github.com/NivinCNC/CNCVerse-Cloud-Stream-Extension/blob/cf8ef61a91aa6de410c7557de93b554311597ce4/plugins.json) · [Exact artifact inspected](https://github.com/NivinCNC/CNCVerse-Cloud-Stream-Extension/blob/cf8ef61a91aa6de410c7557de93b554311597ce4/AniKoto.cs3) · [Public source tree and README](https://github.com/NivinCNC/CNCVerse-Cloud-Stream-Extension/tree/2600586e69dceeb2ecb87936f17fba62ad153df3)

### Backend and audio selection

- The AniKoto provider constructor sets its base to `https://anikototv.to`. It reads the site’s AJAX episode and server lists. Earlier research observed the site redirect to `anikototv.bz`. The v12 playback path has no identified dependency on the disabled Anivexa API, `anikoto.cz` or the WordPress site at `anikoto.wtf`.
- The load path reads original episode numbers and individual `data-dub` flags, then builds Cloudstream’s dubbed and subbed episode lists. The package index’s `language: en` describes the provider and supplies no per-stream audio evidence.
- The plugin registers a Cloudstream provider and the MegaPlay, Vidwish and Vidtube extractors. Its `loadLinks` implementation can resolve a watch page or a selected server embed. The bundled MegaPlay resolver handles player tokens and signed playlists and can emit a native `M3U8` link. This accounts for browser-free playback. The successful server and final delivery host on the user’s device are unconfirmed.
- The package first selects servers for the requested audio category. If those results are empty, it selects linked server rows without the language filter. Its watch-page fallback defaults a missing or blank episode audio type to `sub`. A dubbed listing and a successful callback therefore cannot independently guarantee English-only audio.

**Integration decision.** Cloudstream exposes installed-provider lookup, `search`, `load`, and the `loadLinks` callback. That callback takes opaque episode data and returns links after the provider has chosen and resolved a server. It exposes no option to require a particular approved upstream playback method before resolution. The site identifies its content as third-party hosted. I found no documented, authorised interface for Muhn Pace to obtain the English stream for 1155. Calling the installed provider would still run its server selection before Muhn Pace could inspect the result. The linked site guide redirected to `anikototv.bz` and returned HTTP 403. No protected stream request was made or reproduced. [Cloudstream host interface](https://github.com/recloudstream/cloudstream/blob/master/library/src/commonMain/kotlin/com/lagradost/cloudstream3/MainAPI.kt) · [Provider site](https://anikototv.bz/)

A documented native-player handoff or a media-host-approved English MP4/HLS interface for 1155 would allow the proof to proceed. After that proof, fresh per-episode dub rows could drive discovery without a weekly extension release. Discovery would use the exact series and original episode number, with separate checks for playable English audio and continuous story coverage before a row becomes visible. Episode 1160 is recorded only as a discovery observation. This investigation adds no runtime code or catalogue entries and requires no Android build.
