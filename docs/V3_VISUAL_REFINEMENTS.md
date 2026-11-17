# Preview v5 visual refinements

## Playback information

**No safe provider-only override for the complete overlay line.** Focused inspection of Cloudstream `2dcc25c3945c61dfa95d1710ced7c74acfe84baa` shows:

- [`GeneratorPlayer.setPlayerDimen`, lines 1944–1960](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1944) joins the selected `ExtractorLink.name` with the player's actual video dimensions. `Pixeldrain • Direct HTTPS` comes from this extension's link name. `1920x1080` comes from the decoded video's size, not the extension's `Qualities.Unknown` metadata.
- An empty link name would remove the first component, but still leave the resolution. It would also create an unnamed row in the source selector: the client uses the same name plus the quality label there ([lines 1134–1142](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/player/GeneratorPlayer.kt#L1134)); unknown quality has an empty label. The public `ExtractorLink` API has no distinct overlay-only name/visibility field. Keep the useful source-menu identity and stream fields intact.
- Cloudstream already supports hiding the complete line through **Settings → Player → Layout → Show player information**: deselect **Source Name** and **Resolution**; leave **Media Info** off if no technical details are wanted. Reopen the player so the settings are reloaded. The exact menu grouping can vary with the installed Beta. This does not hide the separate episode title, alter video resolution, reset progress, or change the source menu.

The existing setting is implemented in [`SettingsPlayer.kt`, lines 118–147](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/settings/SettingsPlayer.kt#L118) using `title_info_pref_names`/values and the `show_name_key` / `show_resolution_key` preferences. `GeneratorPlayer` reads those preferences when the player opens. No Cloudstream modification, hidden preference write, stream-name trick or quality misreporting is used. The assistant cannot change the user's Fire TV settings remotely, and does not claim the line has disappeared on that device.

## Filled portrait poster

The received replacement `IMG_9122.jpeg` is **1280×718 landscape**, SHA-256 `193481d2d4ac36df3f57efdbdc0c2883799da4118a6c272cf88acd0a7474f4b0`. It supersedes the v4 poster source. Source and all supplied screenshots were inspected. The reference card contains extra sky/lower artwork absent from this source; retaining the entire landscape cannot fill its portrait frame without padding, distortion or invented content.

The user selected the central portrait crop after the crew-loss trade-off was explained. Crop **[399, 0, 877, 717]**, with exclusive right/bottom coordinates, gives **478×717**. Proportional LANCZOS resize gives **768×1152**, JPEG quality 92, 4:4:4. It fills the frame with Luffy, Zoro and Sanji prominent; most outer crew members are excluded, with partial figures at the edges. Only 37.3% of source width is retained. No blur, empty padding, stretching, regenerated characters or other artwork was added. Upscaling cannot add source detail.

The original source, exact crop, export hash and reproduction command are recorded in [the artwork manifest](../assets/phase4b/manifest.json) and [README](../assets/phase4b/README.md). Artwork checkpoint: `52051fde43afbf13f21231dd38b712f49a8b94a6`. The poster gets its own immutable commit pin; the background retains its **exact v4 URL and bytes**. All 274 thumbnails, episode metadata, seasons, indices, URLs, names and history inputs are unchanged.

## Validation and publication

The final source uses isolated preview version **5**. Existing Python checks, Kotlin/JUnit regression tests, Android/DEX builds and package validation must pass before publication. Reuse that successful CI artifact; no second build is needed. Actual publication results are recorded in the handover after public-byte verification.

Known-good fallback remains v4 preview commit `3317cfccfbf5d81f43ae6c0182f4b75ade96474d`, source `8f3e2f8de65a4b6c2385b0487113ecb7954af859`. No production branch changes or Phase 6. New Fire TV appearance, source-menu operation and persisted resume remain device checks; only the user's v4 feedback has been observed.
