# v3 Fire TV preview

This is a separate temporary installation for visual acceptance of Phases 1–3. Phase 4 has not begun.
Both milestones are complete: the source/build milestone was committed, pushed and passed development CI before the separate preview-only publication. An Actions artifact ZIP is **not** an installable Cloudstream repository.

## Install on Fire TV

Verified anonymously on 2026-11-15: this manifest, its plugin index and `.cs3` download all returned HTTP 200, and the downloaded package passed manifest/DEX/identity/hash/size checks.

```text
https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json
```

1. Leave the existing **MuhnPace** extension installed. Open **Cloudstream Beta → Settings → Extensions → Add repository**.
2. Enter the URL above, with optional repository name **Muhn Pace v3 Preview**. You can use the iPhone Amazon Fire TV app's keyboard when the URL field is focused, if available, or enter it with the Fire TV remote. No computer or sideloaded APK is required.
3. Open the **Muhn Pace v3 Preview** repository and install **Muhn Pace v3 Preview**. The production extension is still **MuhnPace**, with provider **Muhn Pace Direct (English Dub)**.
4. Return to Home and select provider **Muhn Pace v3 Preview**. Its arc cards and loaded titles end in **(Muhn Pace Preview)**. Start testing from these cards, not an existing production Continue Watching card.
5. Open arcs to inspect posters, backgrounds and descriptions. Focus episode rows and open the player's episode-selection list to inspect thumbnails, titles and descriptions where your Beta layout exposes them. Test remote focus, scrolling, Back and episode selection.
6. Play a preview edit briefly, pause, and inspect the controls. If **Show Player Metadata Overlay** is enabled, wait about eight seconds without input for the separate paused overlay. Exit with Back, inspect the preview Continue Watching card, and reopen it to test resume. Confirm your original provider's saved progress still exists. No preview progress migration is promised.
7. To remove it, open **Settings → Extensions**, uninstall **only Muhn Pace v3 Preview**, then remove **only its preview repository**. Keep **MuhnPace** and the production repository. Do not clear app data or reset history. Separate preview history cards may remain cached after uninstall.

Published build: source `4526260c8e78a3c7dac973a6a9c20c14b14193fd`; [successful development CI](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43747049494); artifact branch commit `6d28eb858bed6933ccfd6677f94d6c6395c5f7c6`. Package SHA-256: `437953f02bc059ab8f605a386876e94182a5b6d02473becbbd1f0bcca350be16` (27,903 bytes). The published file is the validated local build of that source commit; CI independently validated the same source. Connector artifact download returned HTTP 403, so the CI ZIP was not used for publication.

## Isolation established from Cloudstream source

Inspected Cloudstream [5d66bbeaf3](https://github.com/recloudstream/cloudstream/tree/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c), matching the previously resolved library. The installed Fire TV Beta revision is unknown; no device installation or playback test has occurred.

- [PluginManager](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/plugins/PluginManager.kt#L735) creates installed paths using both repository URL and internal plugin name. Loading is keyed by absolute file path, with a separate class loader and plugin URL. Updates match internal name **and** repository-derived installed path. Deleting/unloading one plugin removes that plugin's file/registrations, not playback-history storage.
- [APIHolder](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/library/src/commonMain/kotlin/com/lagradost/cloudstream3/MainAPI.kt#L134) resolves providers by name. A distinct provider name is necessary but insufficient for history isolation.
- [ResultViewModel2](https://github.com/recloudstream/cloudstream/blob/5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L370) hashes the collection's `uniqueUrl` after removing the provider main URL and slashes. TV episode IDs add season × 100000 + edit number + 1. Therefore the preview also uses distinct catalogue routes; merely renaming the plugin would share production progress.

| Identity | Production | Preview |
| --- | --- | --- |
| Plugin internal name / filename | `MuhnPace` / `MuhnPace.cs3` | `MuhnPacePreview` / `MuhnPacePreview.cs3` |
| Provider name | Muhn Pace Direct (English Dub) | **Muhn Pace v3 Preview** |
| Kotlin/Android namespace | `com.muhnpacedirect` | `com.muhnpacepreview` |
| Plugin class | `MuhnPacePlugin` | `MuhnPacePreviewPlugin` |
| Provider class | `MuhnPaceProvider` | `MuhnPacePreviewProvider` |
| Repository artifact branch | `builds` | `v3-preview` |
| Version | 2 | 1, independent version track |
| Collection URL / unique URL path | `/l/<collection>` | `/muhnpace-v3-preview/l/<collection>` |
| Series/player/history display suffix | `(Muhn Pace)` in v3 development | `(Muhn Pace Preview)` |

The virtual preview collection URLs are app navigation identities, not public Pixeldrain web pages. `load()` resolves them to the unchanged `/api/list/<collection>` endpoint. All playback `/api/file/<id>` URLs, selected file IDs, seasons, edit numbers, sorting, episode titles, descriptions and artwork are shared unchanged. Preview explicitly rejects production collection routes.

All baseline production and preview series/episode history keys are checked for collisions, including both Pixeldrain hosts and provider-not-loaded URL hashing. This supports side-by-side isolation for this catalogue, not a universal guarantee about every third-party provider's 32-bit hashes. Preview progress starts separately and is not promised to migrate to a final release. The extension never edits or deletes Cloudstream history/preferences.

## Build and validation

Production Kotlin files, production packaging script and production workflow remain untouched. `-Ppreview=true` opts into `MuhnPacePreview`; the normal production build does not include it. `scripts/prepare_preview.py` generates relocated sources under `MuhnPacePreview/build/`, making narrowly checked identity substitutions. It fails if an expected source pattern changes; generated files are not committed. The stream callback remains byte-for-byte identical. Shared code avoids a second manually maintained provider implementation.

```sh
gradle -Ppreview=true :MuhnPace:testDebugUnitTest :MuhnPace:make :MuhnPacePreview:testDebugUnitTest :MuhnPacePreview:make --no-daemon
python3 -m unittest discover -s tests -v
python3 scripts/assemble_preview.py --out published-preview --source-sha <full-source-commit>
```

Development CI remains read-only and now builds/tests both modules and validates the isolated manifest, DEX class namespace, routes, package hash/size and preview-only index URLs. Its preview artifact contains only `MuhnPacePreview.cs3`, `repo.json`, `plugins.json` and build provenance. Production artifacts remain in a separate CI upload. Publishing requires a separate explicit Git push to the preview artifact branch after milestone 1 succeeds.

Preview tests use the real Cloudstream plugin registration and response builders. Android logging uses the Android unit-test default stub; these tests do not execute Android's plugin-file loader, preferences, video player or Fire TV UI. The existing 32 regression tests still run separately.

## Fire TV acceptance scope

Check all arc posters/backgrounds, plot text, episode images/fallbacks, descriptions, clean titles, remote focus/navigation, player control title, delayed paused overlay and Continue Watching. Cloudstream's delayed TV metadata overlay requires its setting enabled and about eight seconds paused without interaction. Its fields/limits remain those in [CLOUDSTREAM_METADATA.md](CLOUDSTREAM_METADATA.md).

Select only the clearly named preview provider and cards ending `(Muhn Pace Preview)` when creating test progress. Return to the existing production provider to confirm its previous progress still exists. Do not uninstall production, clear app data or reset history. Removing the preview extension/repository does not promise automatic cleanup of its separate cached history entries.
