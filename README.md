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

Production **version 8** includes 30 reviewed English-dubbed Egghead edits in release order. The first 22 are One Pace edits, followed by six Kuma Cut and two Shaved Egghead edits. Each has a description, a thumbnail from that edit and its original anime episode numbers. The final edit covers episodes 1139–1140.

The catalogue has **15 arcs**. Wano has 51 edits in one arc. Fish-Man Island lists the corrected group-shot edit once. The archived source lists yield 303 entries. At release, the live lists yield 302 because the host removed Fish-Man Island’s *10,000 Meters Under the Sea* before v8. The remaining edits keep their navigation positions. Original anime numbers appear in the titles, including nonconsecutive ranges.

The video host controls availability and transfer limits. The plugin uses Pixeldrain directly and contains no video files. [Pixeldrain API documentation](https://pixeldrain.dev/api)

## Updates and saved progress

Cloudstream updates the existing `MuhnPace` package at the repository URL above. The provider name, plugin class, series URL and legacy arc URLs are the same as v7. Wano Act 3 moves from season 16 to Wano positions 27–51. Cloudstream uses season and episode positions for resume history, so those 25 edits may need their positions restored manually.

Egghead Dev v5 has its own package, provider and series URL and can stay installed alongside v8. The release uses the reviewed catalogue and English-only resolver from the [Fire TV-tested development source](https://github.com/lincutto/MuhnPaceCloudstream/commit/5973689bae40dc681029c4f6671c39f83f97d56b). The v7 [source](https://github.com/lincutto/MuhnPaceCloudstream/tree/release/production-v7) and [compiled package and indexes](https://github.com/lincutto/MuhnPaceCloudstream/tree/rollback/production-v7-builds) are preserved for rollback.

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
