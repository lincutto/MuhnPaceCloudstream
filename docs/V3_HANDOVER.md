# MuhnPaceCloudstream v3 — handover

Updated: 2026-11-14 (UTC). Phase 1 is complete. Phase 2 is in progress and blocked on artwork-source access. Phases 3 and 4 have not begun.

Phase 1 is complete, with unsupported original-episode mappings explicitly left unknown as requested. No extension was published.

## Phase 2 progress and remaining blocker

The user supplied photographs of Cloudstream Beta on an Amazon Fire TV Stick 4K (2nd Generation), showing grey arc cards, sparse pages, raw episode filenames and existing watch progress. These show the published provider, not a device test of the development branch. Phase 1's clean titles are preserved.

| Commit | Verified change |
| --- | --- |
| `be72443` | Exact-edit thumbnails and factual episode descriptions; all 12 Phase 1 Kotlin tests plus 5 new tests pass |
| `6e6267c` | Distinct synopses for all 16 collections, live edit counts, anime/adventure tags, supported artwork fields and fallback hooks; 20 Kotlin tests pass |

**Phase 2 is not complete. No high-quality arc poster/background URLs have been configured yet.** Existing runtime network policy blocks `one-piece.com`, `en.wikipedia.org` and `image.tmdb.org` with proxy HTTP CONNECT 403 responses. Do not invent URLs, assume image permission, or present 256-pixel edit thumbnails as finished television-quality posters.

A restricted-network draft has been saved with the existing domains preserved and these five proposed additions: `one-piece.com`, `en.wikipedia.org`, `upload.wikimedia.org`, `www.themoviedb.org`, `image.tmdb.org`. Saving the agent draft does **not** apply runtime access or publish anything. The tool reported `requires_publish: true` for the saved configuration. The owner must review/save the settings through the product; recheck live access before resuming artwork research. No environment or extension was published by the agent.

After access is available, finish these Phase 2 requirements before declaring completion:

1. Verify legitimate source terms, attribution and stable HTTPS image URLs, select suitable arc-specific posters/backgrounds, and inspect the images. Add URL/configuration evidence and tests for all 16 entries, including Wano parts and the Fishman Island special.
2. Populate `ArcPresentations`' artwork fields and deliberate fallback choices. The current fallback order is configured poster → fallback poster; background → chosen poster; missing episode image → chosen background/poster. Network failures after an image is handed to Cloudstream are handled by the host app; no custom on-error hook exists in `Episode` metadata.
3. Add original anime arc coverage only where verified, using the existing `OriginalEpisodeMapping` type and a source. Broad arc coverage must not be mistaken for edit-level mapping.
4. Re-run relevant tests, local compilation and development-only CI; update this handover and retain the production branch safeguards.

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

The [Pixeldrain API](https://pixeldrain.dev/api) documents `/file/{id}/thumbnail?width=x&height=x`. The implementation accepts only a `thumbnail_href` matching the exact selected file (`/file/<selected-id>/thumbnail`) and uses `https://pixeldrain.dev/api/file/<selected-id>/thumbnail?width=256&height=144`. Missing or mismatched links use configured arc artwork when available. Stream URLs and the `loadLinks` implementation remain unchanged.

All **274** selected files' thumbnail URLs were fetched on 2026-11-14: HTTP 200, `image/png`, successfully decoded at 256×144. Their source references, URLs, dimensions, response cache policy and SHA-256 are recorded in `MuhnPace/src/test/resources/thumbnail-verification-2026-11-14.json`. Images themselves are **not** committed. These are remotely hosted previews of the existing edit files, not original-anime mappings or a claim of redistribution rights.

Pixeldrain allows dimensions only in multiples of 16 up to 256. A 1280×720 request was rejected with HTTP 400. The thumbnails are suitable for small rows, not HD backgrounds. Inspected examples include opening-credit imagery, so multiple edits may look similar and spoiler freedom is not guaranteed. The API may return a generic file icon when it cannot generate a thumbnail. Successful responses carry `Cache-Control: public, max-age=31536000`; Cloudstream controls its own cache and rendering.

Thumbnail code validation: [run 43591490583](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43591490583) **succeeded** for `be72443` in 1m 27s, with test-report and package artifacts. The arc-description commit passed **20 Kotlin tests**, **10 Python checks**, and `:MuhnPace:make` locally. [Development run 43591948009](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43591948009) **succeeded** for `6e6267c` in 1m 26s, also producing both artifacts. The same CI package assertions passed locally against the assembled files under `/tmp/muhnpace-phase2-package`.

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

Remote protected destinations were checked after that push and remained:

- `main`: `8ade241e95405d91c0606bd3ea6080ac0709992a`
- `builds`: `cbe8d9dce96169da7ed12fefb8adc3787779bab3`

## Remaining limits

- Original anime mappings are unavailable and explicitly unknown, as approved by the user. No mapping research needs to be repeated without a new evidence source.
- The regression fixtures represent the recovered 2026-11-14 catalogue. Runtime selection still reads live Pixeldrain lists; future source changes or new filename conventions need review and new fixtures.
- Fire TV playback, Cloudstream UI rendering and a device-level watch-history migration were not exercised in this task. URL/identity compatibility is checked at the provider/API level.
- Existing Cloudstream Gradle/plugin library dependencies still use `-SNAPSHOT`; upstream changes can affect future builds. This recovery did not change dependency strategy.
- GitHub REST/log access is blocked in this environment, although Git operations and public Actions run pages work.

Production publication and changes to `main` or `builds` remain prohibited without explicit approval. Continue Phase 2 only after its artwork-access blocker is resolved; stop before Phase 3.
