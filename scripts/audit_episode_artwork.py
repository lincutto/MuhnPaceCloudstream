#!/usr/bin/env python3
"""Offline image integrity/duplicate check. Human review must still establish scene relevance."""
import argparse
import hashlib
import itertools
import json
from pathlib import Path
from PIL import Image, ImageStat

ROOT = Path(__file__).resolve().parents[1]


def inspect(path):
    with Image.open(path) as image:
        image.load()
        width, height = image.size
        gray = image.convert("L")
        small = gray.resize((9, 8), Image.Resampling.LANCZOS)
        pixels = list(small.getdata())
        bits = [pixels[y*9+x] > pixels[y*9+x+1] for y in range(8) for x in range(8)]
        dhash = sum(int(bit) << index for index, bit in enumerate(bits))
        stats = ImageStat.Stat(gray)
        return dict(width=width, height=height, format=image.format,
                    sha256=hashlib.sha256(path.read_bytes()).hexdigest(),
                    dhash=f"{dhash:016x}", mean_luma=round(stats.mean[0], 3),
                    stddev_luma=round(stats.stddev[0], 3))


def near_duplicates(records, threshold=5):
    return [dict(first=a["file"], second=b["file"], distance=distance)
            for a,b in itertools.combinations(records, 2)
            if (distance := (int(a["dhash"],16) ^ int(b["dhash"],16)).bit_count()) <= threshold]


def main():
    parser=argparse.ArgumentParser()
    parser.add_argument("--manifest", type=Path, default=ROOT/"assets/phase4a/manifest.json")
    args=parser.parse_args()
    data=json.loads(args.manifest.read_text())
    thumbs=[]
    for entry in data["images"]:
        result=inspect(ROOT/entry["file"])
        for key in ["width","height","format","sha256","dhash"]:
            assert result[key] == entry[key], (entry["file"],key)
        assert result["mean_luma"] > 10 and result["stddev_luma"] > 10, entry["file"]
        if entry["role"] == "episode_thumbnail": thumbs.append(entry)
    assert not near_duplicates(thumbs), "Episode images need a new human-reviewed selection"
    print(f"Verified {len(data['images'])} images; {len(thumbs)} distinct episode stills, no near-duplicate pairs at dHash <= 5.")


if __name__ == "__main__":
    main()
