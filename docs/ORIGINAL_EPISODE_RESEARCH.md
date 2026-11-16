# Original-episode mapping research

Current Phase 4B findings: [complete metadata record](PHASE4B_METADATA.md). **249 One Pace-derived ranges** are admitted through published release correspondence, **25 late-Wano ranges remain UNKNOWN**, and **zero full Muhn Pace ranges are directly verified**. The five earlier Dressrosa conflicts were resolved with newly inspected Muhn-specific source records; both earlier alternatives remain archived. Post-Enies Lobby's numbering mismatch is explicitly handled. Dressrosa file `yUkgcapt` now has derived coverage **683–686**, including 684, but its exact transition timestamp and complete Muhn boundaries are not directly verified. Do not describe it as a fully audited resume point. The sections below are **historical Phase 3 research**, not the current display policy.

## Historical Phase 3 investigation

Checked 2026-11-15 (UTC). **Accepted mappings: 0.** All 274 selected edits remain explicitly unknown. No verified edit-specific story title or synopsis was added.

## Sources investigated

| Source | Result and evidence decision |
| --- | --- |
| All 16 live `https://pixeldrain.dev/api/list/<collectionId>` responses | HTTP 200/success; 290 source entries, all descriptions empty, no explicit original-episode field. All source IDs and filenames match the preserved baseline, including its 274 selections. See the [audit record](research/phase3-mapping-audit.json). |
| [Dressrosa file yUkgcapt info](https://pixeldrain.dev/api/file/yUkgcapt/info) | Identifies `[Muhn Pace] Dressrosa 25.mp4` and file hash, but no original anime coverage or story title. This verifies file identity only. |
| [Community dub guide](https://steamcommunity.com/sharedfiles/filedetails/?id=3685024934) | Identifies Muhn Pace as a separate project based on One Pace, supplies the collection links and mixed-project watch order. It does not map Dressrosa edits to original anime episodes or prove identical coverage across projects. |
| Guide-linked [all-arcs Drive folder](https://drive.google.com/drive/folders/1OdtYJmoxqazacnANOSnp67g28THyaEuW) and [Fun Pace watch-order sheet](https://docs.google.com/spreadsheets/d/1LYxp1i5FldNiKdL8BKaQ3T8EeueCSpgDWvN51kCBcKI) | Web tool could not access; terminal requests denied by the network proxy (CONNECT 403). Contents were not inspected. The sheet is linked as a filler/watch-order reference, not established Muhn Pace mapping evidence. |
| [djpain episode table](https://github.com/djpain/one-pace-muhn-pace-to-plex/blob/7b91a41b3b718e300d9320e5d2c9b45b87ba2321/episodes-reference.json) and [rename code](https://github.com/djpain/one-pace-muhn-pace-to-plex/blob/7b91a41b3b718e300d9320e5d2c9b45b87ba2321/rename.py) | Candidate Dressrosa 25 range **684–686**. Despite the repository name mentioning Muhn Pace, its README/code target `[One Pace]` filenames and its table does not bind the range to our exact Muhn Pace file/edition. Rejected for catalogue use. |
| [Official One Pace watch catalogue](https://onepace.net/en/watch) | Its **One Pace** Dressrosa 25 lists anime **683–686**, manga 748–749 and a story title. This is a different edit project and differs from the candidate table's starting episode. No exact-file equivalence to Muhn Pace is established. Neither its range nor title was copied into this catalogue. |
| [Denperidge Muhn Pace addon](https://github.com/Denperidge/stremio-muhn-pace-addon/blob/b47f29574326ce5a05168ddfba5b210f579527ac/README.md), [links](https://github.com/Denperidge/stremio-muhn-pace-addon/blob/b47f29574326ce5a05168ddfba5b210f579527ac/data/links.json), [metadata generator](https://github.com/Denperidge/stremio-muhn-pace-addon/blob/b47f29574326ce5a05168ddfba5b210f579527ac/data/subs.json.js) | Has Pixeldrain file links, but titles are generated from **One Pace** subtitles, matched by arc/edit numbering with overrides. It supplies no independently verified original-anime coverage for our exact file. Title reuse was not accepted as proof. |
| [superfly310/muhn-pace-subtitles README](https://github.com/superfly310/muhn-pace-subtitles/blob/a9a06f7970939cb294909f1a4babad8488c10eea/README.md) | Describes a mirror of One Pace subtitles; its name alone is not evidence for Muhn Pace edit coverage. No subtitle story content was imported. |
| [Whole Cake Island source note](https://pixeldrain.dev/api/file/b2zsFV4H) | Names content in edits 15/16 but gives no original-anime numbers. Not mapping evidence; no inferred story synopsis was added. |

Web queries included Muhn Pace + original episodes, mapping, spreadsheet, Dressrosa 25 and 684; GitHub repository/file searches and source inspection supplemented those queries. Accessible results did not expose a maintainer-authored exact-file conversion table. This is a bounded research result, not a claim that no such table exists anywhere. No accounts, Discord messages, premium services, torrents or full media downloads were used.

The special Fishman filename's `[632-633]` remains unlabelled source numbering, not accepted anime coverage. No offsets, proportional duration calculations, arc-boundary interpolation, or cross-project numbering assumptions are permitted.

## Episode 684 outcome

The target source being investigated is collection **`f8HTXmAk` (Dressrosa)**. The previously photographed edit 25 is file **`yUkgcapt`**, filename **`[Muhn Pace] Dressrosa 25.mp4`**, whose Pixeldrain-reported content SHA-256 is **`3dd98cd7b06ae4d53fe3d99b3613c4c071690e796fef28220bc68123bcf45f93`**. This hash was read from the info API, not recomputed by downloading the video.

There is **no accepted evidence** that this file, or any other exact Muhn Pace edit, contains original episode 684. The two candidate ranges above cannot resolve that gap. Additional coverage, an edit-specific title, a timestamp within the edit, and the best Muhn Pace starting point therefore remain unknown. **Do not recommend edit 25 or another edit as a verified transition point.**

The indexed [official original-anime episode 684 page](https://one-piece.com/anime/o2761/index.html) identifies its Japanese title as `大集結！ルフィと凶悪戦士軍団` and broadcast date as 2015-03-15. Direct page opening returned 403, so this is an indexed-source observation only. It identifies the original episode, not a Muhn Pace file. Its synopsis/title were not assigned to an unverified edit.

A safe completion of the transition task needs new evidence from the editor or an independently reproducible comparison identifying the exact file, actual included original episodes (including partial/noncontiguous coverage), and where episode 684 begins. No recommendation to skip, restart or choose an edit is made without that evidence.

## Admission rule for future evidence

Every accepted entry must record the exact collection ID, file ID, edit number, source filename/edition, original anime episode/range, source URL, UTC verification date and qualification. Use a content hash when evidence identifies a particular encoding. A human must check the source's reliability and whether it concerns Muhn Pace rather than One Pace. A URL or well-formed date alone does not establish truth.

`VerifiedEditMapping`/`EpisodeMappingRegistry` now require that record and match collection, file, edit number and filename before exposing a range. Missing, changed or conflicting entries return unknown. The production registry is deliberately empty; synthetic tests do not populate it. Remote filenames, descriptions or ad-hoc `original_episodes` fields are not trusted automatically.

Do not flatten disjoint coverage into a continuous range or describe partial coverage as complete. The current formatter handles a single verified interval; if future evidence requires multiple intervals, extend its representation and tests before admitting the entry. Broad arc coverage remains separate from exact-edit evidence. Source/date/qualification belongs in this research record with each accepted entry; it must not silently disappear into a guessed UI label.
