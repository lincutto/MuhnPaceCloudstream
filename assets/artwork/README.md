# Original catalogue artwork

These are **original geometric illustrations created for this repository**, serving as deliberate arc artwork fallbacks. They are not official One Piece promotional art, screenshots, traced images, or assets downloaded from an artwork database. No third-party images, logos or font files are included. The thematic landmarks describe each arc's premise; no individual edit-to-anime mapping is implied.

Each collection has a labelled 768×1152 poster and a text-free 1920×1080 background with a quiet left side for Cloudstream's own text. The Fishman Island edit-15 collection shares its arc's motif with a distinct palette and label. The two Wano collections have separate landmarks and labels. Selection avoids character outcomes and late-story character transformations.

`scripts/generate_artwork.py` contains the original vector drawing code and the collection-ID-to-artwork mapping. Checked-in SVGs are the editable sources; checked-in PNGs are the runtime-compatible exports. Regeneration is optional and needs CairoSVG 2.8.2 and the system DejaVu Sans font (rendered text only; no font file is redistributed):

```sh
python -m pip install CairoSVG==2.8.2
python scripts/generate_artwork.py
```

The provider uses HTTPS PNG URLs pinned to the artwork commit on `raw.githubusercontent.com`. URL responses, decoded dimensions and hashes are verified separately before configuration. Cloudstream owns image loading and caching; the provider does not prefetch artwork. No production branch or release asset is used for hosting.

Source-image access was restricted during implementation. These illustrations are intentional fallbacks, not a claim that licensed anime posters were obtained or that a Fire TV rendering test occurred. They can be replaced with appropriately permitted, verified source artwork later without altering collection or episode identities.

## Preview

These are the exported assets, not screenshots of Cloudstream. Backgrounds use the same motif without text; for example, [Dressrosa's 1920×1080 background](dressrosa-background.png).

| Enies Lobby | Post-Enies Lobby | Thriller Bark | Sabaody |
| --- | --- | --- | --- |
| <img src="enies-lobby-poster.png" width="180" alt="Enies Lobby poster"> | <img src="post-enies-lobby-poster.png" width="180" alt="Post-Enies Lobby poster"> | <img src="thriller-bark-poster.png" width="180" alt="Thriller Bark poster"> | <img src="sabaody-poster.png" width="180" alt="Sabaody poster"> |

| Amazon Lily | Impel Down | Marineford | Post-Marineford |
| --- | --- | --- | --- |
| <img src="amazon-lily-poster.png" width="180" alt="Amazon Lily poster"> | <img src="impel-down-poster.png" width="180" alt="Impel Down poster"> | <img src="marineford-poster.png" width="180" alt="Marineford poster"> | <img src="post-marineford-poster.png" width="180" alt="Post-Marineford poster"> |

| Fishman Island | Fishman Island special | Punk Hazard | Dressrosa |
| --- | --- | --- | --- |
| <img src="fishman-island-poster.png" width="180" alt="Fishman Island poster"> | <img src="fishman-special-poster.png" width="180" alt="Fishman Island special poster"> | <img src="punk-hazard-poster.png" width="180" alt="Punk Hazard poster"> | <img src="dressrosa-poster.png" width="180" alt="Dressrosa poster"> |

| Zou | Whole Cake Island | Wano Acts 1 & 2 | Wano Act 3 |
| --- | --- | --- | --- |
| <img src="zou-poster.png" width="180" alt="Zou poster"> | <img src="whole-cake-poster.png" width="180" alt="Whole Cake Island poster"> | <img src="wano-acts-1-2-poster.png" width="180" alt="Wano Acts 1 and 2 poster"> | <img src="wano-act-3-poster.png" width="180" alt="Wano Act 3 poster"> |
