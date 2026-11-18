"""Embed the reviewed release rows in the dev DEX. Browsing needs no metadata service."""
import csv
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / "docs/research/egghead-fan-edits.csv"
COLLECTION_TYPES = {"nKaeVsMp": "ONE_PACE", "ef1wn8dY": "COMMUNITY_EDIT"}


def kotlin_string(text):
    return json.dumps(text, ensure_ascii=False).replace("$", "\\$")


def anime_episodes(label):
    numbers = []
    for part in label.split(", "):
        match = re.fullmatch(r"([1-9][0-9]*)(?:–([1-9][0-9]*))?", part)
        if not match:
            raise ValueError(f"Invalid anime coverage: {label}")
        first = int(match[1])
        last = int(match[2] or first)
        if last < first:
            raise ValueError(f"Reversed anime range: {label}")
        numbers.extend(range(first, last + 1))
    if not numbers or numbers != sorted(set(numbers)):
        raise ValueError(f"Invalid anime coverage: {label}")
    return ", ".join(map(str, numbers))


def generate(target):
    entries = []
    slots, files = set(), set()
    with REVIEW.open(encoding="utf-8", newline="") as source:
        for row in csv.DictReader(source):
            if row["native_review"] != "verified":
                continue
            slot = int(row["season_17_position"])
            file_id = row["pixeldrain_file_id"]
            digest = row["published_sha256"]
            synopsis = row["synopsis"].strip()
            height = int(row["video_height"])
            if (height <= 0 or slot <= 0 or slot in slots or file_id in files or not re.fullmatch(r"[A-Za-z0-9_-]{1,64}", file_id)
                    or not re.fullmatch(r"[a-f0-9]{64}", digest) or not synopsis):
                raise ValueError(f"Incomplete or duplicate reviewed release at Egghead {slot}")
            slots.add(slot)
            files.add(file_id)
            kind = COLLECTION_TYPES[row["pixeldrain_list_id"]]
            numbers = anime_episodes(row["editor_anime_episodes"])
            entries.append(f'    "17/{slot}" to EggheadRelease(\n'
                           f'        ApprovedFile(SourceType.{kind}, "{file_id}", "{digest}",\n'
                           f'            setOf("en"), listOf({numbers}), {height}),\n'
                           f'        {kotlin_string(synopsis)},\n    ),\n')
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text("package com.muhnpaceeggheaddev\n\n"
                      "internal val reviewedEggheadReleases = mapOf<String, EggheadRelease>(\n"
                      + "".join(entries) + ")\n", encoding="utf-8")


if __name__ == "__main__":
    generate(Path(sys.argv[1]))
