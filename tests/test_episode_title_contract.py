"""Frozen acceptance data cross-check; this does not test Android rendering."""
import json
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]


class EpisodeTitleContractChecks(unittest.TestCase):
    def test_frozen_names_numbers_and_files_match_reviewed_catalogue(self):
        contract = json.loads((ROOT / 'MuhnPace/src/test/resources/v3-episode-contract.json').read_text())
        baseline = json.loads((ROOT / 'MuhnPace/src/test/resources/catalogue-2026-11-14.json').read_text())
        facts = {r['file_id']: r for r in json.loads(
            (ROOT / 'metadata/phase4b/reference-facts.json').read_text())['entries']}
        metadata = {r['file_id']: r for p in (ROOT / 'metadata/phase4b').glob('season-*.json')
                    for r in json.loads(p.read_text())['entries']}
        expected = []
        for season, collection in enumerate(baseline['collections'], 1):
            for position, ep in enumerate(collection['expected_episodes'], 1):
                expected.append((ep['file_id'], season, ep['number'], position))
        self.assertEqual(274, len(contract))
        self.assertEqual(set(range(1, 17)), {r['season'] for r in contract})
        self.assertEqual(expected, [(r['file_id'], r['season'], r['edit'], r['episode']) for r in contract])
        ranged = 0
        for row in contract:
            meta = metadata[row['file_id']]
            name = meta['display_title']
            if meta['accepted_original_episodes']:
                name += ' - ' + facts[row['file_id']]['original_label']
                ranged += 1
            self.assertEqual(name, row['name'])
            self.assertNotRegex(row['name'], r'^\s*\d+\.\s|Muhn Pace|S\d+:E\d+')
        self.assertEqual(249, ranged)  # 25 late-Wano suffixes are approximate display only, not accepted mappings.


if __name__ == '__main__':
    unittest.main()
