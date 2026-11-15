# Phase 4A A1–A3: single-series preview and Dressrosa sample

Inspected 2026-11-15. Supersedes the earlier sixteen-card presentation and Phase 3's prohibition on *all* One Pace-derived metadata. The user authorized one series, sixteen collection seasons and evidence-backed One Pace correspondence. Stop after publishing this preview; no B/C expansion.

## Recovery and architecture

Recovered `10fbf54fdbf666c1d211aa5cc379f20396b15097` plus unfinished Kotlin changes, eight researched records, nineteen original-anime title references and ten extracted image assets. Checkpoint `fbac35fa64a652f7be13e44485f9ba42b4e5a3fa` preserves the research and immutable artwork. No discarded/recreated prior phases. The user reports that preview v1 played and navigated successfully on Fire TV; this is user feedback, not an assistant device test.

Home and search now return **one** `One Piece (Muhn Pace)` series. Arc-name searches find that series. `SeriesCatalogue.kt` fixes the sixteen collection/season positions and navigation origins. All 274 selected file IDs, file URLs and chronological within-collection ordering are preserved. The catalogue is partial: it begins at the available Enies Lobby material, not original anime episode 1.

| Season | Collection | Available videos | Collection ID |
| --- | --- | ---: | --- |
| 1 | Enies Lobby | 12 | 6VyLVkB2 |
| 2 | Post Enies Lobby | 2 | jVhFqouN |
| 3 | Thriller Bark | 22 | NwJKTg21 |
| 4 | Sabaody Archipelago | 11 | KztkSCuY |
| 5 | Amazon Lily | 5 | TuZJgwRL |
| 6 | Impel Down | 10 | 4AN9Xxht |
| 7 | Marineford | 17 | oa8oZvZx |
| 8 | Post Marineford | 2 | MH7AQkgr |
| 9 | Fishman Island | 22 | ZQhstabS |
| 10 | Fishman Island • Alternate scene | 1 | kuoQYqfR |
| 11 | Punk Hazard | 22 | A1jFtPvQ |
| 12 | Dressrosa | 48 | f8HTXmAk |
| 13 | Zou | 10 | totP9Xmm |
| 14 | Whole Cake Island | 39 | AyvkmFFZ |
| 15 | Wano • Acts 1 & 2 | 26 | QZw3Ejpy |
| 16 | Wano • Act 3 | 25 | sF9stHJo |

Season 10 is an alternative scene from Fishman Island, not a new subsequent story arc. It remains adjacent to its parent collection so all sixteen source collections remain separately accessible. The other collection ordering follows the existing watch guide and One Pace arc sequence.

`Episode.season` and `SeasonData(season, name, displaySeason)` supply native selectors such as **Season 12 Dressrosa**. Host-generated `S12:E32` / `32.` labels are navigation indices, not an assertion about original anime episode 32. The provider supplies no duplicate numeric prefix. Original anime indices cannot safely replace navigation positions in a partially researched season: the host sorts numerically and deduplicates the same season/episode key, while fan edits can overlap original episodes. Stable navigation origins keep later keys unchanged if an earlier file temporarily disappears. Source insertions before those origins need explicit review.

The new preview series route is `/muhnpace-v3-preview/one-piece`. The old `/muhnpace-v3-preview/l/<collection>` routes remain loadable solely for saved history/bookmarks; they are absent from the primary catalogue. Their season 1/edit-number history inputs are unchanged. Old preview progress remains attached to those entries and is **not migrated** to the new series. No data store is edited or reset. The new series, legacy preview and production keys are checked for collisions for both Pixeldrain hosts and provider-loaded/unloaded hashing. Actual persisted Fire TV resume still needs user testing.

## Mapping and presentation

[Structured evidence](../metadata/dressrosa-30-37.json) is keyed by exact collection ID, file ID, filename, internal edit number and API-reported media SHA-256. Hashes were not recomputed by downloading complete videos. Optional mismatched/corrupt metadata falls back without removing playback. `ResearchDataset.kt` is generated offline; no additional title/mapping/artwork-service API requests occur during browsing. Loading the series fetches sixteen Pixeldrain lists, at most four concurrently through Cloudstream's existing `amap` API.

