# MuhnPaceCloudstream v3 — handover

## Season 16 approximate display ranges — preview v6 (2026-11-17)

**25/25 titles updated**, retaining the existing Muhn Pace scene labels and appending the user-supplied anime range with the same `Title - 1034-1036` format used by Seasons 1–15. Single episodes use `Title - 1079`. No approximation marker is added to visible titles or descriptions.

The user explicitly accepts **provisional, approximate Onigashima Paced reference ranges** for display. Exact correspondence with Muhn Pace has **not** been established; none is directly or independently verified. This display decision supersedes the earlier no-display/no-publication instructions below, not their evidence findings. Each [Season 16 record](../metadata/phase4b/season-16.json) retains `mapping_status: UNKNOWN`, empty accepted ranges and its historical research classification, with a separate `provisional_display_mapping` containing the supplied numbers, original title and qualification. Only the display title is changed in the bundled runtime dataset. No mapping investigation, licensing review or video inspection was repeated.

- Starting remote development: `cf91e6cd41b24f0d8b811c92e5acf563d3c536ac`. Remote preview was still v5; no v6 existed.
- **Known-good v5 fallback:** preview `091fc774199b2af7572f8e63b3f59bd97543b040`, source `8ffd8958f7e939dc8e1a6985a523c9a0e64a9f75`, package SHA-256 `c55172447f41cb6a4f0390b59edb0317abe702a5d287d3543a818957cfe3ebf4` (92,267 bytes). Preserve this commit and its four publication files in preview history; only fast-forward publication is permitted.
- [Comparison against public v5 and its recorded source passed](research/preview-v6-v5-comparison.json): the downloaded v5 DEX contains the exact baseline dataset; all **274 episodes / 16 seasons**, IDs, indices, playback URLs, ordering, descriptions, thumbnails, series artwork, provider identities and watch-history inputs are unchanged. Seasons 1–15 remain byte-identical.
- Preview-only package version is **6**. Existing Python metadata/integrity checks and both Kotlin/Android build targets run once on the final source commit through the read-only development workflow. Publish that validated preview artifact only after success, then verify the public four-file installation chain, manifest/version, ZIP/DEX, hash and size. Record source/preview hashes and results in the publication evidence after validation; this preparation entry alone does not claim success.
- Installation URL remains `https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json`. Production `main` and `builds` remain prohibited. Stop after verified v6; no Phase 6.

The entries below are historical checkpoints; their previous unresolved display decisions do not override this explicit user authorization.

## Supplied Season 16 references — bounded follow-up (2026-11-17)

**0/25 DIRECTLY_VERIFIED, 0/25 DERIVED_VERIFIED, 1/25 PARTIAL, 24/25 UNKNOWN; 0 titles changed.** [Updated evidence, all candidates and exact file identities](research/season16-mapping-review-2026-11-17.json). Latest remote development `15105e83ae0535eca484caa7872c93b85246c608` and preview `091fc774199b2af7572f8e63b3f59bd97543b040` were inspected before changes.

