# MuhnPaceCloudstream v3 — handover

Updated: 2026-11-15 (UTC). Revised Phase 4A **A1 and A2 are implemented and locally validated**; A3 publication is pending the development CI result. One series, sixteen native seasons, 274 preserved file URLs, eight improved Dressrosa records. Stop after the isolated preview update for user feedback; do not start B/C, unrelated optimization or Phase 5.

## Phase 4A validation follow-up

Implementation `1b44ee34c98d966f4d0362e1ae81e045b5a56791` passed all local checks and both local repository/package validators. Development [run 43776844521](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43776844521) failed before Kotlin compilation: the new image audit imported Pillow, but `tests/requirements.txt` only installed PyYAML. Added the explicitly tested Pillow version as a **development test dependency**, with no extension/runtime dependency change. CI must succeed before preview publication.

## Phase 4A A1/A2 — locally validated

- Recovered `10fbf54` and preserved unfinished work in checkpoint `fbac35fa64a652f7be13e44485f9ba42b4e5a3fa`, pushed to development. Full [architecture, mapping, artwork and source findings](PHASE4A_DRESSROSA.md).
- Home/search now expose **One Piece (Muhn Pace)** once. Sixteen named native collection seasons retain all 274 streams; Dressrosa is season 12. Fishman Island's separately supplied alternate scene is season 10, adjacent to the main collection.
- Legacy arc routes, stream callback, provider/plugin identity and old season/episode history inputs remain intact for saved entries. Unified-series progress starts separately; no history migration/reset. Native episode indices remain navigation positions because partial/overlapping original numbering breaks host sorting/deduplication. No internal edit number is inserted into provider-authored titles/descriptions.
- Eight One Pace titles, eight individual story synopses and eight distinct exact-file interior stills. Coverage: **0 DIRECTLY_VERIFIED, 3 ONE_PACE_DERIVED, 5 CONFLICTING, 266 UNKNOWN**. `White Town - 701-702` is derived, with partial scene evidence for 701 recorded separately. Conflicting ranges are not displayed as established facts; unresearched titles use explicit unknown placeholders.
- New temporary main series art uses the recovered Law portrait and Luffy/Law background. Ten images are pinned to the recovery commit. Source/licensing limits remain explicit; no TMDB secret or unauthorized image mirror/hotlink. All eight formerly repeated thumbnails are replaced; the other 266 are outside this audit.
- **45 Kotlin/JUnit tests** (41 main + 4 preview), **20 Python checks**, Android compilation/DEX/`.cs3` creation pass locally. [Live API/image check](research/phase4a-live-validation.json) verifies 16 collections, all 274 baseline IDs/filenames, eight media-reported hashes and ten image URLs/dimensions/hashes. `amap` replaced unavailable direct coroutine imports after the first compile failed; no dependency was added.
- [Actual library/TV API inspection](CLOUDSTREAM_METADATA.md#phase-4a-native-seasons-and-skip-segments) confirms native season names and host indices. The paused plot remains series-level; Continue Watching lacks episode-title/range fields. Native skip buttons exist in the host but no public provider timestamp API is exposed. No unsupported seeking workaround or standard-anime skip-time lookup was added.
- Preview version is prepared as **2**, retaining `MuhnPacePreview`, `com.muhnpacepreview`, **Muhn Pace v3 Preview** and the existing repository URL. No Fire TV visual or persisted-resume test of this build has occurred. `main` and `builds` remain unchanged.

## Phase 4A recovery checkpoint — revised scope (2026-11-15)

Recovered development HEAD `10fbf54` and the interrupted, uncommitted Dressrosa 30–37 research. Preserved eight exact-file interior stills, two portrait/background experiments, source-file identities, scene observations, original title references and image audit. The audit passes: eight distinct thumbnails, zero near-duplicate pairs (dHash distance <=5), replacing eight opening images with 28 near-duplicate pairs. Source access is not a copyright licence; see `assets/phase4a/README.md`.

The user's revised scope is A1 (one series / sixteen seasons), A2 (eight Dressrosa samples with One Pace titles and qualified mappings), A3 (existing isolated preview update), then STOP. No B/C expansion. Skip-segment API investigation is secondary. This is a recovery/data checkpoint, **not a completed or published implementation**. The Kotlin work remains in progress. Production branches are unchanged.

## Pre-Phase 4 preview — milestone 2 complete (2026-11-15)

- Milestone 1 source `4526260c8e78a3c7dac973a6a9c20c14b14193fd` was committed and pushed before publication. Development [run 43747049494](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43747049494), job `132912564557`, passed every step: Python checks, both Kotlin suites/builds, both package checks and separate artifact uploads. All three artifacts identify this source SHA and development branch.
- Published exactly four preview-only files to the new orphan branch **`v3-preview`**, commit **`6d28eb858bed6933ccfd6677f94d6c6395c5f7c6`**: `repo.json`, `plugins.json`, `MuhnPacePreview.cs3`, `preview-build.json`. No production artifact or workflow is present there.
- Verified normal anonymous installation chain: `https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json` → preview `plugins.json` → preview `.cs3`, all HTTP 200. Downloaded bytes exactly match the locally validated build; manifest, DEX, isolated classes/routes, index hash and size passed again. Package SHA-256 **`437953f02bc059ab8f605a386876e94182a5b6d02473becbbd1f0bcca350be16`**, 27,903 bytes. Provenance identifies source `4526260`. CI independently passed on that source; its artifact ZIP download was blocked by HTTP 403, so publication uses the validated local package.
- [Fire TV installation, testing and safe removal instructions](V3_PREVIEW.md) require no computer. Install **Muhn Pace v3 Preview** alongside **MuhnPace** and select the preview provider/cards. Preview progress is separate; no transfer promise, history reset or production uninstall.
- The 35 Kotlin tests, 15 Python checks, Android/package checks and live 16-list/274-file/32-artwork checks passed as recorded below. No real Fire TV installation, playback, visual or persisted-resume test occurred. Await the user's visual feedback before Phase 4.
- Production `main` (`8ade241e95405d91c0606bd3ea6080ac0709992a`), `builds` (`cbe8d9dce96169da7ed12fefb8adc3787779bab3`), `MuhnPace.cs3` and the production installation URL remain unchanged.

## Pre-Phase 4 preview — milestone 1 (2026-11-15)

Recovered and fetched a clean `codex/v3-overhaul` at `91a9bf94748c0642ad1fb785dfd5e2f947a65f2f`. The user authorized a separately installed visual preview, followed by isolated publication only after the build milestone is validated and pushed. Phase 4 remains blocked on their Fire TV feedback.

- Inspected actual Cloudstream plugin paths, class loading, update matching, provider registration, uninstall and URL-derived history IDs. Both plugin identity **and catalogue/history routes** must differ. Details: [V3_PREVIEW.md](V3_PREVIEW.md).
- Added opt-in `MuhnPacePreview` module (`-Ppreview=true`), namespace `com.muhnpacepreview`, plugin `MuhnPacePreviewPlugin`, provider **Muhn Pace v3 Preview**, file `MuhnPacePreview.cs3`, version 1. Preview catalogue routes use `/muhnpace-v3-preview/l/<id>`; cards/player/history carry `(Muhn Pace Preview)`. Production routes are rejected by the preview.
- Preview sources are generated from current v3 source with checked identity substitutions; production Kotlin files, production workflow and packaging script are untouched. Pixeldrain playback URLs/callback, 16 collections, 274 selected edits, seasons/numbers, titles, descriptions, artwork and thumbnail fallback logic are preserved.
- **35 Kotlin/JUnit tests passed** (32 existing plus 3 preview tests); **15 Python checks passed**. Tests register both real plugins, exercise actual Cloudstream builders and compare all baseline production/preview series and episode history keys, including legacy host cases. Android logging alone uses the test default stub; device storage/plugin loading is not simulated.
- Android compilation, DEX and both `.cs3` builds passed. Preview package validation passed for manifest/version, relocated classes, absence of production classes, catalogue routes, index URLs, size and SHA-256. The first preview test compile used `MainPageData` where `MainPageRequest` was required; corrected. Registration initially hit the JVM Android Log stub; enabled the preview module's logging-compatible unit-test defaults, then all tests passed.
- Live checks: all 16 lists expose all 274 baseline selected files and exact-file thumbnail references; all 32 artwork URLs returned HTTP 200 with matching PNG dimensions and SHA-256.
- Development CI remains read-only and now validates/uploads preview files separately. Publication is pending milestone 1 CI success; no installation URL is advertised yet. Intended isolated artifact branch: `v3-preview`.
- Preview history is separate and is not promised to transfer to the final release. No Fire TV visual, playback or persisted-resume testing occurred. No production history/settings are edited by the extension. `main` and `builds` remain at the previously recorded SHAs.

## Phase 3 delivery (2026-11-15)

Recovered a clean `codex/v3-overhaul` at `d79c41798f1814b4de7c7f659e8c406fbcbeb530`. Terminal, Git fetch and repository access were verified before edits. A mid-task environment restart preserved the uncommitted changes, research and passing test reports; actual terminal execution rechecked them before resuming.

### Implemented and supported behaviour

- Series/load and home/search labels now identify **One Piece**, the arc and **Muhn Pace**, for example `One Piece • Dressrosa (Muhn Pace)`. The special Fishman collection remains distinct as `One Piece • Fishman Island • Edit 15 (Muhn Pace)`. Catalogue identities and stored arc names are unchanged.
- The existing episode-title formatter is unchanged. Unknown example: `Dressrosa • Muhn Pace 25`. Verified-range formatting remains available only for evidence-backed data. No real mappings or story titles were invented.
- The arc plot puts the live available-edit count, dub language and **Player numbers identify Muhn Pace edits** before the unchanged synopsis. Cloudstream's delayed TV pause overlay reads this plot, not `Episode.description`.
- Exact-edit mapping admission now requires collection/file/edit/filename, original range and source URL, verification date and qualification. Missing, changed or conflicting evidence remains unknown. The production registry is empty.
- Real Cloudstream response builders are shared by live loading and offline tests. All 274 baseline URLs/numbers/seasons and the 16 series URLs, type, provider identity and artwork assignments are checked. `loadLinks` and its file-URL validator remain byte-for-byte unchanged from `d79c417`.

The full [playback/API investigation](CLOUDSTREAM_METADATA.md) records the actual resolved library (`-SNAPSHOT:5d66bbeaf3-1`), AAR hash, source links, UI fields, history algorithm and Fire TV limits. The extension does not modify Cloudstream or its settings/storage.

### Player and Continue Watching limitations

- Player controls combine series name, host `S1:E25` label and episode title; all numeric season/episode labels still identify edits. Some arc/brand repetition is imposed by the shared host fields.
- The separate delayed TV pause overlay can show series/arc/edit brand, tags, numeric edit position and the arc synopsis. It cannot show the current episode's original range, name or description separately. It is setting-dependent, delayed eight seconds, and constrained to a two-line title/five-line overview in the inspected TV layout.
- Continue Watching can show the improved cached series name, existing arc poster, numeric season/edit and saved progress. It has no episode-title/description/range field. Its cached title refreshes when Cloudstream reloads the collection; existing cards are not forcibly rewritten.
- The result-page resume label and episode-selection list can use the existing episode name. Progress storage, next/previous selection, resume thresholds and history are host-controlled. No history reset, ID migration or automatic jump to an original episode was introduced.

### Mapping research and original episode 684

**Zero verified mappings added.** All sixteen live Pixeldrain lists were rechecked: 290 source entries, empty descriptions, no original-anime mapping fields; all IDs/filenames match the preserved baseline. [Research findings](ORIGINAL_EPISODE_RESEARCH.md) and the [dated audit](research/phase3-mapping-audit.json) record source URLs, response hashes, rejected candidates and access limits.

A third-party One Pace table gives Dressrosa 25 as 684–686; the official One Pace catalogue gives its own edit 25 as 683–686. Neither proves the coverage of Muhn Pace file `yUkgcapt` in collection `f8HTXmAk`. Another addon derives titles from One Pace subtitles, which also fails the exact-edit evidence requirement. No candidate title/range was imported.

The Muhn Pace edit containing original **684**, any additional covered episodes, its edit-specific story title, an intra-edit timestamp and the best transition starting point remain **unknown**. Do not recommend Muhn Pace edit 25 as a verified match. The guide-linked Drive folder/sheet were inaccessible; no maintainer-authored exact-file mapping was established. New reliable evidence is needed, not an offset or a cross-project numbering assumption.

### Validation and boundaries

Implementation/test commit: `8af331037ba08840b3632c244c3291d603bff728`.

- **32 Kotlin/JUnit tests**, zero failures/errors/skips: all 25 existing tests plus seven mapping-admission/real-response tests.
- **12 Python checks** passed, including workflow publication guards and artwork consistency.
- `gradle :MuhnPace:testDebugUnitTest :MuhnPace:make --no-daemon --console=plain` passed in 24s, including Android Kotlin/test compilation, DEX and `.cs3` generation.
- Local package assembly at `/tmp/muhnpace-phase3-package` passed the exact assertions from `validate.yml`: ZIP integrity, DEX/provider/plugin classes, unchanged manifest version 2, index URLs, hash and size. SHA-256: `fcf0299a207d0946e094436bfaae2b499308dff4748bcb0cd981c211c57e3039` (not a reproducible-build guarantee).
- Research/API documentation commit: `d75fc577e95b9540e86efe5c67dcc91255d40d04`.
- Development-only [validation run 43712404759](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43712404759) **succeeded** for that commit. The GitHub connector confirmed the successful `validate` job `132806631896`, every test/build/assembly/upload step, and the two artifacts `MuhnPace-tests-d75fc577e95b9540e86efe5c67dcc91255d40d04` and `MuhnPace-validation-d75fc577e95b9540e86efe5c67dcc91255d40d04`, with matching branch and commit. Job logs were accessible through the connector even though terminal REST access was blocked earlier. Local tests/build and this integration run passed; the later CI dependency failure is recorded below.
- Documentation-only commit `bb522ea095e0568d1f13f4ff8d768d1ee9846754` triggered [run 43712619237](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43712619237). Its first attempt failed before compilation: JitPack could not supply `com.github.recloudstream:gradle:-SNAPSHOT` (metadata selected `32895aedb6-1`, but its POM was not found). The full job log was retrieved via the GitHub connector. The implementation was identical to the preceding successful run; no provider/test change was indicated. The retry **succeeded without any code/dependency change**: job `132807769900` completed all checks and produced both `MuhnPace-tests-bb522ea095e0568d1f13f4ff8d768d1ee9846754` and `MuhnPace-validation-bb522ea095e0568d1f13f4ff8d768d1ee9846754`. This resolves validation for that commit, but intermittent upstream SNAPSHOT availability remains a build limitation.
- `main` and `builds` were rechecked after the implementation/research push and remain at `8ade241e95405d91c0606bd3ea6080ac0709992a` and `cbe8d9dce96169da7ed12fefb8adc3787779bab3`. Workflows, dependencies, version 2 and the installation URL are unchanged. Only `codex/v3-overhaul` was pushed; CI artifacts are validation outputs, not a production release.

No real Fire TV UI, media playback or persisted resume-position test occurred. API input compatibility is tested; device acceptance remains necessary. Existing SNAPSHOT dependencies can change upstream. No extension was published and no production URL changed. Stop after Phase 3; Phases 4 and 5 require separate authorization.

## Episode-title correction (2026-11-15)

Recovered the clean development branch `codex/v3-overhaul` at `5723fc1266dce9de8242205c87dec3304277f769` after confirming terminal execution and GitHub Git access. Phase 1 and Phase 2 were already complete; this change only corrects episode title formatting and its regression coverage. Phase 3 has not begun.

- Unknown original mapping: `Dressrosa • Muhn Pace 25`.
- Verified mapping: `One Piece <original range> • <arc> <edit number>`, with an en dash for ranges and a single number for a single original episode.
- There are still **zero verified catalogue mappings**. Mapped-title tests use explicitly synthetic evidence only. No equivalence between Dressrosa edit 25 and any original anime episode is asserted.
- `EpisodeMetadata.title` is now derived from the arc title, edit number, edition and optional verified mapping, so changing mapping metadata cannot leave a stale display title. Alternate/filler/fixed-group-shot labels and Wano act qualifiers remain visible. The Fishman special's collection title is unchanged; its episode title avoids repeating the collection's episode label.
- Provider and plugin identity, collection IDs/URLs/titles, selected file IDs, Pixeldrain playback URLs, explicit edit numbers and season 1 are unchanged. `MuhnPaceProvider.kt`, including `loadLinks`, is byte-for-byte unchanged from the recovered commit. Titles remain display metadata, not serialized playback or history identifiers.
- Regression coverage checks the new names and all 274 baseline file IDs, playback URLs, edit numbers and seasons. Formatter tests cover unknown mappings, synthetic ranges/single episodes, clearing a mapping, editions and special collection normalization.

Local validation passed:

- **25 Kotlin/JUnit tests**, zero failures/errors/skips; **12 Python checks** passed.
- `gradle :MuhnPace:testDebugUnitTest :MuhnPace:make --no-daemon --console=plain` succeeded, including Android Kotlin/test compilation and DEX/package generation (1m 48s).
- Local assembly under `/tmp/muhnpace-title-package` passed the exact package assertions from `validate.yml`: ZIP/DEX, plugin identity, manifest version `2`, index size/hash and URLs. Package SHA-256: `66f880ea00d9506ffbf1157e5026b23644878d0afc471e879f379247a8864a25` (not a reproducible-build claim).
- Code/test commit: `73ed51ac3aadee0f7e58a80bccfc5cb03e0fefdc`, pushed only to `codex/v3-overhaul`.
- Development-only [validation run 43663698313](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43663698313) **failed** in 49s. The [validate job](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43663698313/job/132658341449) reports exit code 1 in “Run Kotlin tests and build the Android Cloudstream extension”; no test reports or package artifacts were produced. The cause is **not established**, and remote validation must not be described as passing. Local validation above passed.
- Detailed CI logs require sign-in on the public GitHub page; GitHub REST access is blocked by the proxy, and shared-browser inspection failed. No workflow or dependency changes were made without diagnostic evidence. The subsequent development validation succeeded with unchanged implementation, as recorded below.
- Development-only [validation run 43663918261](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43663918261) **succeeded** for `2d0ca3ed120f3d6ccdab2f96aff48d83f8562620` in **1m 24s**. Its successful `validate` job produced `MuhnPace-tests-2d0ca3ed120f3d6ccdab2f96aff48d83f8562620` and `MuhnPace-validation-2d0ca3ed120f3d6ccdab2f96aff48d83f8562620`. The SHA, conclusion and both artifact names were verified on the public run page. The implementation and tests are identical to `73ed51a`; no code or workflow workaround was needed. The earlier failure did not recur, but its cause remains unknown.
- Remote `main` and `builds` were checked after the code push and remain at `8ade241e95405d91c0606bd3ea6080ac0709992a` and `cbe8d9dce96169da7ed12fefb8adc3787779bab3`, respectively.

This correction does not publish the extension or change `main` or `builds`. Fire TV rendering, playback and persisted device history still require real-device verification; the compatibility claims here concern provider/API inputs.

## Phase 2 delivery

The user supplied photographs of Cloudstream Beta on an Amazon Fire TV Stick 4K (2nd Generation), showing grey arc cards, sparse pages, raw episode filenames and existing watch progress. These show the published provider, not a device test of the development branch. Phase 1's clean titles were preserved during Phase 2 and are clarified by the title correction above.

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

Example edit 25 now uses the title `Dressrosa • Muhn Pace 25` and describes only known facts:

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

### Later requirements recorded at the Phase 2 handover

- **Phase 3 (now addressed above within API/evidence limits):** improved paused-playback titles; verified original One Piece episode numbers/ranges; proper anime/arc metadata during playback; better Continue Watching presentation.
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

`originalMappings` uses an empty exact-edit registry after Phase 3 research. The UI description says `Original One Piece episode mapping is unverified.` Unknowns remain `null`, rather than zero, a fabricated offset, or a guessed range. Before populating a mapping, record evidence identifying the exact file/edit, original **anime** episode range, source URL, and verification date here; key it by file ID. Do not borrow a different edit's numbering or infer mappings from broad arc ranges. The range-rendering unit test uses explicitly synthetic evidence and contributes no catalogue mappings.

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

Production publication and changes to `main` or `builds` remain prohibited without explicit approval. Phase 3 development is complete within the documented API/source limits. Stop before Phases 4 and 5.
