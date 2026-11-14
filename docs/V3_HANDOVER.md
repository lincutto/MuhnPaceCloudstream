# MuhnPaceCloudstream v3 — handover

Updated: 2026-11-14 (UTC). Phase 1 is complete. Phase 2 is implemented with original thematic artwork as the deliberate fallback for every collection. Phases 3 and 4 have not begun. No extension or environment was published.

Phase 1 is complete, with unsupported original-episode mappings explicitly left unknown as requested. No extension was published.

## Phase 2 delivery

The user supplied photographs of Cloudstream Beta on an Amazon Fire TV Stick 4K (2nd Generation), showing grey arc cards, sparse pages, raw episode filenames and existing watch progress. These show the published provider, not a device test of the development branch. Phase 1's clean titles are preserved.

| Commit | Verified change |
| --- | --- |
| `be72443` | Exact-edit thumbnails and factual episode descriptions; all 12 Phase 1 Kotlin tests plus 5 new tests pass |
| `6e6267c` | Distinct synopses for all 16 collections, live edit counts, anime/adventure tags, supported artwork fields and fallback hooks; 20 Kotlin tests pass |
| `f3e3a8e` | Original geometric artwork: 16 labelled portrait posters, 16 widescreen backgrounds, editable SVGs, PNG exports and reproducible authoring/verification scripts |
| `0c792b0` | Connect verified immutable artwork URLs; replace eight known black thumbnails with matching arc art; verify real home/search responses and all artwork mappings; 23 Kotlin and 12 Python tests pass |

All sixteen collections have a **768×1152 poster and 1920×1080 background**. These are original, geometric arc-themed illustrations, intentionally used as fallback artwork. They are not official anime promotional images or a claim that source-image licensing was obtained. No third-party image assets, logos or font files were committed. See [artwork provenance and regeneration](../assets/artwork/README.md), [the original drawing code](../scripts/generate_artwork.py) and [the collection manifest](../assets/artwork/catalogue.json).

| Collection | Original artwork motif |
| --- | --- |
| Enies Lobby | Judicial island tower and gates |
| Post-Enies Lobby | Water Seven canal, bridge and town |
| Thriller Bark | Castle ship beneath the moon |
| Sabaody Archipelago | Mangrove grove and bubbles |
| Amazon Lily | Mountain temple |
| Impel Down | Submerged prison gate |
| Marineford | Navy fortress and waves |
| Post-Marineford | Straw hat and compass |
| Fishman Island | Underwater palace and coral |
| Fishman Island edit 15 special | Same arc motif, separate palette and corrected-group-shot label |
| Punk Hazard | Fire, ice and research site |
| Dressrosa | Colosseum and hilltop palace |
| Zou | Forest carried above the sea by an elephant |
| Whole Cake Island | Cake castle and candy trees |
| Wano Acts 1 & 2 | Mountain, torii gate and pagoda |
| Wano Act 3 | Horned island silhouette and festival lanterns |

The PNG URLs are pinned to artwork commit `f3e3a8e88abb6d7792daf95a6ef3802e18112a63` on `raw.githubusercontent.com`, independent of `main`, `builds` and releases. All **32 HTTPS URLs** were fetched and decoded successfully: HTTP 200, `image/png`, correct dimensions, and SHA-256 identical to the committed assets. Evidence is in `MuhnPace/src/test/resources/artwork-verification-2026-11-14.json`; `scripts/verify_artwork.py` repeats the check when needed. The responses advertised `Cache-Control: max-age=300`; stable URLs let Cloudstream reuse its image caches. All sixteen posters and a representative Dressrosa background were inspected locally. This is asset inspection, **not Fire TV testing**.

Fallback order: configured poster → original thematic poster; configured background → chosen poster; missing/mismatched/known-black episode image → chosen arc background/poster. Network failures after an image is handed to Cloudstream are handled by the host app; `Episode` metadata provides no custom on-error hook. An unavailable GitHub or Pixeldrain host can still prevent image loading. No image prefetch or additional per-image metadata/HEAD request is made during browsing.

External promotional-image research was restricted: requests to `one-piece.com`, `en.wikipedia.org` and `image.tmdb.org` received proxy HTTP CONNECT 403. The original illustrations avoid depending on those sources. A network draft proposing `one-piece.com`, `en.wikipedia.org`, `upload.wikimedia.org`, `www.themoviedb.org` and `image.tmdb.org` was saved while investigating access; it remains **unapplied and unpublished**. No network change is required for the delivered artwork. Those sources could be considered for a separately approved replacement later, with permissions, URLs and matching checked first.