- The supplied `6ip/onepace-streams` catalogue contains Wano 71–95. Its titles correspond to Onigashima 29–48 and End of Wano 01–04 (including 03.5), whose spreadsheet contains all 25 proposed ranges. The catalogue does not supply anime ranges; the spreadsheet does not establish Muhn-file equivalence. Official One Pace stops at Wano 62 (1032–1034) and points to 1034–1085 for the remaining anime; that viewing handover is not a Muhn mapping.
- **Edit 71 discrepancy is not proof of a different boundary.** The existing Robin title is an editorial scene label. Newly sought exact-file stills at 60s and 2040s show Luffy underwater and Momonosuke with the frog Mary, consistent with the candidate story period. The 1080s midpoint is not independently identifiable; 1067s was dark. Neither the existing Robin imagery nor these samples establishes a complete range. In particular, do not replace 1034–1036 with guessed 1042–1044. The Mary announcement spans/reappears in the 1036/1037 references, so no unique numbered frame attribution is claimed.
- Source A's linked release lists the same byte size as file `8ZxdEP7k`, but no comparable media hash. Its duration is 35:35 versus the spreadsheet's 35:09. Association and partial scene correspondence are supported; identical coverage and a full-range conversion are not. Different anime boundaries are also **not proven**. Stop here rather than inspect the remaining 24 videos.
- All candidates and classifications are retained in research-only evidence. Season 16's runtime mapping fields remain UNKNOWN with empty accepted ranges; generated Kotlin and all visible metadata remain unchanged. The new research stills are not application artwork. All 274 episode records, sixteen seasons, ordering, file IDs, URLs, descriptions, thumbnails, artwork, provider identities and history inputs are preserved.
- No accepted correction means **no CI, Kotlin/Android build, version bump or publication**. Retain known-good **preview v5**, `091fc774199b2af7572f8e63b3f59bd97543b040`, validated source `8ffd8958f7e939dc8e1a6985a523c9a0e64a9f75`. The evidence-only development commit uses `[skip ci]`. No production or Phase 6 changes.

Resume only with creator-authored Muhn anime ranges or an exact-file comparison that establishes complete coverage, including intercuts/flashbacks and boundaries. Matching titles, chapter tags, edit numbers, byte sizes or near-equal runtimes alone are insufficient.

## Season 16 original-anime mapping review — no supported changes (2026-11-17)

**0/25 complete mappings verified; 0/25 titles updated.** All Season 16 positions **1–25** (Muhn edits **71–95**) remain unresolved. [Exact file list and focused evidence](research/season16-mapping-review-2026-11-17.json).

The live `sF9stHJo` listing still matches all 25 selected filenames and media-reported hashes. Every selected file description is empty; filenames give only the Muhn edit number. The separate 38-byte note redirects to One Pace. The current Muhn-specific metadata tree is identical to the previously inspected tree (`ce627c81bd5985b6322ae7558b41976f316e3c1d`), which lacks these late releases. The directly relevant Plex table stops at Wano 31, and the subtitle mirror at Wano 46. The dub guide supplies viewing order, not per-edit original-anime coverage. Existing sparse scene observations cannot establish full ranges, and another edit's indices are not a supported mapping.

The existing formatter already appends admitted original-anime coverage while preserving the title and navigation indices. No runtime, metadata, artwork, thumbnail, description, playback or identity change was warranted. The catalogue still has **274 entries / 16 seasons**, including **25 in Season 16**. No tests or builds were rerun; no unchanged preview was republished.

Retain the already verified **preview v5**, commit `091fc774199b2af7572f8e63b3f59bd97543b040`, validated source `8ffd8958f7e939dc8e1a6985a523c9a0e64a9f75`; v4 `3317cfccfbf5d81f43ae6c0182f4b75ade96474d` remains the fallback. Production `main` and `builds` are unchanged. Resume mapping work only with a per-edit release table or a sufficiently complete exact-file comparison that establishes the original episodes, including partial/rearranged coverage. No Phase 6.


## Preview v5 — final visual refinements prepared (2026-11-17)

The user tested v4 on Fire TV and requested removal of technical playback text plus a filled portrait poster. [Focused findings, supported player setting and crop trade-offs](V3_VISUAL_REFINEMENTS.md).

- Complete playback-line suppression is a client display setting, not an independent provider field. Use **Settings → Player → Show player information**, deselect **Source Name** and **Resolution**. Keep **Media Info** off. The provider's link name also labels the source selector; blanking it would leave an unnamed row and would not remove the decoded resolution. Playback code and quality metadata are unchanged.
- User explicitly chose the central portrait crop. Replacement IMG_9122.jpeg arrived as 1280×718; crop `[399,0,877,717]` then proportional LANCZOS resize gives **768×1152**, without padding/distortion/generation. Luffy, Zoro and Sanji remain prominent; most outer crew members are excluded. Original source and exact transformation are preserved. Artwork commit: `52051fde43afbf13f21231dd38b712f49a8b94a6`.
- Independent immutable poster pin preserves the background's exact v4 URL and bytes. All 274 episode metadata records, sixteen seasons, thumbnails, stream URLs, provider identities and watch-history inputs are unchanged.
- Preview-only version **5** is prepared for the existing CI validation/build workflow. Publication is conditional on success. Preserve v4 fallback `3317cfccfbf5d81f43ae6c0182f4b75ade96474d`; production `main`/`builds` remain prohibited. No Phase 6.