Evidence categories: `DIRECTLY_VERIFIED`, `ONE_PACE_DERIVED`, `CONFLICTING`, `UNKNOWN`. The strict exact-file Phase 3 verified registry remains empty. A partial scene confirmation does not become direct verification of a complete edit range.

| Internal source edit | File ID | Viewer-facing title | Coverage status |
| --- | --- | --- | --- |
| 30 | edkjkrVg | Secret Plan - original range unresolved | CONFLICTING |
| 31 | 8GdJ6uQg | The Operation Fruit - 699-700 | ONE_PACE_DERIVED |
| 32 | Q5RqeqED | White Town - 701-702 | ONE_PACE_DERIVED |
| 33 | h795H5E2 | The Island of Fate: Minion - 703-704 | ONE_PACE_DERIVED |
| 34 | xxL4CmKQ | Cora-san - original range unresolved | CONFLICTING |
| 35 | vSkNFYho | Bellamy the Pirate - original range unresolved | CONFLICTING |
| 36 | U642iqmh | The Spear of Elbaf and Don Sai - original range unresolved | CONFLICTING |
| 37 | TPncrp8t | Cabbage and Lomeo - original range unresolved | CONFLICTING |

Totals across 274: **0 directly verified full mappings, 3 One Pace-derived, 5 conflicting, 266 unknown**. Eight individually researched descriptions and eight corresponding interior stills. Outside the sample, `Title unavailable - original episodes unknown` is an explicit placeholder; no story title/range is invented or obtained by blindly matching edit numbers. Factual fallback descriptions omit internal edit numbers. Native host navigation indices remain unavoidable.

Descriptions start with one or two self-authored, story-specific sentences, followed by mapping qualifications and the original anime titles for accepted coverage. Example: “Law’s childhood in Flevance reveals the suffering caused by White Lead disease. His arrival among the Donquixote family brings him into conflict with Corazon.” The scene-confirmed 701 note and One Pace-derived 701–702 qualification follow. All nineteen reference titles remain in the structured dataset; unaccepted candidate coverage is not advertised as fact. Research used sampled scenes plus the corresponding One Pace story reference, not complete frame-by-frame viewing.

### Provenance and discrepancies

