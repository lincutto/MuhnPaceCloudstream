# v3 Fire TV preview

**Preview version 3 is published.** It preserves the working version 2 unified series and sixteen named seasons, adds crew artwork, and fills the baseline catalogue's story metadata. The exact public repository → index → package chain was verified on 2026-11-16. [Successful development CI](https://github.com/lincutto/MuhnPaceCloudstream/actions/runs/43832261690), [publication proof](research/phase4b-preview-publication.json), [live image/file checks](research/phase4b-live-validation.json), and [coverage/limitations](PHASE4B_METADATA.md).

Validated source: `ffc1f51d98916c1ac4fa60f6a67e27c2df0da738`. Preview branch commit: `1dcb77c446a763adcad74e01ac71403192243975`. Package: **92,180 bytes**, SHA-256 **`43c3704007b0ffab045109c2c1f08d1adfe73ff343c44cd97b63d1edbd9a090c`**. The locally validated artifact was published; CI independently built/validated the same source. No byte-identical local/CI build claim is made.

## Update the existing preview on Fire TV

Keep the existing repository URL:

```text
https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/v3-preview/repo.json
```

1. Leave the production extension and **Muhn Pace v3 Preview** installed. No uninstall, app-data clearing or history reset is needed.
2. Fully close and reopen Cloudstream Beta with automatic plugin updates enabled. The normal updater should detect version **2 → 3**. If your Beta has **Update plugins**, you can use it; the exact location/wording varies. A trash icon is a removal control, not an update control.
3. Check **Settings → Extensions → Muhn Pace v3 Preview** for version **3**. Choose provider **Muhn Pace v3 Preview**, return to Home and reopen **One Piece (Muhn Pace)**. Leave an already-open player/result page first so it reloads its episode names and images.
4. Inspect the new vertical crew poster and different widescreen crew background. Open the native season selector: the same sixteen named seasons should remain. Check Dressrosa (season 12), including `White Town - 701-702`, `To Russian, with Love - 714-716` and `Zoro vs. Pica - 716-719`, then sample other seasons for image relevance, synopsis readability and remote focus.
5. Play a preview episode and pause. The controls can show the new story title/range after Cloudstream's series and S/E prefix. With **Show Player Metadata Overlay** enabled, waiting about eight seconds without input displays the separate concise series overview. The provider cannot make that idle panel dynamically show each episode's synopsis.
6. Exit with Back and resume through the same **preview** Continue Watching entry. Version 3 retains version 2's parent URLs, season positions, file IDs and provider identity. Check that saved progress and next/previous selection remain correct on your device. Production history is separate; no preview-to-production progress transfer is promised.
7. Check an existing production item and any saved legacy preview arc route. These remain separate and have not been rewritten. Do not clear data to refresh an old image or title; reopen the series or restart normally.

If the preview repository is missing, add the exact URL in **Settings → Extensions → Add repository**, then install **Muhn Pace v3 Preview**. The iPhone Fire TV remote app's keyboard can help enter the URL. No computer, APK sideload or Actions-artifact download is required.

To remove it later, uninstall **only Muhn Pace v3 Preview** and remove **only its preview repository**. Keep the production extension, production repository and Cloudstream app data. Cached preview history entries may remain; no automatic cleanup is performed.

## Coverage and limits

- 274 populated titles: 249 published One Pace titles with derived original coverage; 25 late-Wano editorial scene labels without fabricated ranges.
- 274 story descriptions, including 25 limited to sampled late-Wano story threads. The two identical-media Fishman IDs intentionally share one description; 273 distinct texts.
- 274 individually associated, distinct anime thumbnails. No baseline generic fallbacks or near-duplicate pairs at dHash distance ≤5. All 276 published image URLs were fetched and decoded with matching hashes/dimensions.
- Zero directly verified full anime mappings; 249 derived, 25 unknown, zero unresolved admitted conflicts. Unknown range/confidence notes stay internal, not in viewer descriptions.
- Continue Watching uses the host's series card, season/position and saved progress; it has no separate episode-title/range field. Cached art/titles can take a reload to refresh. Host layout/settings can still truncate long published titles.
- Crew poster credited upstream to **juniormarcos on TMDB**, background to **warlinct on TMDB**, via the pinned addon's source images. These are distinct compositions of the original five Straw Hats. [Provenance and rights limitations](../assets/phase4b/README.md): no rightsholder artwork licence is claimed or established.
- No new native Skip Intro API was invented, no standard-anime timing was applied to fan edits, and no Cloudstream fork was made.

## Isolation

| Identity | Production | Preview v3 |
| --- | --- | --- |
| Plugin/file | MuhnPace / MuhnPace.cs3 | MuhnPacePreview / MuhnPacePreview.cs3 |
| Namespace/plugin | com.muhnpacedirect / MuhnPacePlugin | com.muhnpacepreview / MuhnPacePreviewPlugin |
| Provider | Muhn Pace Direct (English Dub) | Muhn Pace v3 Preview |
| Artifact branch | builds | v3-preview |
| Legacy arc routes | /l/&lt;id&gt; | /muhnpace-v3-preview/l/&lt;id&gt; |
| Unified route | No production publication | /muhnpace-v3-preview/one-piece |

The v2 → v3 update changes presentation while preserving these preview identities. The earlier v1 → v2 unified-series transition created a new parent; those legacy arc routes still work, without automatic history migration. All 48 Kotlin/JUnit tests, 24 Python checks and both Android/package builds passed; development CI succeeded. Main/builds heads and the public production manifest/index/package bytes remain unchanged. No assistant Fire TV visual, playback or persisted-resume testing occurred. Stop here for the user's feedback; no Phase 5.