## Artwork preview v4 — published and verified (2026-11-17)

- **Known-good fallback:** preview commit `3317cfccfbf5d81f43ae6c0182f4b75ade96474d`, version **4**, from exact source `8f3e2f8de65a4b6c2385b0487113ecb7954af859`.
- Reused the existing successful [CI run 43943800055](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43943800055), job `133567958091`: **25 Python checks, 50 Kotlin/JUnit tests, both Android/DEX/CS3 builds**. Downloaded preview artifact `14263741833` for that exact source; its ZIP SHA-256 matches GitHub's artifact digest. No rebuild or test rerun was needed.
- [Public verification evidence](research/preview-v4-publication.json): `repo.json`, `plugins.json`, `preview-build.json`, and `MuhnPacePreview.cs3` returned HTTP 200 and matched the validated artifact bytes. ZIP/DEX/manifest/version/namespace/routes/heading/hash/size validation passed. Package: **92,215 bytes**, SHA-256 `3cb3e58d6dd24b1472f2452c13ff23f5d52a89560903869349599d3c59a00700`.
- Both immutable artwork URLs at `9d8ea67c8d98c1aba6dade4d1e633b753201dde3` returned HTTP 200, `image/jpeg`, decoded correctly, and matched committed bytes/hashes: poster **768×1152**, background **1280×721**. Existing real-preview tests cover home/search/detail poster and detail background assignment; the package contains the exact **One Piece • Muhn Pace (English dub)** heading and artwork commit.
- All 274 episodes across 16 seasons, episode metadata/thumbnails, playback, provider/plugin/package identities, routes and history inputs remain unchanged. Runtime diff from `89f3c9b` changes only the series artwork base. `repo.json` remains byte-identical to v3.
- Production remains untouched: `main` at `8ade241e95405d91c0606bd3ea6080ac0709992a`, `builds` at `cbe8d9dce96169da7ed12fefb8adc3787779bab3`. No Fire TV device test is claimed. No Phase 6 or production release.


## Secondary UI checks — complete; retain v4 (2026-11-17)

[Focused API/client findings](V3_UI_LIMITATIONS.md): both **episode count** and **provider display label** are **unsupported** under the required preservation constraints. Cloudstream formats the selected season's list size (Dressrosa: 48); it provides no total-count-label override. The visible provider name is also its registry/saved `apiName` lookup key, with no separate display-only name. Relevant code is unchanged between the previously resolved `5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c` and current upstream `2dcc25c3945c61dfa95d1710ced7c74acfe84baa`.

No runtime changes, episode restructuring/duplication, identity renames, dependency changes or unsupported workarounds. No v5 build/publication; existing v4 remains the deliverable and fallback. No publication blocker remains. These documentation-only results do not require repeating successful tests/builds. Stop here; no Phase 6 or production release.

## Supplied artwork integration — preview version 4 prepared (2026-11-16)

The user's new instruction supplies exact images and authorizes their integration/public preview publication without repeating the earlier broad licensing research. This supersedes the unresolved sourcing approach below. Picture 1 is now the main poster with proportional scaling and dark blurred portrait padding; Picture 2 is the unchanged 1280×721 background. No specific publication restriction was supplied with these attachments. User supply is recorded as provenance, not as a rightsholder licence.