No original anime arc ranges or edit-level mappings were invented. The optional arc-coverage field reuses `OriginalEpisodeMapping`, is explicitly labelled as broad arc context, and remains unset without verified evidence.

### Supported Cloudstream fields and implementation

The real library API was inspected at Cloudstream commit `5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c` and the implementation compiled against the project's actual dependency:

- Search/home cards: `SearchResponse.posterUrl`.
- Arc pages: `TvSeriesLoadResponse.posterUrl`, `backgroundPosterUrl`, `plot`, and `tags`.
- Episode rows: `Episode.posterUrl` and `description`. Cloudstream's `EpisodeAdapter` chooses its richer row when a poster or description exists and binds these fields. TV layouts still belong to Cloudstream, not the extension.
- Cloudstream's Coil image loader enables memory, disk and network caching. The extension hands it stable URLs and does not prefetch images or add per-image metadata/HEAD requests during browsing.

Presentation extends the existing `EpisodeMetadata` with nullable thumbnail and factual-summary fields. `withPresentation` returns an enriched copy without changing file ID, collection ID, edit number, edition, original mapping or clean title. `ArcPresentation.kt` keeps editorial descriptions and artwork configuration separate from streaming and the existing sixteen collection identities.

Every collection now has a distinct self-authored, premise-only synopsis, including the Fishman Island corrected-group-shot special and both Wano parts. Counts come from the live selected episode list rather than a fixed total. Tags are Anime, Action, Adventure and Fantasy; no unsupported ratings, dates or cast data are added.

Example Dressrosa page text:

> The Straw Hats visit Dressrosa, where living toys share the streets and a colosseum attracts fighters from across the seas. Their mission brings them into conflict with the kingdom's powerful ruler.
>
> 48 Muhn Pace edits available • English dub. Numbers below identify Muhn Pace edits.

Example edit 25 keeps the title `Dressrosa • Episode 25` and now describes only known facts:

> Dressrosa • Muhn Pace edit 25. English dub.
> Original One Piece episode mapping is unverified.

There is no verified edit-specific story synopsis. Do not copy an official single-episode description onto a multi-episode fan edit or infer the story from the arc synopsis.

### Thumbnail source, verification and limitations

