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

The provider reads the 16 collections in the [community dub guide](https://steamcommunity.com/sharedfiles/filedetails/?id=3685024934). It keeps numbered Muhn Pace edits and explicitly dub-labelled episodes, filters out subtitled-only and comparison files, and uses the episode numbers in the filenames. The numbers refer to the fan edit. For example, Wano Act 3 begins at edit 71.

When several versions share an episode number within a collection, the provider prefers the regular edit. The dedicated Fishman Island episode 15 collection also appears in the guide. Its dub can appear in the main Fishman Island list too.

The video host controls availability and transfer limits. If the host blocks a request on your connection, the app may show a playback error. The plugin uses Pixeldrain directly and contains no video files. [Pixeldrain API documentation](https://pixeldrain.dev/api)

## Build

The GitHub Actions workflow builds the Kotlin provider against the Cloudstream library with JDK 17, checks the package and its index, and publishes three files to `builds`.

| File | Purpose |
| --- | --- |
| `repo.json` | Install URL and link to the extension index |
| `plugins.json` | Extension version, package URL, size and SHA-256 |
| `MuhnPace.cs3` | Android DEX bytecode and Cloudstream plugin manifest |

To build locally, use Gradle 8.12 (or the Gradle wrapper from the pinned Cloudstream template in the workflow), JDK 17 and Android SDK 35. From the project root, run:

```sh
gradle :MuhnPace:make
python3 -m unittest discover -s tests -v
python3 scripts/publish.py --build-dir MuhnPace/build --repo lincutto/MuhnPaceCloudstream --out published
```
