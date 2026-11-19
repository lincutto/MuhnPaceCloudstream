"""Embed the reviewed release rows in the extension DEX. Browsing needs no metadata service."""
import csv
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / "docs/research/egghead-fan-edits.csv"
ANIME_TITLES = ROOT / "metadata/egghead-original-anime-titles.csv"
WANO_REVIEW = ROOT / "metadata/wano-act-3-coverage.json"
WANO_BASELINE = ROOT / "metadata/phase4b/season-16.json"
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


def wano_coverage():
    review = json.loads(WANO_REVIEW.read_text(encoding="utf-8"))
    baseline = json.loads(WANO_BASELINE.read_text(encoding="utf-8"))["entries"]
    sources = {(e["collection_id"], e["file_id"], e["source_sha256"], e["edit_number"]) for e in baseline}
    seen = set()
    rows = []
    for edit in review["edits"]:
        file_id, digest = edit["file_id"], edit["sha256"]
        if (review["collection_id"], file_id, digest, edit["muhn_edit"]) not in sources or file_id in seen:
            raise ValueError(f"Unmatched or duplicate Wano source {file_id}")
        seen.add(file_id)
        numbers = [original["number"] for original in edit["originals"]]
        if not numbers or numbers != sorted(set(numbers)) or any(n <= 0 for n in numbers):
            raise ValueError(f"Invalid reviewed Wano coverage for {file_id}")
        rows.append(f'    ("{file_id}" to "{digest}") to listOf({", ".join(map(str, numbers))}),\n')
    return "internal val reviewedWanoCoverage = mapOf(\n" + "".join(rows) + ")\n"


def generate(target):
    titles = {}
    with ANIME_TITLES.open(encoding="utf-8", newline="") as source:
        for row in csv.DictReader(source):
            number = int(row["number"])
            title = row["title"].strip()
            if number <= 0 or number in titles or not title or not row["source_url"].startswith("https://"):
                raise ValueError(f"Missing or duplicate anime title evidence for {number}")
            titles[number] = title
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
                           f'        {kotlin_string(row["release"])}, {kotlin_string(synopsis)},\n    ),\n')
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text("package com.muhnpacedirect.egghead\n\n"
                      "internal val reviewedEggheadReleases = mapOf<String, EggheadRelease>(\n"
                      + "".join(entries) + ")\n\n"
                      + "internal val eggheadAnimeTitles = mapOf(\n"
                      + "".join(f"    {n} to {kotlin_string(t)},\n" for n, t in titles.items())
                      + ")\n\n" + wano_coverage(), encoding="utf-8")


if __name__ == "__main__":
    generate(Path(sys.argv[1]))