The [Pixeldrain API](https://pixeldrain.dev/api) documents `/file/{id}/thumbnail?width=x&height=x`. The implementation accepts only a `thumbnail_href` matching the exact selected file (`/file/<selected-id>/thumbnail`) and uses `https://pixeldrain.dev/api/file/<selected-id>/thumbnail?width=256&height=144`. Missing or mismatched links use the configured arc background. Stream URLs and the `loadLinks` implementation remain unchanged.

All **274** selected files' thumbnail URLs were fetched on 2026-11-14: HTTP 200, `image/png`, successfully decoded at 256×144. Their source references, URLs, dimensions, response cache policy and SHA-256 are recorded in `MuhnPace/src/test/resources/thumbnail-verification-2026-11-14.json`. Source preview images themselves are **not** committed. These are remotely hosted previews of the existing edit files, not original-anime mappings or a claim of redistribution rights.

Decoded pixel checks identified **eight black previews** (maximum RGB channel 0 or 1): Enies Lobby edits 11–15, Impel Down edit 1, and Post-Marineford edits 7–8. Presentation uses the matching original arc background for those file IDs. The other **266** selected edits use their exact-file source previews. The evidence includes pixel maxima and blank-frame flags. Review this list if the source regenerates its thumbnails; it is not a runtime image-analysis or probe feature.

Pixeldrain allows dimensions only in multiples of 16 up to 256. A 1280×720 request was rejected with HTTP 400. The thumbnails are suitable for small rows, not HD backgrounds. Inspected examples include opening-credit imagery, so multiple edits may look similar and spoiler freedom is not guaranteed. The API may return a generic file icon when it cannot generate a thumbnail. Successful responses carry `Cache-Control: public, max-age=31536000`; Cloudstream controls its own cache and rendering.

### Phase 2 validation

The final implementation at `0c792b0` passed locally:

- **23 executable Kotlin/JUnit tests**, zero failures/errors: all 12 Phase 1 regressions, five episode-presentation tests, three synopsis/fallback tests and three artwork tests. The latter exercise actual Cloudstream home/search responses, preserved collection identities/order, all sixteen poster/background assignments, and per-arc episode fallback selection. Tests use the real Cloudstream library.
- **12 Python checks**, including artwork export/hash/provenance consistency and the existing workflow publication-boundary checks.
- Android Kotlin compilation, test compilation, DEX compilation and `:MuhnPace:make` succeeded. The home/search coroutine dependency is test-only.
- All **32 arc-art URLs** and **274 source-preview URLs** independently fetched and decoded; offline regression tests use the saved proof, not live network requests.
- Package ZIP/DEX, provider/plugin identity, manifest version `2`, generated indexes, size and hash passed the same assertions as CI. The local package SHA-256 was `5aab5e91002cd0252057eb8f58aa4df8ba31fb611fb6b8bbb086e335ff27c945` (not a reproducible-build claim).
- `loadLinks` and its file-URL validation are byte-for-byte unchanged from the completed Phase 1 source. The baseline tests still verify all 274 playback URLs, explicit episode numbers and season 1.

Final integration [validation run 43593647093](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43593647093) **succeeded** for `0c792b03cceeebdbb446263af017d87daecaa6dc` in **1m 34s**. Its successful `validate` job produced both `MuhnPace-tests-0c792b03cceeebdbb446263af017d87daecaa6dc` and `MuhnPace-validation-0c792b03cceeebdbb446263af017d87daecaa6dc` artifacts. The SHA, conclusion and artifacts were checked on the actual GitHub run page.

Earlier incremental CI succeeded: [43591490583](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43591490583) for `be72443` (1m 27s), [43591948009](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43591948009) for `6e6267c` (1m 26s), and [43592230918](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43592230918) for the progress handover `39c09c4` (1m 24s). Each produced test and package artifacts.

Real-device verification remains: confirm poster/background loading and legibility, episode thumbnail/description layout, remote navigation, cached artwork refresh, and retained resume position on Cloudstream Beta / Fire TV Stick 4K (2nd Generation). No updated device screenshot, playback session or visual acceptance test has been performed. The published installation will remain unchanged until a later explicitly approved release.

### Later requirements preserved, not implemented

- **Phase 3:** improved paused-playback titles; verified original One Piece episode numbers/ranges; proper anime/arc metadata during playback; better Continue Watching presentation.
- The user is currently at **original One Piece episode 684 (Dressrosa)**. The photographed history includes Muhn Pace edit 25. There is **no verified equivalence** between those positions; do not infer one. Continue showing the Muhn Pace edit number until evidence exists.
- **Phase 4:** automatic/manual intro skipping using supported Cloudstream functionality and verified timestamps for the actual edit, without skipping story content.
- Updated Fire TV layouts, artwork rendering, remote navigation, persisted history and playback-overlay behaviour require a later real-device test. No updated visual/device verification is claimed here.

## Phase 1 recovery

- The terminal and the published environment's JDK 17, Gradle 8.12 and Android API 35 toolchains work.
- The initial checkout was the clean `work` branch at `8ade241e95405d91c0606bd3ea6080ac0709992a`. There were no tracked/untracked edits or stashes to rescue.
- Fetched `codex/v3-overhaul`; its latest remote commit was `f9209a99a958441891b797c0d0e8a7fc876fcbfe`. Work resumed on a local branch of that name. No previous metadata implementation or `docs/V3_HANDOVER.md` existed in that revision.
- `f9209a9` changed only `.github/workflows/build.yml` and added `validate.yml`. It restricted production to `main` and added development validation.
- [Validation run 43538548891](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43538548891) **succeeded**, for `f9209a9`, in 1m 38s. Verified on its GitHub run page, including the successful `validate` job and artifact. This was an existing build/structural-check run; it did not execute the new Kotlin tests.
- Git fetch, dry-run push, and the actual development-branch push succeeded. The GitHub REST CLI could not reach `api.github.com` because the environment proxy denied that destination (HTTP CONNECT 403). Run conclusions were checked on `github.com`; CLI account authentication/log retrieval was not claimed.
- Reused the sixteen `/tmp/muhn-list-*.json` snapshots from 2026-11-14 00:55 UTC. Those files were preserved. The saved conversation concerned the earlier prototype and contained no verified edit-to-original-episode table. The user confirmed that unsupported mappings should remain unknown.

## Delivered changes

| Commit | Change |
| --- | --- |
| `635650e` | Structured metadata, clean titles, numeric sorting, Kotlin tests and saved compatibility fixtures |
| `8acd626` | Kotlin tests required in CI, publication-boundary checks, retained test reports, clearer local assembly output |

`EpisodeMetadata.kt` separates collection ID, file ID, original filename, edit number, display title, edition, and nullable original-episode mapping. `MuhnPaceProvider` converts that metadata into real Cloudstream `Episode` objects. Titles use the catalogue arc name and edit number; alternate, filler and fixed-group-shot editions retain a readable qualifier.

Selection still prefers the first regular edit, falling back to the first listed variant. Sorting is numeric, with existing gaps and non-one starting numbers retained. Subtitled-only entries, comparison clips, notes, malformed IDs, and unsupported filename formats are excluded. The parser matches the known arc name before the edit number, so version, resolution, and unlabelled bracket ranges cannot become episode numbers.

No guessed story titles or original anime numbers were added. The built extension's manifest version remains `2`; these changes are unpublished development work.

## Identity and compatibility

Preserved:

- Provider name: `Muhn Pace Direct (English Dub)`.
- Plugin class: `com.muhnpacedirect.MuhnPacePlugin`; provider class: `com.muhnpacedirect.MuhnPaceProvider`; internal extension name: `MuhnPace`.
- Main URL: `https://pixeldrain.dev`; all sixteen collection IDs, catalogue titles, search/load URLs and series type remain unchanged. Legacy `pixeldrain.com` collection/file URLs are still accepted as before.
- Playback data: `https://pixeldrain.dev/api/file/<fileId>` with the same selected file IDs; `loadLinks` is unchanged.
- Episode numbers and `season = 1`. Metadata and titles are not serialized into playback data.

Cloudstream's [TV-series history-ID calculation at 5d66bbe](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2239) uses the series ID, season, and explicit episode number. The series ID derives from `uniqueUrl` and the provider's URL. These inputs remain unchanged; title cleanup and numeric sorting do not substitute new history identifiers. The compatibility test constructs real Cloudstream episodes and checks all 274 selected URLs, episode numbers and seasons against the recovered baseline.

The source snapshot has 290 list entries, including notes, variants and non-dub material. It produces these 274 selected episodes:

| Collection | List ID | Selected | Edit numbers |
| --- | --- | ---: | --- |
| Enies Lobby | `6VyLVkB2` | 12 | 11–22 |
| Post Enies Lobby | `jVhFqouN` | 2 | 4–5 |
| Thriller Bark | `NwJKTg21` | 22 | 1–22 |
| Sabaody Archipelago | `KztkSCuY` | 11 | 1–11 |
| Amazon Lily | `TuZJgwRL` | 5 | 1–5 |
| Impel Down | `4AN9Xxht` | 10 | 1–10 |
| Marineford | `oa8oZvZx` | 17 | 1–17 |
| Post Marineford | `MH7AQkgr` | 2 | 7–8 |
| Fishman Island | `ZQhstabS` | 22 | 3–24 |
| Fishman Island • Episode 15 | `kuoQYqfR` | 1 | 15 |
| Punk Hazard | `A1jFtPvQ` | 22 | 1–22 |
| Dressrosa | `f8HTXmAk` | 48 | 1–48 |
| Zou | `totP9Xmm` | 10 | 1–10 |
| Whole Cake Island | `AyvkmFFZ` | 39 | 1–39 |
| Wano • Acts 1 & 2 | `QZw3Ejpy` | 26 | 5–30 |
| Wano • Act 3 | `sF9stHJo` | 25 | 71–95 |

## Original-episode evidence

There are currently **zero verified original anime mappings**. Every recovered selected file has an empty API description and no explicit original-episode field. Most filenames identify only the fan-edit number. The Fishman Island filename contains `[632-633]`, but that unlabelled range is not evidence of original anime episodes; it must not be displayed as such. The Whole Cake Island `Filler Content.txt` note only says edits 15 and 16 contain the Big Mom/Brook fight and supplies no original-episode range.

`originalMappings` is deliberately empty. The UI description says `Original One Piece episode mapping is unverified.` Unknowns remain `null`, rather than zero, a fabricated offset, or a guessed range. Before populating a mapping, record evidence identifying the exact file/edit, original **anime** episode range, source URL, and verification date here; key it by file ID. Do not borrow a different edit's numbering or infer mappings from broad arc ranges. The range-rendering unit test uses explicitly synthetic evidence and contributes no catalogue mappings.

## Validation and commands

Local validation passed on `635650e` (metadata/tests), with the CI configuration and package assembly checked again at `8acd626`:

- **12 executable Kotlin/JUnit tests**, 0 failures, 0 errors. Covers all sixteen snapshots/274 baseline selections, numeric ordering, gaps, title cleanup, aliases, edition preference/fallback, language/media filtering, malformed input, and unknown mapping semantics. Tests use the actual Cloudstream library and JVM `org.json`; no hand-written API stubs.
- **10 Python checks**, including parsed-YAML permission, trigger, credential, publishing-guard and required-Kotlin-test assertions.
- `:MuhnPace:compileDebugKotlin`, unit-test compilation, `:MuhnPace:testDebugUnitTest`, `:MuhnPace:compileDex`, and `:MuhnPace:make` succeeded.
- The `.cs3` ZIP, DEX header/classes, plugin identity/version, generated indexes, file size and SHA-256 passed the same package assertions used by development CI. Files were assembled locally under `/tmp/muhnpace-phase1-package`; nothing was deployed.
- Compiled package SHA-256 at the metadata commit: `0352cce5aece812d3f014d529da0923f1f6937a8de35622c9a28fd5801a6ad16` (a local artifact, not a reproducible-build guarantee).

New GitHub validation: [run 43589206292](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43589206292), commit `8acd626`, **succeeded in 1m 22s**. Its successful `validate` job produced both `MuhnPace-tests-8acd62642d280eb9700e305a17d4871143c5fffe` and `MuhnPace-validation-8acd62642d280eb9700e305a17d4871143c5fffe` artifacts. The run conclusion, commit and artifacts were verified on the GitHub run page.

In this published environment:

```sh
cd /workspace/MuhnPaceCloudstream
source /workspace/setup/activate.sh
python3 -m pip install -r tests/requirements.txt
python3 -m unittest discover -s tests -v
gradle :MuhnPace:testDebugUnitTest :MuhnPace:make --no-daemon --console=plain
```

On another machine, provide JDK 17, Gradle 8.12 and Android SDK API 35 before running the last three commands. CI obtains the wrapper from the pinned official extensions commit. Test XML is in `MuhnPace/build/test-results/testDebugUnitTest/`; the HTML report is in `MuhnPace/build/reports/tests/testDebugUnitTest/`.

## Publication boundary

`validate.yml` runs on `codex/**` pushes, pull requests into `main`, and manual dispatch. It has `contents: read`, no publishing step or release action, no repository secrets, and `persist-credentials: false` on both checkouts. It uploads validation artifacts and Kotlin reports only. `scripts/publish.py` writes local files and performs no network operation.

`build.yml` accepts pushes to `main` and manual dispatch. Both its write-permission job and publication step require `github.ref == 'refs/heads/main'`. A dispatch from the development branch cannot enter the publishing job. Only `codex/v3-overhaul` was pushed during this recovery.

Remote protected destinations were checked again after the final Phase 2 code push (`0c792b0`) and remained:

- `main`: `8ade241e95405d91c0606bd3ea6080ac0709992a`
- `builds`: `cbe8d9dce96169da7ed12fefb8adc3787779bab3`

## Remaining limits

- Original anime mappings are unavailable and explicitly unknown, as approved by the user. No mapping research needs to be repeated without a new evidence source.
- The regression fixtures represent the recovered 2026-11-14 catalogue. Runtime selection still reads live Pixeldrain lists; future source changes or new filename conventions need review and new fixtures.
- Fire TV playback, Cloudstream UI rendering and a device-level watch-history migration were not exercised in this task. URL/identity compatibility is checked at the provider/API level.
- Existing Cloudstream Gradle/plugin library dependencies still use `-SNAPSHOT`; upstream changes can affect future builds. This recovery did not change dependency strategy.
- GitHub REST/log access is blocked in this environment, although Git operations and public Actions run pages work.

Production publication and changes to `main` or `builds` remain prohibited without explicit approval. Phase 2 development is complete with deliberate original artwork fallbacks and the documented device/source limitations. Stop before Phase 3.
