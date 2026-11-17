# Preview identification artwork

These files are small identification images for the noncommercial, isolated Muhn Pace preview. One Piece characters and animation remain © Eiichiro Oda/Shueisha and Toei Animation. No rightsholder-granted licence, public-domain status or endorsement is claimed. Public availability is not evidence of unrestricted reuse; do not redistribute these as an artwork pack or treat the software licence as an image licence.

The poster uses the replacement **IMG_9122.jpeg** supplied on 2026-11-17; the previous padded poster is preserved in preview v4. The received image is **1280×718 landscape**, despite being described as extended. Its exact source is retained at `sources/IMG_9122.jpeg`; source/export hashes and crop coordinates are in `manifest.json`. The supplied Fire TV screenshots were used only as composition references, not imported into the artwork.

The new **768×1152 (2:3)** poster fills the card with an ordinary crop and proportional resize. Crop `[399, 0, 877, 717]` retains 478×717 source pixels, centred on Luffy with Zoro and Sanji visible. LANCZOS scales it to 768×1152; JPEG quality 92, 4:4:4. There is no padding, stretching, generated content or alteration of character appearances. Upscaling adds no source detail. The necessary trade-off is retaining only 37.3% of the landscape width: most outer crew members are excluded, with partial figures at the edges. The reference card includes additional sky and lower artwork absent from this attachment; cropping/resizing cannot reproduce that full-crew composition. Keeping the whole source would reintroduce large padding; a montage would rearrange/repeat the composition. Neither alternative was used. New Fire TV rendering remains a device check.

Reproduce with the existing Pillow dependency:

```python
from PIL import Image
with Image.open("assets/phase4b/sources/IMG_9122.jpeg") as image:
    image.convert("RGB").crop((399, 0, 877, 717)).resize(
        (768, 1152), Image.Resampling.LANCZOS
    ).save("assets/phase4b/series/poster.jpg", quality=92, subsampling=0)
```

The separate **1280×721 background** remains the byte-for-byte copy of Picture 2 (`IMG_9123.jpeg`) from 2026-11-16. Its URL and bytes remain pinned to the v4 artwork commit. All episode thumbnails are unchanged. These supplied assets retain the recorded provenance; no new artwork search or licensing investigation was performed.

Episode thumbnails are 640×360 JPEGs, quality 82, selected from the corresponding video. Sources are either locally extracted exact Pixeldrain files or the pinned addon's existing 180-second captures. Exact ID/filename associations and any edition ambiguity are checked before use. The eight previously reviewed Dressrosa stills are preserved. Each record retains its source, capture time, transformation, hash and review. No opening landscape, black frame, logo or title/credit card is knowingly used.

`scripts/audit_episode_artwork.py --manifest assets/phase4b/manifest.json` checks dimensions, hashes, luminance and near-duplicate dHashes. Image comparison cannot prove story correspondence or licensing; the provenance records and visual review are separate evidence. The completed baseline scope is 274 episode images plus two series images. The original Phase 4A files remain intact for recovery and legacy artwork.
