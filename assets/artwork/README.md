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
