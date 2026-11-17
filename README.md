# Muhn Pace Rework (by lincutto)

An unofficial Cloudstream extension maintained by **lincutto** for the English-dubbed **Muhn Pace** fan-editing project by **Muhny**. Editing credit belongs to the original Muhn Pace project; this extension is not an official Muhn Pace, One Piece or Cloudstream release. One Piece belongs to Eiichiro Oda/Shueisha and Toei Animation. It uses public HTTPS list and file requests. No VPN, Real-Debrid account, torrent client or proxy is required by the extension.

## Install on Fire TV

1. Open **Cloudstream Beta → Settings → Extensions → Add repository**.
2. Enter the repository URL below. Give it the name **Muhn Pace** if prompted.
3. Open the repository and install **Muhn Pace Rework (by lincutto)**. On the home screen select **Muhn Pace Rework (by lincutto)**, then open **One Piece (Muhn Pace)** and choose a season and episode.

```text
https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/builds/repo.json
```

## Catalogue and playback

Production **version 7** (internal version code **7**) promotes the validated preview v6 catalogue: **274 playable episodes across 16 seasons**, with the same ordering, titles, descriptions, thumbnails, poster, background and Pixeldrain HTTPS playback URLs. The home row remains **One Piece • Muhn Pace (English dub)**.

The 25 Wano Act 3 titles retain the approved approximate original-anime ranges. These are provisional display references, not independently verified exact Muhn Pace boundaries. Native season/episode numbers identify navigation positions, not original anime episode numbers.

The video host controls availability and transfer limits. The plugin uses Pixeldrain directly and contains no video files. [Pixeldrain API documentation](https://pixeldrain.dev/api)

## Updates and saved progress

The production package identity remains `MuhnPace`, class `com.muhnpacedirect.MuhnPacePlugin`, at the same package URL. Version 7 is newer than production version 2. The provider name is now **Muhn Pace Rework (by lincutto)**; Cloudstream also uses that name to resolve saved entries. Old favourites or Continue Watching entries may require manually reopening/re-adding the series and restoring your position. There is no automatic history migration.

Preview v6 remains unchanged at `v3-preview` as a separately installed fallback. Preview history does not automatically transfer to production. Legacy production arc routes remain supported, but the unified series has separate progress keys. Keep the preview and existing app data until any progress you need has been transferred manually.

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

`scripts/publish.py` assembles local files only; it makes no network requests. Kotlin tests use saved catalogue fixtures and do not request media. See [the v3 handover](docs/V3_HANDOVER.md) for historical validation evidence and limitations. Its earlier preview-only publication restrictions are superseded by this authorised production release.
