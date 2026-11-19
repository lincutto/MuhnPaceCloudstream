# Egghead development

## Egghead Dev v6 original episode numbering

Update **Muhn Pace Rework • Egghead Dev (by lincutto)** in the Egghead testing repository. Open **One Piece (Muhn Pace)** from that provider’s home row or search. The numbered series uses a new URL so its saved positions have their own keys. Existing v5 bookmarks and Continue Watching entries still open the original series with their original episode numbers and resume positions. Production uses its own provider and history. [Fire TV test repository](https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/egghead-test/repo.json)

### What to expect on Fire TV

1. In Whole Cake Island, select *Emperor of the Sea, Charlotte Linlin*. Pause partway through and wait for the idle information panel. Its episode badge should read **E790**.
2. Exit to Continue Watching. The new development card should read **E790** and reopen that same edit at your saved position. Use a point well before the end because the player can put the following edit on the card near completion.
3. With English UI strings and the full player title visible, the heading should read **One Piece (Muhn Pace) - Episode 790 - Emperor of the Sea, Charlotte Linlin • Episodes 788–790**. The episode picker should show **790. Emperor of the Sea, Charlotte Linlin • Episodes 788–790**.
4. In Egghead, start *Luffy vs. Kizaru* (**E1128**) and use Next. It should open *Kuma Bear* (**E1130**). In Wano, *The Onefold Fox* should show **E955** with its reviewed range **954–955**.

### Why the heading and nine numbers are constrained

