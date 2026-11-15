# v3 Fire TV preview

Revised Phase 4A A1–A3 preview: **one series / sixteen native collection seasons**, with improved Dressrosa samples. Version **2** is published and its exact repository/index/package URLs were verified anonymously on 2026-11-15. [Development CI passed](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43776953340); full [publication evidence](research/phase4a-preview-publication.json) and [handover](V3_HANDOVER.md) identify the build. No production release. An Actions artifact ZIP is not a Cloudstream repository.

Published source: `578a7a0059bf44c874db3e01feb24f6ee7d128c7`; preview commit: `b3829bcc1eaecda75808e65fda7b7c360439e0b3`. Package SHA-256: `196d3e5def1db58803a5bdf976bdc3169751fe4b06fc192417b69ac924bd84ce` (45,376 bytes).

## Update the existing preview

Keep the existing preview repository URL:

```text
https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json
```

1. Keep **MuhnPace** and **Muhn Pace v3 Preview** installed. Do not clear Cloudstream data or reset history.
2. Fully close and reopen Cloudstream Beta with automatic plugin updates enabled. The host's normal startup updater detects the preview version increase from 1 to 2. If your Beta exposes **Update plugins**, that is its manual update control; UI wording/location varies by build. Some older TV extension rows only have a trash/remove action, which is not an update button.
3. In **Settings → Extensions → Muhn Pace v3 Preview**, look for version **2**. Return to Home, choose provider **Muhn Pace v3 Preview**, then reload its home page. There should be **one** `One Piece (Muhn Pace)` card. Reopening the provider or restarting refreshes stale catalogue/image caches; do not erase app storage.
4. Open the series. Inspect the Law portrait, Luffy/Law background and native season selector. Select **Season 12 Dressrosa**. The selector should offer sixteen seasons; the [season map](PHASE4A_DRESSROSA.md#recovery-and-architecture) records all of them.
5. Inspect positions **30–37**, especially **White Town - 701-702**. The three accepted ranges are One Pace-derived; five other sample rows explicitly say the range is unresolved. Check distinct stills, story synopses, truncation, remote focus and episode selection. Other entries have honest unknown placeholders until their metadata is researched.
6. Play a sample, pause, and inspect the controls. With **Show Player Metadata Overlay** enabled, wait about eight seconds without input for the separate overview. It reads the series plot and cannot automatically switch to the active arc's synopsis. Exit with Back and reopen the **new unified preview** Continue Watching card to test resume. No native Skip Intro button is supplied by this preview; the resolved provider API cannot pass segment timestamps.
7. Check that an existing production history entry and an old preview arc entry still retain their progress. Old preview arc routes stay loadable; new unified-series progress is separate and is not migrated. Only a real device check can establish persisted behaviour for your installed Beta.

If the repository is missing, add the exact URL in **Settings → Extensions → Add repository**, then install **Muhn Pace v3 Preview**. The iPhone Amazon Fire TV app's keyboard may help enter the URL. No computer or APK sideload is required. Do not add another guessed repository URL or install an Actions artifact ZIP.

To remove the temporary preview after testing, uninstall **only Muhn Pace v3 Preview** and remove **only that repository**. Leave **MuhnPace**, its repository and app data intact. Removal may leave cached preview history cards; no automatic cleanup or transfer to a final release is promised.

## Isolation and history contract

| Identity | Production | Preview v2 |
| --- | --- | --- |
| Plugin/module/file | MuhnPace / MuhnPace.cs3 | MuhnPacePreview / MuhnPacePreview.cs3 |
| Namespace/plugin class | com.muhnpacedirect / MuhnPacePlugin | com.muhnpacepreview / MuhnPacePreviewPlugin |
| Provider | Muhn Pace Direct (English Dub) | Muhn Pace v3 Preview |
| Artifact repository | builds | v3-preview |
| Existing arc routes | /l/&lt;id&gt; | /muhnpace-v3-preview/l/&lt;id&gt; (history compatibility only) |
| Unified series route | No production publication | /muhnpace-v3-preview/one-piece |

Version 2 replaces only the preview plugin in its existing repository/plugin path. It does not overwrite production. The new catalogue architecture necessarily uses a new series/history parent; old entries are not rewritten. The real library builders, plugin registration and inspected host key calculations are covered by 45 Kotlin tests. Those checks are not an Android preferences/device test.

`-Ppreview=true` generates a relocated preview from shared development source with checked substitutions. Production publication scripts/workflow and the production installation URL are unchanged. Development CI remains read-only and validates both builds and the isolated manifest, DEX, URLs, hashes and source provenance. Only four preview files are published to `v3-preview` after validation.

Artwork is limited-excerpt exact-file anime imagery, not a rightsholder-licensed promotional collection; [sources/rights](../assets/phase4a/README.md). Further work waits for the user's Fire TV feedback.
