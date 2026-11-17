# v3 Fire TV preview

**Preview version 4 is published.** It adds the supplied 768×1152 portrait poster with dark blurred padding, the unchanged supplied 1280×721 background, and home-row heading **One Piece • Muhn Pace (English dub)**. All 274 episodes, sixteen seasons, metadata, thumbnails, playback and preview history inputs are preserved. The public repository → index → package chain and both artwork URLs were verified on 2026-11-17. [Successful source CI](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43943800055), [v4 publication proof](research/preview-v4-publication.json), [metadata coverage](PHASE4B_METADATA.md).

Validated source: `8f3e2f8de65a4b6c2385b0487113ecb7954af859`. **Known-good preview/fallback commit: `3317cfccfbf5d81f43ae6c0182f4b75ade96474d`.** Package: **92,215 bytes**, SHA-256 **`3cb3e58d6dd24b1472f2452c13ff23f5d52a89560903869349599d3c59a00700`**. Published the existing successful CI artifact for that exact source; public bytes match it. No rebuild was needed.

## Update the existing preview on Fire TV

Keep the existing repository URL:

```text
https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json
```

1. Leave the production extension and **Muhn Pace v3 Preview** installed. No uninstall, app-data clearing or history reset is needed.
2. Fully close and reopen Cloudstream Beta with automatic plugin updates enabled. The normal updater should detect version **3 → 4**. If your Beta has **Update plugins**, you can use it; the exact location/wording varies. A trash icon is a removal control, not an update control.
3. Check **Settings → Extensions → Muhn Pace v3 Preview** for version **4**. Choose provider **Muhn Pace v3 Preview**, return to Home and reopen **One Piece (Muhn Pace)**. Leave an already-open player/result page first so it reloads its episode names and images.
4. Inspect the new vertical crew poster and different widescreen crew background. Open the native season selector: the same sixteen named seasons should remain. Check Dressrosa (season 12), including `White Town - 701-702`, `To Russian, with Love - 714-716` and `Zoro vs. Pica - 716-719`, then sample other seasons for image relevance, synopsis readability and remote focus.
5. Play a preview episode and pause. The controls can show the new story title/range after Cloudstream's series and S/E prefix. With **Show Player Metadata Overlay** enabled, waiting about eight seconds without input displays the separate concise series overview. The provider cannot make that idle panel dynamically show each episode's synopsis.
6. Exit with Back and resume through the same **preview** Continue Watching entry. Version 4 retains versions 2 and 3's parent URLs, season positions, file IDs and provider identity. Check that saved progress and next/previous selection remain correct on your device. Production history is separate; no preview-to-production progress transfer is promised.
7. Check an existing production item and any saved legacy preview arc route. These remain separate and have not been rewritten. Do not clear data to refresh an old image or title; reopen the series or restart normally.

If the preview repository is missing, add the exact URL in **Settings → Extensions → Add repository**, then install **Muhn Pace v3 Preview**. The iPhone Fire TV remote app's keyboard can help enter the URL. No computer, APK sideload or Actions-artifact download is required.

To remove it later, uninstall **only Muhn Pace v3 Preview** and remove **only its preview repository**. Keep the production extension, production repository and Cloudstream app data. Cached preview history entries may remain; no automatic cleanup is performed.

## Coverage and limits

- 274 populated titles: 249 published One Pace titles with derived original coverage; 25 late-Wano editorial scene labels without fabricated ranges.
- 274 story descriptions, including 25 limited to sampled late-Wano story threads. The two identical-media Fishman IDs intentionally share one description; 273 distinct texts.
- 274 individually associated, distinct anime thumbnails, unchanged from v3 and covered by its existing verification. Both new series artwork URLs were freshly fetched and decoded with matching hashes/dimensions; no unnecessary repeat of the thumbnail audit.
- Zero directly verified full anime mappings; 249 derived, 25 unknown, zero unresolved admitted conflicts. Unknown range/confidence notes stay internal, not in viewer descriptions.
- Continue Watching uses the host's series card, season/position and saved progress; it has no separate episode-title/range field. Cached art/titles can take a reload to refresh. Host layout/settings can still truncate long published titles.
- Picture 1 is the supplied poster, proportionally scaled with dark blurred padding; Picture 2 is the supplied background, copied unchanged. [Recorded provenance and hashes](../assets/phase4b/manifest.json).
- No new native Skip Intro API was invented, no standard-anime timing was applied to fan edits, and no Cloudstream fork was made.

- The **48 Episodes** label counts the selected Dressrosa season, not all 274 playable entries. Cloudstream exposes no supported override for that label or a display-only provider name; **Muhn Pace v3 Preview** remains unchanged to preserve routing/history. [Focused API findings](V3_UI_LIMITATIONS.md).

## Isolation

| Identity | Production | Preview v4 |
| --- | --- | --- |
| Plugin/file | MuhnPace / MuhnPace.cs3 | MuhnPacePreview / MuhnPacePreview.cs3 |
| Namespace/plugin | com.muhnpacedirect / MuhnPacePlugin | com.muhnpacepreview / MuhnPacePreviewPlugin |
| Provider | Muhn Pace Direct (English Dub) | Muhn Pace v3 Preview |
| Artifact branch | builds | v3-preview |
| Legacy arc routes | /l/&lt;id&gt; | /muhnpace-v3-preview/l/&lt;id&gt; |
| Unified route | No production publication | /muhnpace-v3-preview/one-piece |

The v2 → v3 → v4 updates change presentation while preserving these preview identities. The earlier v1 → v2 unified-series transition created a new parent; those legacy arc routes still work, without automatic history migration. All 50 Kotlin/JUnit tests, 25 Python checks and both Android/DEX/package builds passed for the published source; existing successful CI evidence was reused. The main/builds heads remain unchanged; neither production branch was written. No assistant Fire TV visual, playback or persisted-resume testing occurred. Stop here for the user's feedback; no Phase 5.