The inspected rolling Cloudstream pre-release and master refs both resolved to `2dcc25c3945c61dfa95d1710ced7c74acfe84baa`. For TV series the client sorts by season and episode, builds the episode ID from the series ID, internal season and episode number, and drops duplicate IDs. The player advances by list index. Gaps such as E1128 to E1130 work. Equal or decreasing numbers in one arc can hide an edit or change which edit plays next. [TV-series conversion](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2239-L2289) · [Ordered player playlist](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2079-L2087) · [Next by index](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/player/PlayerGeneratorViewModel.kt#L264-L275)

The extension API exposes one numeric episode field. A stable file ID or custom JSON in `Episode.data` selects the video for playback. The client computes the history ID independently. `uniqueUrl` identifies the whole series and `displaySeason` controls the season label. `totalEpisodeIndex` is computed for tracking. Sorting by dates or scores in the episode picker cannot restore an entry already discarded. The anime response path also deduplicates equal numbers within one season and dub track. Exact endings for all edits in the existing arcs require a client field for display numbering independent of ordering and saved IDs. [Episode API](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/library/src/commonMain/kotlin/com/lagradost/cloudstream3/MainAPI.kt#L2538-L2575) · [Series ID](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L370-L378) · [Episode picker sorting](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L1895-L1909) · [Anime conversion](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2182-L2215)

Continue Watching stores the current or next episode number and ID when progress is saved. The home card and the idle panel format that number using the short season/episode formatter. These arcs have a null display season, which gives **E790** in English. The player inserts **Episode 790** between the series name and episode name. Both episode-picker layouts prepend the number. Only a client change could remove those prefixes while retaining the rest of each heading. The extension supplies each story title and its full coverage once. [Resume persistence](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/utils/DataStoreHelper.kt#L702-L749) · [Continue Watching card](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/search/SearchResultBuilder.kt#L254-L268) · [Short formatter](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/utils/AppContextUtils.kt#L683-L693) · [Idle panel](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1631-L1662) · [Player heading](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1877-L1920) · [Episode picker](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/EpisodeAdapter.kt#L205-L209)

**294 of the 303 reviewed edits use the exact ending number.** Five pairs share an ending and four endings go backwards. The other nine edits use the latest covered original that leaves room for the next edit. Each assignment belongs to the release identity and stays fixed if a neighbouring file goes offline. The titles keep all covered originals, including gaps.

| Arc | Edit | Original anime coverage in title | Display |
| --- | --- | --- | --- |
| Impel Down | The Underwater Prison, Impel Down | 422–423, 454 | E423 |
| Impel Down | Friend | 435–438, 446 | E438 |
| Fish-Man Island | At the Sea Forest! Arlong and Hody | 538–539, 541 | E539 |
| Fish-Man Island | Phantom | 563–567 | E566 |
| Dressrosa | Bellamy the Pirate | 707–711 | E710 |
| Whole Cake Island | The Iron Mask | 800–803, 807 | E803 |
| Whole Cake Island | Wave Room | 853–855, 858 | E855 |
| Wano | Void | 917–919, 924 | E919 |
| Egghead | New Emperors | 1086–1088 | E1087 |

## Egghead Dev v5 Wano coverage

All 51 Wano edits retain their season 15 positions. The first 26 match the existing One Pace references, including disjoint coverage at positions 4 and 9. V5 adds original-anime coverage for positions 27–51 (Muhn edits 71–95). Episodes 1046, 1051, 1060 and 1069 each contribute to two edits. The 51-file title fixture records the expected display text in catalogue order. [Published development repository](https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/egghead-test/repo.json) · [Early Wano reference facts](../metadata/phase4b/reference-facts.json) · [All displayed Wano titles](../MuhnPaceEggheadDev/src/test/resources/wano-reviewed-titles.tsv)

The season merge preserved the files and their order. Phase 4B had recorded the late ranges as provisional text in the story titles. Its accepted-coverage lists were empty, so v4 correctly removed those provisional suffixes. V5 bundles the separately reviewed numbers by Muhn file ID and media SHA-256. A source still has to pass the baseline identity checks. A recognized edit without reviewed coverage keeps its story title. [Phase 4B records](../metadata/phase4b/season-16.json) · [V5 per-file evidence](../metadata/wano-act-3-coverage.json)

### How the late ranges were checked

- Matched all 25 selected filenames, IDs and reported hashes to the live Muhn collection. Their measured runtimes agree within one second with the Muhn episode guide, including the regular editions of 80, 84, 87 and 92 and the filler release of 94. The Muhn release catalogue supplies the named cuts. [Muhn collection](https://pixeldrain.dev/api/list/sF9stHJo) · [Muhn episode guide, Wano tab](https://docs.google.com/spreadsheets/d/1n0NlZiHCOghEbMY1zgXKiwutygm7g9XdwhdJipxbmPs/edit) · [Named Muhn releases](https://github.com/6ip/onepace-streams/blob/da83ea2664d74bde8b2624d9cea4856f286a07a7/meta/pp_muhnpace.json)
- Used the Onigashima editor’s anime column for the credited originals. Compared the actual Muhn and reference videos to establish the correspondence for each cut. Video packet timing located candidate shots, then decoded images confirmed them. Each record includes two matching picture timestamps in seconds, Muhn first. For Muhn 84, the editor’s regular download and its mirror have identical MP4 movie indexes and run 43:00 despite the sheet listing 38:39. The selected 39:11 Muhn file contains scenes from all three credited originals. [Editor’s episode guide](https://docs.google.com/spreadsheets/d/1HoYogAchoU5DWxVJzUy3eZcHMZk_hUmzUVnQm9KxeFI/edit) · [Editor’s video collection](https://pixeldrain.com/l/JVMSKn7c) · [Regular Onigashima 42](https://drive.google.com/file/d/1ICVBY_qPwyygx0-7N_6YKaodaiVHVJ7c/view)
- Checked a scene for each of the 56 edit-to-original associations against the numbered anime story. The records give the timestamp in the selected Muhn file, a short scene description and the TVmaze episode ID. Runtime-matched English transcriptions helped locate dialogue, and frames from the Muhn videos corroborated the scenes. This establishes source-episode coverage for the edits. Timing and scene lengths vary between editions. [English transcription manifest](https://github.com/XenaRathon/DubTitlerr-subtitles/blob/f1fb9c4429f2c4fb10187cebd4e85f511653bd17/manifest/One%20Pace.json) · [Numbered Wano episode guide](https://onepiece.fandom.com/wiki/Episode_Guide/Wano_Country_Saga) · [TVmaze episode records](https://api.tvmaze.com/shows/1505/episodes)

The v5 build passed 27 Python checks and 64 Kotlin tests. Local and CI packages matched at 118,867 bytes and SHA-256 `4f9e777d983a1a00edd8379336232052aeefee8f642bac5b791a352267922675`. Comparison with v4 found identical bytecode instructions in 337 baseline, catalogue and playback methods. Device playback testing remains pending. [Validated source](https://github.com/lincutto/MuhnPaceCloudstream/commit/5973689bae40dc681029c4f6671c39f83f97d56b) · [CI run](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/44491175333)

## Egghead Dev v4 catalogue audit

Install **Muhn Pace Rework • Egghead Dev (by lincutto)** using the [development repository URL](https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/egghead-test/repo.json).

| Catalogue | v3 episodes | v4 episodes | v3 seasons | v4 seasons |
| --- | ---: | ---: | ---: | ---: |
| Frozen source lists and reviewed Egghead files | 304 | 303 | 16 | 15 |
| Live source lists, 18 November 2026 | 303 | 302 | 16 | 15 |

**Wano.** All 51 edits keep their development positions in season 15. The first 26 retain their accepted One Pace-derived ranges. After *Enma*, the registry has 25 empty accepted lists. Phase 4B stored approximate numbers from a different cut in its display strings. The v3 formatter stripped those numbers and appended a diagnostic. V4 shows the existing scene titles alone. The internal review identifies each unresolved file, including the partial evidence for edit 71. No new full range qualified. [Per-file review](research/season16-mapping-review-2026-11-17.json) · [Phase 4B records](../metadata/phase4b/season-16.json)

**Fish-Man Island.** Main-season file `G3sZ216k` and alternate `DujFEHwH` have the same filename, size (518,299,087 bytes) and host SHA-256 (`d4acdaa54310f88fa0646337d93a44a2534192968bfddb345a50a0e16db012ad`). Both contain the corrected group shot. The main file returned HTTP 206 with an MP4 header at byte zero and another range at byte 1,048,576. It keeps season 9, position 13, its own thumbnail and its direct route. V4 hides the duplicate and its season. Main-season file `A7NRZuaf` was already absent upstream. All other 21 main-season identities matched. [Main edit list](https://pixeldrain.dev/api/list/ZQhstabS) · [Alternate list](https://pixeldrain.dev/api/list/kuoQYqfR)

**Egghead.** The published One Pace watch entries matched all 22 titles, file IDs and anime coverages, including *Kuma Bear* (1129–1130). The editors list the next six files by project and number. Their displayed titles now use the corresponding English anime headings. The editors assign 1136 to the sixth cut and document a brief post-credits insert from the 1137 recap. The development release records retain the editor's name for every file. The 15 reserved originals (1141–1155) also have sourced titles and still have no approved playback file. [One Pace releases](https://onepace.net/en/watch#egghead) · [Editors' release sheet](https://docs.google.com/spreadsheets/d/1ODt-b2L9PyAr0xEWhj1yIOqp__crBFVXhvejblvP_mU/edit) · [Kuma Cut notes](https://pixeldrain.dev/api/file/uctYEsF5) · [English title sources by episode](../metadata/egghead-original-anime-titles.csv)

The following title tags came from the MP4 movie metadata of the exact Shaved Egghead files. The leading numbers identify the release. Cloudstream supplies the navigation and the episode coverage separately. [Reviewed files and SHA-256 values](research/egghead-fan-edits.csv)

| File | Embedded title | Development display |
| --- | --- | --- |
| `usp6W28S` | Episode 1137+8 - Thank You, Daddy | Thank You, Daddy • Episodes 1137–1138 |
| `2A2TWcz8` | Episode 1139+40 - The Height of Folly | The Height of Folly • Episodes 1139–1140 |

**Other arcs.** All 249 legacy rows with accepted coverage matched the pinned One Pace subtitle titles and the Phase 4B release facts, including single numbers, gaps and overlaps. The independent Muhn catalogue agreed on the 248 corresponding entries it lists. Some companion metadata uses alternate English names, such as *The White Monster* for Marineford 11. Both the subtitle title and the Muhn listing support the current *Monster*. The unavailable Fish-Man Island file keeps its Phase 4B record. The 25 remaining legacy rows are the Wano exceptions above. [Reference facts](../metadata/phase4b/reference-facts.json) · [Published titles](https://github.com/one-pace/one-pace-public-subtitles/blob/76eb3421d97f25fa57a7b2db893b4cf36b417607/main/title.properties) · [Muhn catalogue](https://github.com/6ip/onepace-streams/blob/da83ea2664d74bde8b2624d9cea4856f286a07a7/meta/pp_muhnpace.json)

**Package verification.** CI passed 27 Python checks, the production and development Kotlin test suites, the Android build and the development package checks. The artifact download endpoint returned HTTP 403. The local packaging run reproduced the CI SHA-256, `d9660a046d135a6e0c98e4398833a04f6e48bb585da7ea89bcab45dd1bdbdc1d`, for the 116,992-byte CS3. Comparison with v3 found identical disassembled instructions in 337 baseline, catalogue and playback methods, including all 122 instructions of the provider's `loadLinks`. Fire TV validation of v4 remains pending. [CI run](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/44424798249) · [Development install files](https://github.com/lincutto/MuhnPaceCloudstream/tree/07d5120884b2a8662cd353bce4691094c8871f6f)

## Egghead Dev v3

Install or update **Muhn Pace Rework • Egghead Dev (by lincutto)** from the [Egghead testing repository](https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/egghead-test/repo.json). The development provider, plugin identity and series URL match v2. The source branch's older `dev-install/repo.json` also follows this index.

**Playback baseline.** The v2 publishing commit contains just the package and indexes. Its 107,052-byte package has SHA-256 `d431c4a2bbf46c00038d546a2fccd5f7332d86208238aecb80d6c7ee0601bf32`. The previous source tip still built v1. Reconstructing the v2 changes from the published DEX produced a byte-for-byte identical package at editable commit `b9b5d00c5c608279b2ca18d9c886b9a63bd638f4`. The user's Fire TV playback and 12:00 seek apply to that v2 package. [Published v2](https://github.com/lincutto/MuhnPaceCloudstream/commit/3fc8b8869f5c9968faa57d1bc2c2a419af6077c2) · [Recovered source](https://github.com/lincutto/MuhnPaceCloudstream/commit/b9b5d00c5c608279b2ca18d9c886b9a63bd638f4)

| Egghead positions | Edit | Original anime coverage |
| --- | --- | --- |
| 1–22 | One Pace Egghead 01–22 | 1086–1130, with overlapping scenes and individual gaps. Edit 12 uses 1108 and 1110–1111. |
| 23–28 | Kuma Cut | 1131–1136, one original episode per edit |
| 29–30 | Shaved Egghead | 1137–1138 and 1139–1140 |
| 31–45, unavailable | Reserved original-dub fallback | 1141–1155. The editors' dub stream list ends at 1140, later Shaved edits lack dub sync, and the original release has no verified direct native source in this provider. |

The [release table](research/egghead-fan-edits.csv) contains the per-edit coverage, pinned media hash, audio evidence, seek sample and synopsis. The official watch page confirmed all 22 standard English-dub identities and anime mappings. The community release sheet confirmed the remaining eight. The three alternate community edits overlapping One Pace 21–22 are excluded. The 29 additions cover material from anime episodes 1087–1140. [One Pace release guide](https://onepace.net/en/watch#egghead) · [Community release sheet](https://docs.google.com/spreadsheets/d/1ODt-b2L9PyAr0xEWhj1yIOqp__crBFVXhvejblvP_mU/edit)

On November 18 all 30 files matched the editors' list IDs and host hashes and accepted ordinary HTTPS byte ranges. For each addition, FFprobe inspected the MP4 track index and FFmpeg decoded an indexed interior H.264 frame. All had one default AAC track and 1920×1080 video. For the 15 tracks marked `und`, multilingual Whisper Tiny language identification found spoken English in at least two interior audio samples per file. Both Kuma files without individual dub filename tags also passed spoken checks. The adapter rechecks file info, SHA-256, host availability, and a 12-byte MP4 range on Play. Host denials stop resolution.

Each Egghead row has a native description and a distinct 480×270 JPEG from its own edit, pinned to the artwork commit. Indexed byte ranges supplied the stills. Image loading is independent of catalogue and playback resolution, so a failed image request leaves the episode selectable. Existing episode stills and descriptions are preserved. [Still provenance](../assets/egghead/manifest.json) · [Episode story reference](https://www.tvmaze.com/shows/1505/one-piece/episodes)

**Wano and catalogue titles.** The first Wano collection occupies development slots 1–26 and Act 3 occupies 27–51 in internal season 15. The fixed offset keeps a temporary missing file from shifting another slot. The other arcs keep their IDs, including Egghead at 17. Cloudstream's supported name-only selector uses `SeasonData(season, arcName, displaySeason = null)`. Its TV episode history key includes the internal season and episode, so the 25 former development season 16 entries can lose their saved resume associations. The parent URL, provider identity, all file routes and other episode keys are preserved. [Cloudstream season selector](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L587) · [TV episode ID calculation](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2246)

The development titles label the 249 legacy edits with accepted One Pace-derived coverage. Wano Act 3 retains its editorial story titles with **Anime episodes unverified** on all 25. Its previously displayed provisional numbers came from a different edit. [Legacy mapping evidence](PHASE4B_METADATA.md)

**Local regressions.** The frozen v7 catalogue yields all 274 original routes plus 30 Egghead rows. In the live 16 collections, 273 historical files matched their recorded names, IDs and hashes and served video to a range request. Fish-Man Island's “10,000 Meters Under the Sea” (`A7NRZuaf`, original episodes 526–527) has disappeared from `ZQhstabS` and its file and info endpoints return HTTP 404. The live development catalogue therefore has 303 playable rows at review time. This upstream removal leaves the next Fish-Man Island file at its existing index.

Run locally with JDK 17, Android 35, Gradle 8.12 and Python 3:

```sh
python3 -m unittest discover -s tests -v
gradle -PeggheadDev=true :MuhnPace:testDebugUnitTest :MuhnPaceEggheadDev:testDebugUnitTest :MuhnPaceEggheadDev:make
python3 scripts/assemble_egghead_dev.py
```

The final local run passed 27 Python checks and 58 Kotlin tests. Comparison with the v2 DEX found the same instructions across all 39 baseline and resolver classes and all 122 instructions of the provider's `loadLinks` method. The public JPEGs decoded with the expected dimensions and matched all 30 reviewed hashes.

**Publication.** CI built source `8bd211cecb2f455a2da1415010866b3e59a9ae3e`. The live repo, index and 115,344-byte package match the three release files at `0b69b0085dc7f639d6e282b3dfee053a41759f14`. The CS3 SHA-256 is `7c11caaf8d536e5180f7561463310a0cfe5fd5ba939f44c10cdf8a0f9fc3ab99`. [Successful Actions run](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/44415733011) · [Published files](https://github.com/lincutto/MuhnPaceCloudstream/tree/0b69b0085dc7f639d6e282b3dfee053a41759f14)

The Actions artifact storage download returned HTTP 403 from the development machine. Setting the local CS3's two ZIP entry modes to the runner's 0644 reproduced the package checksum in the CI job log. Packaging those three release files also reproduced the Actions archive digest `d876216b3323558a5eab7168fe00d42b64d9160f40ac2219e7222e885ec1118d`. The source branch's `dev-install/` copy matches this published release.

## Historical v1 investigation

The following records describe v1, its installation and the investigations leading up to v2.

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

### Egghead fan-edit files and playback

The [file map](research/egghead-fan-edits.csv) matches each of the first 30 development positions to the editors’ release, original anime episodes, manga chapters, Pixeldrain list and file IDs, filename, and host-published SHA-256. The 22 One Pace rows use the standard 1080p English Dub links in official release order. The final eight rows use the community editors’ dub list and release sheet. All 30 files reported an available, downloadable MP4. Kuma 1134 and 1135 have a collection-level dub label only. [One Pace catalogue](https://onepace.net/en/watch#egghead) · [Community release sheet](https://docs.google.com/spreadsheets/d/1ODt-b2L9PyAr0xEWhj1yIOqp__crBFVXhvejblvP_mU/edit)

**S17E01, New Emperors.** The normal Download button on the editor-linked Pixeldrain viewer delivered file `HZ9VRHCV`. Its 333,620,890 bytes matched the published SHA-256. FFprobe found 1920×1080 H.264 video and exactly one audio track, default stereo AAC tagged `eng`. The `jpn` tag belongs to the video stream. The file runs 24:53.42. FFmpeg decoded video and audio at 02:00 and 24:00. An offline multilingual spoken-language check (sherpa-onnx 2.0.1, Whisper Tiny) identified English in all three 20-second samples starting at 03:00, 12:00 and 21:00. Only this file received a media inspection. [Release and anime coverage](https://onepace.net/en/watch#egghead-01) · [File details](https://pixeldrain.dev/api/file/HZ9VRHCV/info)

The team’s streaming guide links to Pixeldrain’s viewer. Its 1080p extended-cut list points to the same file for episode 01. All three standard English encodes of this cut, 480p `QfZo4WQu`, 720p `eFC3gg56`, and 1080p `HZ9VRHCV`, specify `onepace.net` as the sole allowed embed domain. The project also publishes an all-in-one MKV for download. The reviewed editor resources describe viewer streaming and downloaded-file playback. I found no supported handoff for streaming this MP4 directly in a third-party native player. [Team streaming guide](https://docs.google.com/document/d/1jmoVWR--eCtDTnflw2xN5AGdPQPhKdNRMadT_ln-lnY/edit) · [Extended collection](https://pixeldrain.dev/api/list/BiCB3xar) · [Viewing FAQ](https://www.reddit.com/r/onepace/comments/1e93kko/frequently_asked_questions/)

Pixeldrain defines off-site direct file access as hotlinking. Its protection can require a CAPTCHA when neither uploader nor downloader has premium. The embed allowlist controls websites that can embed the file. The public file details show historical bandwidth usage and provide no confirmation that the uploader supports this extension’s anonymous native traffic. To enable the first cut, obtain one editor-approved HTTPS playback route for that file with host support for use in Cloudstream. The current Pixeldrain adapter declines files with an embed allowlist. [Pixeldrain hotlink policy](https://pixeldrain.net/about) · [Pixeldrain API](https://pixeldrain.dev/api)

## Catalogue and playback

The release metadata reserves 45 stable Season 17 positions across anime episodes 1086–1155. The player currently exposes **zero** of these positions. Season 18 is deferred.

One Pace’s edits follow scene order. Neighbouring cuts can contain different scenes from the same anime episode. Edit 12 uses 1108 and 1110–1111. One Pace 21–22 already cover material in the community list’s 1127–1130 entries. Those entries are excluded. One Pace 22 ends at manga chapter 1096, followed by Kuma 1131–1136 (1097–1102) and the two Shaved cuts (1103–1106). The editors put new footage from the 1137 recap after the 1136 credits and list Shaved 1137–1138 v2 as the replacement for the earlier Kuma cut. The handoff follows the editors’ order. Its scene-by-scene continuity still needs a viewing check.

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
