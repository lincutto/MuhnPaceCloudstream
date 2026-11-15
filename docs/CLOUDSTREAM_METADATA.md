# Phase 3: Cloudstream playback and history API

Inspected 2026-11-15. This is source/API analysis, not a Fire TV test.

## Exact dependency and source

The extension still declares `com.github.recloudstream.cloudstream:library:-SNAPSHOT`.
Gradle `:MuhnPace:dependencyInsight --dependency com.github.recloudstream.cloudstream --configuration debugCompileClasspath`
resolved both `library` and its Android variant to **`-SNAPSHOT:5d66bbeaf3-1`**.
The compiled Android AAR SHA-256 is `2770b53b5675a774be31cc4cd07edac2fb6ccea93c339091dc2c52c5f8b89819`.
`javap` inspection of its real `Episode` and `TvSeriesLoadResponse` classes confirms the fields used below.
The upstream source inspected is [Cloudstream commit 5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c](https://github.com/recloudstream/cloudstream/tree/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c).
The user's installed Cloudstream Beta revision has not been identified; its UI may differ.
No Cloudstream source was modified, and no dependency was added or upgraded.

## Provider fields and their consumers

| Provider field | Actual host use | Phase 3 behaviour |
| --- | --- | --- |
| `TvSeriesLoadResponse.name` | Result-page title, `ResultEpisode.headerName`, player header, pause-overlay title, cached Continue Watching series name | `One Piece • Dressrosa (Muhn Pace)`; corresponding labels for all 16 collections |
| `SearchResponse.name` | Home/search card title | Same series label as the load response |
| `LoadResponse.plot` | Result-page plot **and TV pause-overlay overview** | Existing arc synopsis, preceded by available-edit count, English dub, and explanation that player numbers are Muhn Pace edits |
| `LoadResponse.tags` | Pause-overlay metadata row and result tags | Existing Anime/Action/Adventure/Fantasy tags |
| `LoadResponse.posterUrl`, `backgroundPosterUrl` | Series artwork; cached series poster on Continue Watching | Existing verified arc art and fallbacks unchanged |
| `Episode.name` | `ResultEpisode.name`, player control title, episode selection and result-page resume label | Existing formatter unchanged: unknown `Dressrosa • Muhn Pace 25`; verified `One Piece <range> • Dressrosa 25` |
| `Episode.description`, `posterUrl` | Rich episode rows, including the player's episode-selection list | Existing factual description, explicit unknown mapping, exact-file thumbnail or arc fallback |
| `Episode.episode`, `season` | Host numeric labels, sorting, navigation and history ID | Always the existing edit number and season 1; never original anime numbers |
| `Episode.data` | Input to `loadLinks` | Existing Pixeldrain file URL, with no serialized display metadata |
| `LoadResponse.uniqueUrl`, `url`, `apiName`, `type` | Series identity, loading, history lookup and episode-ID calculation | Existing inputs preserved, including legacy `.com` URLs and `TvType.TvSeries` |

The library provides no distinct arc, original-anime range, fan-edit number, player-title override or Continue Watching subtitle field. `syncData` is not used to import standard One Piece episode metadata onto fan edits. Changing to `AnimeLoadResponse` would change the history calculation; the provider remains a TV-series response.

## Player controls versus delayed paused overlay

[GeneratorPlayer.getPlayerVideoTitle/setTitle](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1877)
combines the header, localized season/episode label, and episode name. For English UI and Dressrosa edit 25, the resulting control title is:

```text
One Piece • Dressrosa (Muhn Pace) "S1:E25" - Dressrosa • Muhn Pace 25
```

The host adds `S1:E25`; it means **Muhn Pace edit 25**. The provider cannot independently remove it or substitute original episode 684 without changing identity inputs. Some arc/brand repetition is unavoidable while preserving the standalone episode formatter and a useful arc-specific Continue Watching name. We keep the short existing formatter and omit filenames, resolutions, story-title guesses and source citations from title lines. The TV control title has a 600dp maximum width and two lines; the user's title-length setting can truncate or hide it. Readability cannot be guaranteed without the device.

The separate [showPlayerMetadata overlay](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1631)
uses **load name, tags, year/score if supplied, numeric season/episode, and load plot**. It does **not** use the current episode's name, description, thumbnail or original mapping. We supply no unsupported year, score or logo. A future verified range in `Episode.name` would reach the controls and episode list, but would still not become a separate field in this delayed overlay.

[FullScreenPlayer.scheduleMetadataVisibility](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/player/FullScreenPlayer.kt#L180)
shows it after eight seconds paused without interaction, when **Show Player Metadata Overlay** is enabled, no dialog is open, and layout is not PHONE. It then hides the controls. The [TV layout](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/res/layout/player_custom_layout_tv.xml#L12) limits the title to two lines and overview to five. This is why the edit-number explanation now precedes the synopsis. The synopsis itself is retained unchanged; overflow and visibility belong to Cloudstream.

## Continue Watching and resume

[HomeViewModel.getResumeWatching](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/home/HomeViewModel.kt#L62)
reads the cached series header and saved position. Its `ResumeWatchingResult` contains the series name/URL/provider/type/poster, position/duration, parent/episode ID, season and episode. It has **no episode name, description or original-range slot**.

[SearchResultBuilder](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/search/SearchResultBuilder.kt#L254)
binds the saved progress bar and numeric season/episode label. After Cloudstream reloads the series and refreshes its header cache, the card can show `One Piece • Dressrosa (Muhn Pace)`, the original arc poster, `S1:E25` (localized/layout-dependent) and existing progress. Previously cached cards can retain the old display name until that refresh. The extension does not rewrite stored history or force a refresh.

The [result-page TV resume control](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultFragmentTv.kt#L479)
does receive the current `ResultEpisode.name`, so its label can include the clearer episode title alongside saved progress. The player uses the same episode rows for navigation. Existing sorting, gaps, next/previous selection and resume thresholds remain host behaviour. Android TV launcher Watch Next integration is also host-controlled; Fire OS launcher support is not established here.

## Identity and progress preservation

[Series identity](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L370)
comes from `uniqueUrl` with the provider URL removed, slashes removed, then string hash. The TV-series [episode-ID calculation](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2239) is `mainId + season * 100000 + episodeIndex + 1`.
Neither name nor plot participates. Cloudstream [stores position/duration by that ID and updates last-watched state itself](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/utils/DataStoreHelper.kt#L700).

Tests exercise real library `Episode`, search/home and `TvSeriesLoadResponse` builders. They verify all 274 baseline data URLs, explicit numbers, season 1, collection URLs/unique URLs, type/provider identity, posters/backgrounds, plot and tags, including legacy `.com` load URLs. These check the inputs to the host's history algorithm; they do **not** execute Android preferences, migrate a user's history, or simulate Fire TV playback. `loadLinks` and its URL validator remain byte-for-byte unchanged from `d79c417`.

Device acceptance remains: verify the control title, delayed pause overlay, five-line overview truncation, episode-selection layout, card refresh, retained progress and resume position on Cloudstream Beta / Fire TV Stick 4K (2nd Generation), Fire OS 8. No reset or history deletion should be used for this check.
