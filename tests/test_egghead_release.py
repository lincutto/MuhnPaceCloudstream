"""Cross-check the source review and the artwork actually delivered for those files."""
import csv
import json
from pathlib import Path
import unittest

from scripts.audit_episode_artwork import inspect, near_duplicates
from scripts.prepare_egghead_releases import REVIEW, anime_episodes

ROOT = Path(__file__).resolve().parents[1]


class EggheadReleaseChecks(unittest.TestCase):
    def test_every_reviewed_file_has_one_distinct_decodable_still(self):
        with REVIEW.open(newline="", encoding="utf-8") as source:
            rows = [r for r in csv.DictReader(source) if r["native_review"] == "verified"]
        images = json.loads((ROOT / "assets/egghead/manifest.json").read_text())["images"]
        self.assertCountEqual([r["pixeldrain_file_id"] for r in rows], [r["file_id"] for r in images])
        for image in images:
            self.assertEqual(f"assets/egghead/episodes/{image['file_id']}.jpg", image["file"])
            decoded = inspect(ROOT / image["file"])
            self.assertEqual("JPEG", decoded["format"])
            self.assertEqual((480, 270), (decoded["width"], decoded["height"]))
            self.assertEqual(image["sha256"], decoded["sha256"])
            self.assertEqual(image["dhash"], decoded["dhash"])
            self.assertGreater(decoded["stddev_luma"], 10)
            self.assertGreater(decoded["mean_luma"], 10)
        old = json.loads((ROOT / "assets/phase4b/manifest.json").read_text())["images"]
        self.assertEqual([], near_duplicates(images + [r for r in old if r["role"] == "episode_thumbnail"]))

    def test_discontinuous_coverage_survives_embedding(self):
        self.assertEqual("1108, 1110, 1111", anime_episodes("1108, 1110–1111"))
        self.assertEqual("955", anime_episodes("955–955"))
        for label in ("", "0", "1108, 1111–1110", "1108, 1108"):
            with self.subTest(label=label), self.assertRaises(ValueError):
                anime_episodes(label)
