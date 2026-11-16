# Phase 4B: catalogue presentation

## Recovery and baseline

Recovered clean development `5a37acf7ce0ee6b9837fc40e1aa761495fbe6385` and published preview v2 `b3829bcc1eaecda75808e65fda7b7c360439e0b3`. The user reports that v2's unified series, sixteen named seasons and playback work on Fire TV. This is user feedback, not an assistant device test. The eight prior research records, images, scene evidence and original conflict assessments remain in their Phase 4A paths.

## Checkpoint 1: Dressrosa and series presentation

All 48 Dressrosa records now have published One Pace titles, derived original coverage, individual short story synopses and distinct 640×360 stills. Records are keyed by collection, exact file ID, source filename, internal position and media-reported SHA-256. Runtime browsing uses bundled metadata; there are no new metadata-service requests. Images are pinned to immutable development commits.

The newly inspected `other_edits.muhn_pace` table in [ladyisatis/one-pace-metadata](https://github.com/ladyisatis/one-pace-metadata/tree/10f9ff4cae297d8015e44675f25355cb247855c6) explicitly identifies Muhn Pace releases, titles and anime coverage. Together with exact-file indexing, release order and official subtitle titles, this supports **ONE_PACE_DERIVED** correspondence. Its CRC/blake2 values are not claimed to match the rehosted Pixeldrain bytes. Missing such a match alone is not a conflict or grounds to invent a different range.

For the five previously conflicting Dressrosa samples, the newly inspected project-specific records support the One Pace guide rather than the generic Plex conversion lookup. Both prior candidates are retained in `metadata/dressrosa-30-37.json`; the new decision and its qualification are in each Phase 4B record. No complete exact-file range was promoted to DIRECTLY_VERIFIED. Dressrosa 32 remains `White Town - 701-702`, derived, with the separately preserved partial scene evidence for 701. Disjoint ranges retain every stated number: `Secret Plan - 694, 696-698` and `The Curse of Heart - 714-715, 720-723` do not fill gaps.

Descriptions are newly written concise summaries of the referenced story facts; the eight useful existing summaries are preserved. No bulk verbatim description dataset is shipped. One Pace subtitle metadata permits reuse with credit in its README; credit to **One Pace** is retained here. The third-party description dataset has no independently established blanket text licence. Source facts and exact references are internal; confidence, unknown-coverage explanations and development terminology do not appear in episode descriptions.

The main plot is now two sentences introducing Luffy, the crew, and the streamlined English-dubbed fan edit. The new 768×1152 crew poster and different 1920×1080 crew background come through the pinned addon's TMDB attribution chain. Full image provenance and unresolved rights limitations are in [the artwork record](../assets/phase4b/README.md). No rightsholder licence is claimed. Dressrosa images reuse the eight reviewed stills, seven newly extracted interior frames and 33 community captures whose unique source ID/filename association was checked. Fifty total images pass decoding, integrity, size/luminance and dHash checks; the 48 episode stills have no near-duplicate pairs at distance ≤5. This is not a guarantee of TV composition.

## Player and compatibility

Reinspected actual Cloudstream source `5d66bbeaf3b35c0c18ad2e0b9ce6b0aea4f5241c`: `ResultViewModel2` carries `Episode.name` into `ResultEpisode.name`; `GeneratorPlayer.getPlayerVideoTitle()` reads that name and adds the series name plus its own localized S/E prefix. For example the control title can be `One Piece (Muhn Pace) "S12:E38" - To Russian, with Love - 714-716`. The extension cannot remove the prefix without changing host code or identity inputs. No such change is made. An already open result/player can retain old metadata; leave it and reopen the series after updating. No history deletion or data reset is required.

The separate delayed paused/idle information panel still uses the load-response plot, now the concise series introduction. It cannot dynamically show the current episode synopsis through this provider API. Legacy arc routes keep their arc synopsis. Continue Watching keeps the same series URL, provider and saved season/position inputs; its model has no episode-name or range field. A cached card may keep old art until the host refreshes it. The sixteen season origins, all 274 file URLs, both module identities and playback callback are preserved. Full prior API findings: [Cloudstream metadata](CLOUDSTREAM_METADATA.md).

## Remaining work after checkpoint 1

Populate the remaining seasons in batches, validate the complete dataset and publish only after final build/CI success. The inspected public One Pace titles stop before Muhn Pace Wano 71–95; those 25 files do not acquire offset-derived ranges. A user source request is pending, and public reports confirm that the shared dataset lacks these records. No Phase 5 or unrelated feature work is authorized.
