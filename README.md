# Muhn Pace Direct (English Dub)

A Cloudstream Beta provider for the English-dubbed Muhn Pace catalogue on Pixeldrain. It uses public HTTPS list and file requests. No VPN, Real-Debrid account, torrent client or proxy is required by the extension.

## Install on Fire TV

1. Open **Cloudstream Beta → Settings → Extensions → Add repository**.
2. Enter the repository URL below. Give it the name **Muhn Pace** if prompted.
3. Open the repository and install **MuhnPace**. On the home screen select **Muhn Pace Direct (English Dub)**, then choose an arc and episode.

```text
https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/builds/repo.json
```

## Catalogue and playback

The provider reads the 16 collections in the [community dub guide](https://steamcommunity.com/sharedfiles/filedetails/?id=3685024934). It keeps numbered Muhn Pace edits and explicitly dub-labelled episodes, filters out subtitled-only and comparison files, and sorts episodes numerically. Clean titles use the arc and edit number, such as **Dressrosa • Episode 2**. The numbers refer to the fan edit, and gaps are preserved. For example, Wano Act 3 begins at edit 71.

Original One Piece episode mappings are explicitly marked **unverified** until a source verifies the exact edit. Filename version, resolution and unlabelled range tags are not treated as original anime episode numbers.

On the development branch, all 16 collections have original arc-themed posters (768×1152), widescreen backgrounds (1920×1080), distinct synopses, anime/adventure tags and the live number of available edits. The [original geometric artwork](assets/artwork/README.md) is a deliberate fallback, not official anime promotional art. Its HTTPS URLs are pinned to a verified artwork commit.

Episodes use Pixeldrain's thumbnail for the exact selected edit, with factual English-dub/edit descriptions. The host supplies small 256×144 previews, which can show opening credits. Missing, mismatched and eight known black-frame previews use the matching arc background. Cloudstream handles image caching and rendering. Phase 2 is implemented on the development branch; real Fire TV layout verification and any production publication remain separate.

When several versions share an episode number within a collection, the provider prefers the regular edit. The dedicated Fishman Island episode 15 collection also appears in the guide. Its dub can appear in the main Fishman Island list too.

The video host controls availability and transfer limits. If the host blocks a request on your connection, the app may show a playback error. The plugin uses Pixeldrain directly and contains no video files. [Pixeldrain API documentation](https://pixeldrain.dev/api)

## Build

The development workflow validates pushes to `codex/**` and pull requests to `main`. It runs Kotlin unit tests, compiles the Android extension, and checks the package and indexes. It has read-only repository permissions, stores test reports and build artifacts, and has no publication step.

The separate production workflow can publish only from `main`. Its job and publishing step both require `refs/heads/main`. Development validation does not publish an extension. The production repository contains these three files:

| File | Purpose |
| --- | --- |
| `repo.json` | Install URL and link to the extension index |
| `plugins.json` | Extension version, package URL, size and SHA-256 |
| `MuhnPace.cs3` | Android DEX bytecode and Cloudstream plugin manifest |

To build locally, use Gradle 8.12 (or the Gradle wrapper from the pinned Cloudstream template in the workflow), JDK 17 and Android SDK 35. From the project root, run:

```sh
python3 -m pip install -r tests/requirements.txt
python3 -m unittest discover -s tests -v
gradle :MuhnPace:testDebugUnitTest :MuhnPace:make --no-daemon
python3 scripts/publish.py --build-dir MuhnPace/build --repo lincutto/MuhnPaceCloudstream --out published
```

`scripts/publish.py` assembles local files only; it makes no network requests. Kotlin tests use saved catalogue fixtures and do not request media. See [the v3 handover](docs/V3_HANDOVER.md) for recovery details, Phase 2 delivery, validation evidence, and limitations.