- Artwork checkpoint: `9d8ea67c8d98c1aba6dade4d1e633b753201dde3`. Both series URLs use that immutable development commit through the existing `series.json` / `ResearchDataset.SERIES_ARTWORK_BASE` mechanism.
- Picture 1 arrived as a 1200×675 JPEG despite its user-facing name `IMG_9122.webp`. The complete frame is reduced to 768×432, centered on a 768×1152 canvas, and padded above/below with a dark blurred aspect-fill copy. No foreground crop, stretch, generated characters or face changes. Picture 2 (`IMG_9123.jpeg`) is copied byte-for-byte; Pictures 3/4 were not processed. Exact received and exported hashes/transformations are in `assets/phase4b/manifest.json`.
- Preview version is **4**. The exact heading **One Piece • Muhn Pace (English dub)** is included. Home/search/detail poster and detail background field assignments are covered by the existing real-preview test; package validation additionally checks the heading in DEX.
- Provider/plugin identities, virtual URLs, all 16 seasons/274 entries, episode metadata/thumbnails, playback logic and history inputs are unchanged. Byte comparisons against `89f3c9b` confirm this, including the entire bundled episode JSON. Only the series artwork base changes in runtime Kotlin. No episode-numbering work is included.
- Local visual review confirms successful decoding and retained source framing. Fire TV crop, small-thumbnail readability, overlay legibility and saved resume still require the user's device screenshots. No device test is claimed.

Validation passed once after implementation: **25 Python checks, 50 Kotlin/JUnit tests (44 base + 6 preview), and both Android/DEX/CS3 builds**. Publication results will be recorded after verification. Publish only to the existing isolated `v3-preview` repository, then stop for Fire TV screenshots; no production release or Phase 6.

## Final artwork refinement — blocked on reuse permission (2026-11-16)

The latest request requires authentic crew artwork with a verified licence or other permission for this use. The user has no supplied artwork/permission and suggested finding images through Google Images. No replacement pair satisfying that permission requirement was established. **Artwork is unchanged; no next preview version was published.** Do not treat the existing manifest's noncommercial identification rationale as a licence, or treat the new tests as visual acceptance.

