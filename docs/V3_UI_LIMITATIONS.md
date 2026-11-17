# Preview v4: episode count and provider label

Focused inspection on 2026-11-17, after publishing and publicly verifying artwork v4. Checked the previously resolved Cloudstream library/client source `5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c` and current upstream HEAD `2dcc25c3945c61dfa95d1710ced7c74acfe84baa`. The relevant count rendering, TV bindings, provider-name property/registry and TV response fields are identical. This is source inspection, not a test of the user's installed Fire TV APK.

## Episode count — unsupported provider override

- [`ResultViewModel2.postEpisodeRange`, lines 2023–2034](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L2023) formats the count from `currentEpisodes[indexer]?.size`. TV-series episodes are grouped by `EpisodeIndexer(DubStatus.None, season)` at lines 2285–2290. Dressrosa therefore displays **48 Episodes**, although the complete response contains **274 episodes in 16 seasons**.
- [`ResultFragmentTv`, lines 763–765](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultFragmentTv.kt#L763) binds that client-generated text directly.
- [`TvSeriesLoadResponse`, lines 2673–2730](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/library/src/commonMain/kotlin/com/lagradost/cloudstream3/MainAPI.kt#L2673), `EpisodeResponse` and `LoadResponse` expose no count-label override. `getTotalEpisodeIndex` calculates an individual episode's position across seasons; this label does not read it.

No supported way to replace this label with the dynamic total while retaining the existing seasons and episodes was found. No restructuring, duplication, index changes, plot workaround or Cloudstream modification was made.

## Provider label — unsupported display-only override

- [`MainAPI.name`, line 515](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/library/src/commonMain/kotlin/com/lagradost/cloudstream3/MainAPI.kt#L515) is the visible provider name; there is no separate provider `displayName`. The same file's registry indexes providers by `api.name` and resolves saved `apiName` through it (lines 148–162).
- `newTvSeriesLoadResponse` assigns `apiName = this.name` (line 2737). [`ResultViewModel2`, line 331](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultViewModel2.kt#L331) passes that name to the page; [`ResultFragmentTv`, line 870](https://github.com/recloudstream/cloudstream/blob/2dcc25c3945c61dfa95d1710ced7c74acfe84baa/app/src/main/java/com/lagradost/cloudstream3/ui/result/ResultFragmentTv.kt#L870) displays it. The client also carries `loadResponse.apiName` into playable episode records and uses `apiName` to resolve providers when loading saved entries.

Changing the provider name or response `apiName` to **One Piece (Muhn Pace)** would change a routing/identity input, not merely a display label. Renaming the extension-list manifest would not override this provider label. Kept **Muhn Pace v3 Preview**, its package/provider classes, URLs and history inputs unchanged. The series title remains **One Piece (Muhn Pace)**.

## Publication decision

Neither requested refinement has a safe supported implementation. Retain **preview v4**, known-good commit `3317cfccfbf5d81f43ae6c0182f4b75ade96474d`, source `8f3e2f8de65a4b6c2385b0487113ecb7954af859`. No runtime changes, additional tests, rebuild or v5 publication. [Public v4 verification](research/preview-v4-publication.json). Production `main` and `builds` remain untouched.
