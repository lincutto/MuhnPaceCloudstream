#!/usr/bin/env python3
"""Fetch pinned artwork, decode it and record evidence. Optional dependency: Pillow.

Usage: python scripts/verify_artwork.py <40-character artwork commit> <output.json>
This is a maintenance check, never called during catalogue browsing or offline CI.
"""

import concurrent.futures
from datetime import datetime, timezone
import hashlib
import io
import json
from pathlib import Path
import re
import sys
import urllib.request

from PIL import Image


def main():
    if len(sys.argv) != 3 or not re.fullmatch(r"[0-9a-f]{40}", sys.argv[1]):
        raise SystemExit(__doc__)
    commit, output = sys.argv[1:]
    root = Path(__file__).resolve().parents[1]
    collections = json.loads((root / "assets/artwork/catalogue.json").read_text())["collections"]
    base = f"https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/{commit}/assets/artwork"

    def verify(job):
        collection, kind = job
        expected = collection[kind]
        url = f'{base}/{expected["file"]}'
        with urllib.request.urlopen(url, timeout=30) as response:
            data = response.read()
            status = response.status
            mime = response.headers.get_content_type()
            cache_control = response.headers.get("Cache-Control")
        digest = hashlib.sha256(data).hexdigest()
        if status != 200 or mime != "image/png" or digest != expected["sha256"]:
            raise ValueError(f"Artwork response mismatch: {url}")
        with Image.open(io.BytesIO(data)) as image:
            image.load()
            width, height = image.size
            if (width, height) != (expected["width"], expected["height"]):
                raise ValueError(f"Artwork dimensions mismatch: {url}")
        return {
            "collection_id": collection["collection_id"], "kind": kind, "url": url,
            "status": status, "content_type": mime, "width": width, "height": height,
            "bytes": len(data), "sha256": digest, "cache_control": cache_control,
        }

    jobs = [(collection, kind) for collection in collections for kind in ("poster", "background")]
    with concurrent.futures.ThreadPoolExecutor(max_workers=4) as pool:
        results = list(pool.map(verify, jobs))
    proof = {
        "verified_at": datetime.now(timezone.utc).isoformat(), "artwork_commit": commit,
        "source": "Original geometric artwork; see assets/artwork/README.md", "images": results,
    }
    Path(output).write_text(json.dumps(proof, indent=2) + "\n")
    print(f"Verified {len(results)} HTTPS images: HTTP 200, image/png, decoded dimensions and SHA-256 match.")


if __name__ == "__main__":
    main()