- Fresh remote heads before work: development `282dc1a6696a74728c29a416752bae82e20e83b7`; preview `1dcb77c446a763adcad74e01ac71403192243975` (version 3, source `ffc1f51d98916c1ac4fa60f6a67e27c2df0da738`). The prior episode-numbering investigation remains unchanged.
- The current environment now reports enforced **unrestricted** networking, superseding the old network checkpoint below. The official Egghead announcement `https://one-piece.com/news/64180/index.html` and site agreement still return HTTP 403 on ordinary HTTPS requests; no restrictions were bypassed. Other official Toei pages and TMDB terms were accessible.
- [Toei USA's FAQ](https://www.toei-animation-usa.com/faq.html), individual-inquiries section, does not grant individuals permission to use its characters for the listed creative/display uses. [Toei Europe's legal notice](https://www.toei-animation.com/legal-notice/) retains intellectual-property rights and supplies no express grant for this preview. These pages do not establish permission to republish the desired images.
- [TMDB's FAQ](https://developer.themoviedb.org/docs/faq) permits attributed noncommercial API use, but this is conditional, not an unrestricted image licence. The [full API terms](https://www.themoviedb.org/api-terms-of-use), accessed successfully over HTTPS, require a prominent logo/notice and prohibit caching beyond six months and making derivatives. The existing permanent GitHub-copy pipeline does not satisfy those conditions. No suitable licensed pair or alternative attribution/delivery integration was established; no TMDB images were imported. A Google Images result or uploader credit alone does not meet the user's explicit permission requirement.
- Added one real-preview regression in `PreviewIsolationTest.kt`: exact home heading **One Piece • Muhn Pace (English dub)**, unchanged `muhn-preview` key/routes, distinct HTTPS series image URLs, poster assignment to home/search/detail, and background assignment to the detail background field. No runtime source, metadata, episode thumbnail, artwork image, version, or publication configuration was changed.
- Validation: **25 Python checks passed**; **50 Kotlin tests passed** (44 base + 6 preview, no failures/errors/skips); both Android/DEX/CS3 build targets succeeded. Unchanged tasks were up-to-date; the new Kotlin test compiled and the preview suite executed. No replacement-image dimensions/decode or Fire TV visual test is claimed because no replacements were selected.
- Downloaded the four existing public preview files over HTTPS: HTTP 200, byte-identical to the preview commit, and `assemble_preview.py --verify-only` passed ZIP/DEX/manifest/index/hash/size checks. Existing `.cs3` size/hash remain 92,180 bytes / `43c3704007b0ffab045109c2c1f08d1adfe73ff343c44cd97b63d1edbd9a090c`. The installation URL remains `https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json`.

Resume only with a suitable authorised source or an agreed different artwork approach. No intermediate heading-only preview was published. Production `main`/`builds` and preview were not written. Phases 5 and 6 remain unstarted.

## Additional visual refinements — development checkpoint (2026-11-16)

The user has tested preview v3 and requested a modern crew portrait, a different widescreen crew background with space for text on the left, and the exact home row heading **One Piece • Muhn Pace (English dub)**. Include these refinements in the next validated preview release; **do not publish a separate intermediate preview for this request**.

- The home row heading is implemented in the shared provider and isolated preview generator. Category keys (`muhn` / `muhn-preview`), provider/plugin/package identities, routes, installation URL, all 16 seasons and all 274 episode records remain unchanged.
- Source checkpoint **`0b59be060504a0bc358505517e415dd99a99b104`** is pushed. Local validation: **24 Python checks and 48 Kotlin tests passed**, with both Android/DEX/`.cs3` packages built successfully and the exact requested label confirmed in the preview DEX. [Development CI 43877617023](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43877617023), job `133334671638`, passed every step for that source. No new Fire TV test was performed.
- **Artwork replacements remain pending source access and visual review.** Both current series images and their immutable commit pin remain unchanged. The active environment's enforced network allowlist still excludes `one-piece.com`, `www.themoviedb.org`, `image.tmdb.org` and `developer.themoviedb.org`; official-site/TMDB fetches returned HTTP 403. The user has been asked to apply the network-setting update. Do not treat the earlier offer to change settings as proof that it was applied, or claim image quality/rights/URLs have been verified.
- The user subsequently reported enabling **Enable all public internet access**. A fresh status check still reports current observations, enforced `restricted` mode and configuration revision **1**, with custom hosts limited to `chatgpt.com`, `jitpack.io`, `pixeldrain.com` and `pixeldrain.dev`. Fresh connections to all four requested artwork hosts still fail at the proxy with HTTP 403. The reported setting has therefore not reached this running instance; check the current chat's applied environment rather than repeatedly requesting individual domain additions. No networking bypass was attempted.
- A research lead is the [official Egghead key visual announcement](https://one-piece.com/news/64180/index.html). It is only a candidate: download and inspect the actual source before choosing it, verify dimensions and composition, and record attribution and rights limitations. Do not substitute another copy of the rejected older crew poster or red background.
- Resume by selecting two suitable distinct images, using the existing artwork manifest/audit and immutable asset-commit pipeline, and verifying their exact published URLs. Preserve all episode thumbnails/metadata. No preview version bump or publication has been performed for this checkpoint; published v3 remains `1dcb77c446a763adcad74e01ac71403192243975`.

## Phase 4B — preview version 3 published and verified (2026-11-16)

- Validated source **`ffc1f51d98916c1ac4fa60f6a67e27c2df0da738`**, isolated preview commit **`1dcb77c446a763adcad74e01ac71403192243975`**. All incremental source/artwork checkpoints are preserved in development history.
- [Development CI 43832261690](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43832261690), job `133180161782`, **succeeded** in every project-check, Kotlin/Android, package validator and artifact-upload step. All three artifact records identify the same development source/branch. Local **48 Kotlin tests, 24 Python checks**, both Android/DEX/.cs3 builds and preview package validation passed.
- [Anonymous live verification](research/phase4b-live-validation.json): all **16 source collections**, all **274 baseline file identities and media-reported hashes**, and all **276 immutable JPEG URLs** passed; exact dimensions, MIME and hashes match. No near-duplicate thumbnail pairs at dHash ≤5. These checks do not verify copyright permission, full story boundaries or Fire TV rendering.
- [Public preview verification](research/phase4b-preview-publication.json): exact `repo.json` → `plugins.json` → `MuhnPacePreview.cs3` URLs return HTTP 200 and package version **3**, with manifest/DEX/namespace/routes/hash/size intact. **92,180 bytes**, SHA-256 `43c3704007b0ffab045109c2c1f08d1adfe73ff343c44cd97b63d1edbd9a090c`. Public bytes equal the locally validated artifact. CI independently validates the same source; no local/CI reproducibility claim.
- Repository URL is unchanged: **https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json**. `repo.json` is byte-identical to v2; provider/plugin/package/routes and all v2 progress inputs are preserved. [Fire TV update/test instructions](V3_PREVIEW.md). No uninstall or app-data reset.
- Coverage: **249 published One Pace titles/derived ranges**, **25 late-Wano editorial scene labels with UNKNOWN ranges**, **274 descriptions** (273 unique; two IDs contain the same corrected Fishman media), **274 distinct individual thumbnails**. No fabricated ranges or claimed full direct verification. [Research, gaps and artwork rights](PHASE4B_METADATA.md).
- Production `main` remains **`8ade241e95405d91c0606bd3ea6080ac0709992a`** and `builds` remains **`cbe8d9dce96169da7ed12fefb8adc3787779bab3`**. All three public production repository files were compared byte-for-byte with the baseline; installation URL, manifest and package are unchanged.
- **STOP for Fire TV feedback.** The user tested v2; the assistant has not tested v3's TV visuals or stored resume. No Phase 5, further mapping expansion or unrelated features have begun. Late-Wano full ranges remain a documented evidence gap, not a reason to reset progress or change sorting.

## Phase 4B — final source ready for CI/publication (2026-11-16)

All 274 records are populated without changing the unified series, sixteen seasons, source IDs/URLs or v2 preview history inputs. Coverage: 249 published One Pace titles/ranges; 25 late-Wano editorial scene labels and internally UNKNOWN ranges; 274 summaries (273 distinct texts, two identical-media Fishman IDs); 274 distinct thumbnails, zero baseline generic fallbacks or dHash near-duplicate pairs. No full range is DIRECTLY_VERIFIED. [Findings, source/rights limitations and coverage](PHASE4B_METADATA.md).

Validated checkpoints pushed: Dressrosa `a028ae8`, first eight seasons `a4ab3d8`, Fishman/Punk/Zou `929d0bc`, Whole Cake/early Wano `18eb697`. Late-Wano image checkpoint `876d409`; new metadata preserves sparse scene evidence without fabricating One Pace counterparts or ranges. All 48 Kotlin tests, 24 Python checks and both Android/.cs3 builds pass locally. Preview-only version is 3; production module/version and publication workflow are unchanged. Final CI, anonymous image/source checks and isolated publication remain to be recorded after they actually pass. No Fire TV test has been performed by the assistant. Stop after preview publication for user feedback; no Phase 5.

## Active Phase 4B — checkpoints 1 and 2 (2026-11-16)

The user's new Phase 4B request supersedes the Phase 4A stop below. Recovered clean `5a37acf`; preserve published v2 and its identities. Crew-art/Dressrosa image checkpoint: `17df462` (50 reviewed images, 48 distinct episode stills). All 48 Dressrosa titles/ranges/synopses now use structured exact-file records; the additional Muhn Pace-specific source resolves the five earlier sample disagreements as ONE_PACE_DERIVED, not directly verified. The main series plot is concise; player descriptions no longer contain research/development notes. See [Phase 4B findings and remaining work](PHASE4B_METADATA.md).

Checkpoint 1 implementation `a028ae8` is pushed. The first eight additional seasons add 81 records and stills (art checkpoint `0ebd79a`), bringing completed story records to 129. Post-Enies Lobby files 4/5 correspond to One Pace 5/6 by their specific Muhn release titles and story references, not by equal edit numbers. All native positions remain unchanged.

Next batch: Fishman Island (including the separately preserved corrected-shot file), Punk Hazard and Zou add 55 records, bringing coverage to 184. All are individually summarized with derived ranges and distinct exact-file-associated stills. Both Fishman corrected-shot filenames explicitly match the published manga-chapter tags; those tags are never mistaken for anime episode numbers.

Whole Cake Island and Wano Acts 1/2 add 65 records with independently written summaries and reviewed frames (art `df730ce`), bringing published-reference coverage to 249. The latest batch passes all 47 Kotlin and 24 Python checks and both Android builds. Late Wano 71–95 remains outside the published title/range tables; the user explicitly authorized evidence-supported descriptions with internally unresolved mappings and bounded further investigation.

Existing Android builds and Kotlin regression suite passed after updating two obsolete description expectations; Python has 24 passing checks. All 47 Kotlin tests (43 main + 4 preview), both Android packages and the additional formatter/real-response checks passed locally. No v3 package has been published yet. Remaining seasons and final validation/publication follow; do not modify main/builds or start Phase 5. Preserve the late-Wano evidence gap rather than guessing coverage.

Updated: 2026-11-15 (UTC). Revised Phase 4A **A1, A2 and A3 are complete and the isolated preview v2 is published**; development CI and anonymous installation-chain checks passed. One series, sixteen native seasons, 274 preserved file URLs, eight improved Dressrosa records. Stop after the isolated preview update for user feedback; do not start B/C, unrelated optimization or Phase 5.

## Phase 4A A3 — publication verified, awaiting Fire TV feedback

- Recovery/assets: `fbac35fa64a652f7be13e44485f9ba42b4e5a3fa`. Implementation: `1b44ee34c98d966f4d0362e1ae81e045b5a56791`. CI dependency correction / validated source: **`578a7a0059bf44c874db3e01feb24f6ee7d128c7`**.
- [Development validation run 43776953340](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43776953340), job `133005293407`, **succeeded**. Every project-check, Kotlin/Android build, package validator and artifact upload passed. All three artifact records identify source `578a7a0` on `codex/v3-overhaul`. This resolves the missing-Pillow failure described below. A clean local Python virtual environment also passed all 20 checks with the declared requirements; all 45 Kotlin tests and both local packages passed.
- Updated the existing `v3-preview` branch by fast-forward commit **`b3829bcc1eaecda75808e65fda7b7c360439e0b3`**, preserving plugin, namespace, repository and download identities. Version **1 → 2**. The branch still contains only four preview repository files; `repo.json` itself is byte-identical. Published the locally validated Android package; CI independently built/validated the same source, and no byte-identical local/CI build claim is made.
- Verified at **2026-11-15 23:53 UTC**: anonymous exact URL chain `repo.json` → `plugins.json` → `MuhnPacePreview.cs3`, plus provenance, returned HTTP 200. Downloaded ZIP/DEX/manifest/version/identity/size/hash checks passed; package bytes equal the validated local artifact. Size **45,376 bytes**, SHA-256 **`196d3e5def1db58803a5bdf976bdc3169751fe4b06fc192417b69ac924bd84ce`**. [Publication evidence](research/phase4a-preview-publication.json).
- Installation URL remains **https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json**. [Update and test instructions](V3_PREVIEW.md): use Cloudstream's normal plugin update mechanism; select **Muhn Pace v3 Preview**, open **One Piece (Muhn Pace)**, choose **Season 12 Dressrosa**, inspect positions 30–37. No uninstall/data reset is needed for the update. The new unified series starts separate progress; old preview arc routes/keys remain available. No migration promise.
- Rechecked remote production `main` **`8ade241e95405d91c0606bd3ea6080ac0709992a`** and `builds` **`cbe8d9dce96169da7ed12fefb8adc3787779bab3`**. All three public production repository files are byte-identical to that builds commit. Production installation URL, package and manifest are unchanged.
- **STOP here.** The user must test the new layout, image framing, episode descriptions and actual persisted resume on Fire TV. No assistant device test occurred. Do not begin remaining Dressrosa/other-arc research, B/C, intro-skipping implementation, unrelated optimization or Phase 5 without further authorization. Native skip buttons exist in the host, but the resolved public provider API cannot supply exact-file segment times.

## Phase 4A validation follow-up

Implementation `1b44ee34c98d966f4d0362e1ae81e045b5a56791` passed all local checks and both local repository/package validators. Development [run 43776844521](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43776844521) failed before Kotlin compilation: the new image audit imported Pillow, but `tests/requirements.txt` only installed PyYAML. Added the explicitly tested Pillow version as a **development test dependency**, with no extension/runtime dependency change. The corrected run succeeded before publication, as recorded above.

## Phase 4A A1/A2 — locally validated

- Recovered `10fbf54` and preserved unfinished work in checkpoint `fbac35fa64a652f7be13e44485f9ba42b4e5a3fa`, pushed to development. Full [architecture, mapping, artwork and source findings](PHASE4A_DRESSROSA.md).
- Home/search now expose **One Piece (Muhn Pace)** once. Sixteen named native collection seasons retain all 274 streams; Dressrosa is season 12. Fishman Island's separately supplied alternate scene is season 10, adjacent to the main collection.
- Legacy arc routes, stream callback, provider/plugin identity and old season/episode history inputs remain intact for saved entries. Unified-series progress starts separately; no history migration/reset. Native episode indices remain navigation positions because partial/overlapping original numbering breaks host sorting/deduplication. No internal edit number is inserted into provider-authored titles/descriptions.
- Eight One Pace titles, eight individual story synopses and eight distinct exact-file interior stills. Coverage: **0 DIRECTLY_VERIFIED, 3 ONE_PACE_DERIVED, 5 CONFLICTING, 266 UNKNOWN**. `White Town - 701-702` is derived, with partial scene evidence for 701 recorded separately. Conflicting ranges are not displayed as established facts; unresearched titles use explicit unknown placeholders.
- New temporary main series art uses the recovered Law portrait and Luffy/Law background. Ten images are pinned to the recovery commit. Source/licensing limits remain explicit; no TMDB secret or unauthorized image mirror/hotlink. All eight formerly repeated thumbnails are replaced; the other 266 are outside this audit.
- **45 Kotlin/JUnit tests** (41 main + 4 preview), **20 Python checks**, Android compilation/DEX/`.cs3` creation pass locally. [Live API/image check](research/phase4a-live-validation.json) verifies 16 collections, all 274 baseline IDs/filenames, eight media-reported hashes and ten image URLs/dimensions/hashes. `amap` replaced unavailable direct coroutine imports after the first compile failed; no dependency was added.
- [Actual library/TV API inspection](CLOUDSTREAM_METADATA.md#phase-4a-native-seasons-and-skip-segments) confirms native season names and host indices. The paused plot remains series-level; Continue Watching lacks episode-title/range fields. Native skip buttons exist in the host but no public provider timestamp API is exposed. No unsupported seeking workaround or standard-anime skip-time lookup was added.
- Preview version is **2**, retaining `MuhnPacePreview`, `com.muhnpacepreview`, **Muhn Pace v3 Preview** and the existing repository URL. No Fire TV visual or persisted-resume test of this build has occurred. `main` and `builds` remain unchanged.

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
