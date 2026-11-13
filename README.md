# Muhn Pace Direct (English Dub) — Cloudstream beta extension

This is an **independent source-code project** for Cloudstream Beta on Android / Fire TV.

## What it does

- Lists **16 public Pixeldrain collections** linked by the community **One Pace Dub Watch Guide**.
- Each collection appears as a separate selectable TV series / arc. The current list includes Dressrosa, Punk Hazard, Zou, Whole Cake Island, Wano, and earlier arcs.
- Reads the public Pixeldrain list API *when you open an arc*, populating video files with their real names **in Pixeldrain list order**.
- Select an episode in Cloudstream and the plugin provides its `https://pixeldrain.com/api/file/{id}` URL.
- Does not use P2P, torrents, a VPN, Real-Debrid, accounts or a proxy.

**IMPORTANT:** This is a *beta / untested-on-device* project. It has not been compiled into `.cs3` in the chat environment. **Pixeldrain may block direct hotlinked playback from Cloudstream (HTTP 403), impose bandwidth limits, or require captcha.** Working playback on the Pixeldrain website **does not guarantee** playback in Cloudstream. There is no mechanism here for bypassing restrictions. File names/links are source metadata; English audio tracks cannot be confirmed remotely.

The 16 list URLs come from: https://steamcommunity.com/sharedfiles/filedetails/?id=3685024934
API docs: https://pixeldrain.dev/api
Cloudstream extension template: https://github.com/recloudstream/extensions

### Watching order warnings

- The special **Fishman Island · Episode 15** collection should be inserted after 14, before 16, not watched after all of Fishman Island.
- Wano is separated into **Acts 1 & 2** and **Act 3**, and the dub-guide recommends mixing One Pace and Muhn Pace episodes. Use the guide's watch order rather than assuming a contiguous episode numbering scheme.
- This extension does not map Muhn Pace edit numbers to original One Piece episode numbers (e.g. 677). Don't guess the matching episode.

## Building and installing without an Android development machine

The included `.github/workflows/build.yml` is configured to **build a real `.cs3` on GitHub Actions** using the current official Cloudstream extension build template, then publish the compiled plugin and its `repo.json` on the repository's `builds` branch.

1. Create a **new public GitHub repository** (e.g. `MuhnPaceCloudstream`).
2. Upload **all the contents** of this source ZIP into the repository root, preserving `.github/workflows/build.yml`, `MuhnPace/`, `scripts/`, and the other paths. GitHub Actions must be enabled and allowed to write repository contents (Settings → Actions → General → Workflow permissions → Read and write permissions, if your repository defaults to read-only).
3. Commit to `main` (or go to Actions → *Build and publish Muhn Pace Cloudstream extension* → Run workflow).
4. Check the workflow run. **Only continue if the build and publish jobs succeed.** It publishes a Cloudstream repository at:

   `https://raw.githubusercontent.com/YOUR_USERNAME/YOUR_REPOSITORY/builds/repo.json`

5. On Fire TV, in Cloudstream Beta open Settings → Extensions → Add Repository, and paste that actual URL using your username/repository name.
6. Install **MuhnPace**, select the provider in Cloudstream, open Dressrosa, and select an episode.
7. If playback fails (especially 403), Pixeldrain is likely blocking direct app hotlinking. Do not assume the solution is to change VPN, DNS, or repeat the same steps. Direct HTTPS integration cannot be guaranteed for this host.

For local Android toolchains: use the official Cloudstream extensions repository as build base, copy the `MuhnPace/` directory into its root and run `./gradlew :MuhnPace:make` with JDK 17 and an Android SDK. The GitHub workflow does this automatically.

## Validation completed in this chat

- Source review against current Cloudstream `MainAPI` and `newExtractorLink` APIs.
- Static checks: `python3 -m unittest discover -s tests -v`
- Local Kotlin syntax compile against lightweight Cloudstream/Android API **stubs** (not the real Cloudstream dependency and not a `.cs3` build).

**Not validated here**: Android Gradle build, actual Pixeldrain API responses, English-audio track selection, network playback through Cloudstream, Fire TV 4K 2nd Gen compatibility. A successful GitHub Actions build only confirms compilation and packaging, not video host playback.

No media files are included or distributed. Only access material you have permission to stream.
