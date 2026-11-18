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
