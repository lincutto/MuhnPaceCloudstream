"""Offline checks link the checked-in art to the independently fetched URL evidence."""

import hashlib
import json
from pathlib import Path
import struct
import unittest
from urllib.parse import urlparse
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
ARTWORK = ROOT / "assets/artwork"
RESOURCES = ROOT / "MuhnPace/src/test/resources"


class ArtworkChecks(unittest.TestCase):
    def test_all_collections_have_matching_verified_assets(self):
        catalogue = json.loads((ARTWORK / "catalogue.json").read_text())["collections"]
        snapshot = json.loads((RESOURCES / "catalogue-2026-11-14.json").read_text())["collections"]
        proof = json.loads((RESOURCES / "artwork-verification-2026-11-14.json").read_text())
        self.assertEqual({c["id"] for c in snapshot}, {c["collection_id"] for c in catalogue})
        records = {(r["collection_id"], r["kind"]): r for r in proof["images"]}
        self.assertEqual(32, len(records))
        all_hashes = set()
        for collection in catalogue:
            for kind in ("poster", "background"):
                entry = collection[kind]
                record = records[(collection["collection_id"], kind)]
                data = (ARTWORK / entry["file"]).read_bytes()
                self.assertEqual(b"\x89PNG\r\n\x1a\n", data[:8])
                self.assertEqual((entry["width"], entry["height"]), struct.unpack(">II", data[16:24]))
                self.assertEqual(entry["sha256"], hashlib.sha256(data).hexdigest())
                for field in ("width", "height", "bytes", "sha256"):
                    self.assertEqual(entry[field], record[field])
                parsed = urlparse(record["url"])
                self.assertEqual("https", parsed.scheme)
                self.assertEqual("raw.githubusercontent.com", parsed.netloc)
                self.assertEqual(
                    f'/lincutto/MuhnPaceCloudstream/{proof["artwork_commit"]}/assets/artwork/{entry["file"]}',
                    parsed.path,
                )
                all_hashes.add(record["sha256"])
        self.assertEqual(32, len(all_hashes), "Each collection and image role has a distinct export")

    def test_vector_sources_have_no_external_assets(self):
        sources = list(ARTWORK.glob("*.svg"))
        self.assertEqual(32, len(sources))
        for source in sources:
            svg = ET.fromstring(source.read_text())
            for element in svg.iter():
                self.assertNotIn(element.tag.split("}")[-1], {"image", "script", "foreignObject"})
                self.assertFalse(any(key.endswith("href") for key in element.attrib))
            self.assertIsNotNone(svg.find("{http://www.w3.org/2000/svg}title"))


if __name__ == "__main__":
    unittest.main()
