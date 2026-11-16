# Episode numbering investigation — 2026-11-16

**Episode-list numbering: Client limitation. Paused-player numbering: Client limitation.**
No supported provider-only fix exists in the inspected Cloudstream 4.9.1-PRE implementation. Achieving title-only rows and a title-only player header requires a Cloudstream client change. No runtime workaround, metadata renumbering, client modification, or new preview publication was made.

## Revision and scope

The user reports **4.9.1-PRE**. Fresh upstream fetches resolved `master` and the rolling `pre-release` tag to [`5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c`](https://github.com/recloudstream/cloudstream/tree/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c). `gradle/libs.versions.toml:72` specifies `4.9.1`; `app/build.gradle.kts:178` adds `-PRE`. This is also the library revision used by the validated v3 preview. The `v4.9.1` release tag (`c92e0cf02eda0429fbb4b2fcea4b528cb65aa9fb`) was independently fetched: both row expressions and the player-title construction have the same behaviour there. The shared version label does not identify the user's exact rolling Beta APK/build code. No Fire TV connection or device execution was available.

Development baseline: `750f218a00cf3427d4dc79c3aa1cc96721c0f1f6` on `codex/v3-overhaul`. Validated published preview source: `ffc1f51d98916c1ac4fa60f6a67e27c2df0da738`. Preview branch: `1dcb77c446a763adcad74e01ac71403192243975`, version 3, package SHA-256 `43c3704007b0ffab045109c2c1f08d1adfe73ff343c44cd97b63d1edbd9a090c`. The later development source changes only the home-row heading; episode builders and metadata are unchanged from the published preview.

## Exact episode-list rendering path

1. `MuhnPaceProvider.episodeFromMetadata` assigns `Episode.name = metadata.title` and retains the source edit number. `seriesFromFiles` assigns the established season and stable navigation index. Dressrosa's origin is 1, so edit 44 is navigation episode 44. Other seasons with non-one origins retain their existing offset indices.
2. [`ResultViewModel2.kt:2243–2285`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2243) sorts TV-series episodes, assigns `episodeIndex = episode.episode ?: (index + 1)`, and passes that number and `filterName(episode.name)` to `buildResultEpisode`. `filterName` at line 610 does not remove a story title or control the numeric prefix.
3. [`ResultFragmentTv.kt:432`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultFragmentTv.kt#L432) uses `EpisodeAdapter`. The in-player episode chooser uses the same adapter at [`GeneratorPlayer.kt:2144`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L2144).
4. Both the rich and compact row branches in [`EpisodeAdapter.kt:205–209`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/EpisodeAdapter.kt#L205) and [426–430](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/EpisodeAdapter.kt#L426) assign:

```kotlin
val name =
    if (item.name == null) "${episodeText.context.getString(R.string.episode)} ${item.episode}" else "${item.episode}. ${item.name}"
episodeText.text = name
```

There is no provider/type/preference condition around the prefix. Switching row layouts or removing artwork/descriptions does not suppress it. With `episode = 44` and `name = "Heaven and Earth - 730-733"`, this assignment necessarily displays `44. Heaven and Earth - 730-733`. This is verified source behaviour, not a claim of device testing.

## API, settings and other providers

The complete [`Episode` fields at MainAPI.kt:2550](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/library/src/commonMain/kotlin/com/lagradost/cloudstream3/MainAPI.kt#L2550) are data, name, season, episode, posterUrl, score, description, date and runTime (plus deprecated rating). There is no displayEpisode, hideEpisodeNumber, or player-title override. `LoadResponse` (1812), `EpisodeResponse` (2233), `TvSeriesLoadResponse` (2673), `AnimeLoadResponse` (2318), and MainAPI expose no row-number suppression. `SeasonData.displaySeason` (2225) controls season labels only; it cannot remove the episode prefix.

Reviewed both legacy XML and Compose settings implementations. The adapter has no number-visibility setting. The player's `prefer_limit_title_key`/`limitPlayerTitle` setting only hides the entire title or truncates its end; it cannot retain just the episode name. The separate resolution/source/title options affect `playerVideoTitleRez`, not the quoted S/E title.

Existing official providers were inspected at [`recloudstream/extensions` commit cf16b3b45cda911a36ddca69f9acbe4763c6a4b1](https://github.com/recloudstream/extensions/tree/cf16b3b45cda911a36ddca69f9acbe4763c6a4b1). [YouTube playlist episodes](https://github.com/recloudstream/extensions/blob/cf16b3b45cda911a36ddca69f9acbe4763c6a4b1/YoutubeProvider/src/main/kotlin/recloudstream/YoutubeProvider.kt#L275) supply only name/poster/data, leaving episode numbers null. They still pass through the client's fallback numbering and unconditional adapter prefix. [Internet Archive](https://github.com/recloudstream/extensions/blob/cf16b3b45cda911a36ddca69f9acbe4763c6a4b1/InternetArchiveProvider/src/main/kotlin/recloudstream/InternetArchiveProvider.kt#L322) supplies extracted season/episode numbers for playlists. YouTube single-video and Invidious movie responses avoid an episode list altogether; they are not a title-only, sixteen-season equivalent. No supported unnumbered episode-list implementation was found in that provider set. This is not an assertion that every third-party repository was searched.

Changing to Anime still uses numbered ResultEpisodes and changes identity rules. Movie/live responses cannot preserve the current season list and TV-series episode history. Mislabeling the response as a non-episode media type could bypass the player's `isEpisodeBased()` test but cannot bypass the adapter's prefix and misrepresents the series. None of these is an acceptable fix.

## Exact paused-player title path

[`GeneratorPlayer.getPlayerVideoTitle():1877–1920`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1877) reads `ResultEpisode.headerName`, `name`, `episode`, `season`, and `tvType`. For an episode-based type and non-null season it joins:

```text
headerName + " \"" + localized S + season + ":" + localized E + episode + "\"" + " - " + name
```

The English resources define `season_short = S`, `episode_short = E`. `setTitle()` assigns the result to `playerVideoTitle` at line 1945. Consequently the unchanged inputs produce:

```text
One Piece (Muhn Pace) "S12:E44" - Heaven and Earth - 730-733
```

`One Piece (Muhn Pace)` is provider-supplied **series** metadata (`LoadResponse.name`), not text embedded in `Episode.name`. Cloudstream unconditionally reuses it as the header. The quotes, `S12:E44`, separators and composition are client-generated. There is no player-only series-header override. Emptying/renaming the series would affect catalogue/history labels and still leave the S/E prefix; it would not achieve the requested title. No unnecessary prefix exists in the episode name to remove.

The separate delayed paused metadata panel at [`showPlayerMetadata():1631`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1631) uses `load.name`, short season/episode text, tags and `load.plot`. Neither descriptions nor playback controls were changed or disabled.

## Identity and playback consequences

The TV-series conversion sorts by `(season * 10000) + episode`, uses `episode ?: index + 1`, and deduplicates by `mainId + season * 100000 + episodeIndex + 1`. `ResultViewModel2.getId():370–378` derives mainId from uniqueUrl and the provider's mainUrl. [`buildResultEpisode():83–128`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultFragment.kt#L83) reads position and watched state using that ID; [`DataStoreHelper:702`](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/utils/DataStoreHelper.kt#L702) stores position by ID.

Nulling numbers therefore does not hide them and makes fallback IDs depend on list position. Zeroing/repeating numbers can collapse entries as duplicates. Original-anime numbers change sorting/history and may collide where ranges overlap. Names alone do not affect IDs, but changing already-clean names cannot alter the client's prefix. Direct playback URLs are separate `Episode.data` inputs to `loadLinks`; all 274 are preserved and exercised by the regression test. No history migration, reset or deletion is needed or performed.

## Verification and changes

Only tests, their frozen fixture, and this investigation record were added; runtime Kotlin, metadata, package version and publishing configuration remain unchanged.

- `MuhnPace/src/test/resources/v3-episode-contract.json`: frozen 274-row acceptance contract from the existing reviewed catalogue and published range labels, with file ID, season, source edit, navigation index and full expected name.
- `MuhnPacePreview/src/test/kotlin/com/muhnpacepreview/EpisodeTitleContractTest.kt`: real generated preview provider builders, exact names and ordering for all 274 entries/16 seasons, legacy edit numbers, parent/provider/type identity, host ID inputs, descriptions, and every playback callback URL. Rejects manually embedded numeric prefixes and invisible formatting characters. Explicitly checks Dressrosa 43–45.
- `tests/test_episode_title_contract.py`: independently cross-checks every frozen row against the original catalogue snapshot and reviewed metadata/reference facts.
- `docs/EPISODE_NUMBERING.md`: fresh source evidence and limitations, superseding any interpretation that clean metadata alone removes client labels.

All 274 underlying names contain only their accepted story title and range where available; none includes a manually prepended Muhn Pace index. **249 ranges are existing ONE_PACE_DERIVED evidence, not newly frame-verified coverage. The 25 late-Wano entries retain editorial story labels and unknown ranges.** This task does not invent ranges or strengthen their provenance.

Validation results are recorded below after execution. Metadata tests do not prove title-only UI rendering. Source inspection proves the opposite for the inspected client. Exact installed APK matching, Fire TV layout/truncation, playback and persisted resume remain device checks; no visual fix is claimed. A future client change would need to separate display preferences from numeric identity and apply them to both row bindings and player-title construction. This task stops at that API boundary.

### Executed validation

- `python3 -m unittest discover -s tests -v`: **25 passed**.
- `gradle -Ppreview=true :MuhnPace:testDebugUnitTest :MuhnPace:make :MuhnPacePreview:testDebugUnitTest :MuhnPacePreview:make --no-daemon`: **BUILD SUCCESSFUL**, 44 base + 5 preview Kotlin/JUnit tests, zero failures/errors/skips; both Android DEX/CS3 builds passed.
- `scripts/publish.py` assembled the base package locally only. `scripts/assemble_preview.py` validated the local preview manifest/classes/routes, DEX, URLs, SHA-256 and size. These commands did not publish anything remotely.
- Compiled-library `javap` confirms the Episode API inspected in source; resolved Android AAR SHA-256 remains `2770b53b5675a774be31cc4cd07edac2fb6ccea93c339091dc2c52c5f8b89819`.
- The client source was freshly read at both pinned revisions described above. The rendered-string assignments produce numbered rows and the S/E player header; **no title-only rendering was verified or claimed**.

Publication decision: **no new preview published**, because no supported visual fix was implemented. Existing installation URL remains `https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json`. Production `main` and `builds` remain untouched.
