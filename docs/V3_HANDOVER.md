# MuhnPaceCloudstream v3 — Phase 1 handover

Updated: 2026-11-14 (UTC). Scope: Phase 1 metadata and validation only. Phase 2 has not begun.

Phase 1 is complete, with unsupported original-episode mappings explicitly left unknown as requested. No extension was published.

## Recovery

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

Phase 2 features, production publication, and changes to `main` or `builds` are outside this handover's scope.