- [Official One Pace subtitle titles](https://github.com/one-pace/one-pace-public-subtitles/blob/76eb3421d97f25fa57a7b2db893b4cf36b417607/main/title.properties): exact Dressrosa title keys; English release subtitle story material inspected. Bracketed chapter numbers in filenames are **manga**, not anime coverage.
- [One Pace guide-derived metadata](https://github.com/ladyisatis/one-pace-metadata/blob/10f9ff4cae297d8015e44675f25355cb247855c6/metadata/episodes.json) and its README provenance trace ranges to the One Pace Episode Guide and descriptions to the One Pace team's spreadsheet. This is a maintained mirror, not itself proof of Muhn Pace's complete content.
- [Exact Muhn Pace file index](https://github.com/Denperidge/stremio-muhn-pace-addon/blob/b47f29574326ce5a05168ddfba5b210f579527ac/data/links.json) corroborates all eight IDs. Its subtitle matching algorithm alone is insufficient: exact-file interior scene observations and adjacent story progression also support the eight title correspondences.
- [Plex episode table](https://github.com/djpain/one-pace-muhn-pace-to-plex/blob/7b91a41b3b718e300d9320e5d2c9b45b87ba2321/episodes-reference.json), [chapter table](https://github.com/djpain/one-pace-muhn-pace-to-plex/blob/7b91a41b3b718e300d9320e5d2c9b45b87ba2321/chapters-reference.json) and `rename.py` were inspected. Both lookup routes disagree with the guide-derived table for five records. The README targets One Pace releases, not verified exact Muhn Pace files. The cause/version history of the discrepancies is not established; neither table is arbitrarily promoted to truth.

Conflicts (guide-derived vs Plex): 30 = `694, 696–698` vs `697–698`; 34 = `705–706` vs `705–707`; 35 = `707–711` vs `708–710`; 36 = `708–711` vs `711–713`; 37 = `711–713` vs `714–716`. Both candidates remain recorded. The three agreed ranges are admitted as **derived**, never directly verified. No offsets or broad arc boundary inference were used. The user's “To Russian, with Love - 714-716” example is not used as evidence or assigned to source 37.

**Dressrosa 32:** exact file `Q5RqeqED`, collection `f8HTXmAk`, has young Law/White Town/Corazon scenes at the recorded timestamps. This supports the user's observation of episode 701 content; indexed original episode references corroborate the White Town identification. One Pace's title is **White Town**, with 701–702 in both range tables. Under the revised policy this is `ONE_PACE_DERIVED`, with partial scene confirmation of 701 retained separately. Complete Muhn Pace cut boundaries and precise original-episode transition timestamps remain unverified. This task does not reopen episode 684 research.

## Artwork and remaining gaps

The old geometric artwork was deliberately generated as a rights-conservative fallback, and generic descriptions came from `withPresentation()` formatting known identity facts. Pixeldrain's thumbnails for the eight sample files show the opening landscape: all 28 pair combinations have dHash distance <=5. Its thumbnail API has no supported timestamp selector.

The replacement eight 640×360 JPEGs come from the exact linked files at interior scene timestamps. All are distinct in the recorded image audit (zero pairs <=5), with visual inspection for relevance, black frames, credits and title cards. The recovered Law portrait is 600×900 and the Luffy/Law background is 1280×720. They now identify the single series as a **temporary Dressrosa-focused presentation**, and the legacy Dressrosa route. They are scene crops, not licensed promotional posters. The previous geometric files remain available as metadata fallbacks; a configured fallback does not provide automatic HTTP-failure switching inside Cloudstream's image loader.

See [image provenance and usage limitations](../assets/phase4a/README.md) and [hash/dimension/timestamp manifest](../assets/phase4a/manifest.json). Original animation copyright remains with its owners. This is a limited-excerpt identification rationale, **not a permission-cleared art licence**. No TMDB credentials or external image hotlinks were added. TMDB and One Pace site requests still returned proxy 403 when rechecked, despite the user's willingness to change the settings. No restriction was bypassed. Live anonymous verification of all ten pinned GitHub image URLs passed HTTP status, JPEG decoding, dimensions and byte hashes.

The remaining 266 source thumbnails were not given a new relevance/duplication audit in this milestone. Existing exact-file thumbnail/fallback handling remains; eight previously known black thumbnails still use arc art. Do not claim all 274 thumbnails are now unique. Full original research scene samples also remain in `/tmp/muhn4-research/frames` in the recovered environment; committed provenance records the sampling timestamps for reproducibility.

## Validation and host limits

Local **45 Kotlin/JUnit tests** (41 main, 4 preview) and **20 Python checks** passed. Both Android modules compiled, DEX and `.cs3` packaging passed. The first compile exposed that direct coroutine imports were unavailable in the provider compile classpath; replaced with the already-supported Cloudstream `amap`, without adding a dependency. Existing regression coverage was retained and updated for the user's changed display requirements. New tests cover native seasons/host ordering/dedup inputs, legacy/new/production identity separation, missing-source stable positions, evidence admission, synthetic disjoint ranges, bad metadata and exact thumbnail associations. Tests validate implementation against the recorded evidence; they do not certify story truth.

[Live checks](research/phase4a-live-validation.json): all sixteen API lists, all 274 baseline IDs/filenames, the eight API-reported media hashes and ten immutable image URLs passed. No full media or Fire TV playback test occurred. Development CI and publication results are recorded in [handover](V3_HANDOVER.md).

[Cloudstream API findings](CLOUDSTREAM_METADATA.md#phase-4a-native-seasons-and-skip-segments) explain named seasons, unavoidable host indices, series-level paused overview, Continue Watching fields and the lack of a supported provider timestamp API. The paused information feature remains enabled by the same host setting. The unified series cannot make its paused plot automatically change to the active season's synopsis. All arc overviews remain in the series plot; five-line TV truncation may hide later arcs. Old arc history routes retain their specific overviews. Actual Fire TV rendering, remote focus, truncation and persisted progress need the user's new preview test.
