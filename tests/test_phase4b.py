"""Dataset consistency and identity checks, not a substitute for source or device review."""
import importlib.util
import json
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location('art_audit', ROOT/'scripts/audit_episode_artwork.py')
audit = importlib.util.module_from_spec(spec)
spec.loader.exec_module(audit)


def rows():
    return [row for file in sorted((ROOT/'metadata/phase4b').glob('season-*.json'))
            for row in json.loads(file.read_text())['entries']]


class Phase4bChecks(unittest.TestCase):
    def test_exact_identity_and_source_correspondence(self):
        baseline = json.loads((ROOT/'MuhnPace/src/test/resources/catalogue-2026-11-14.json').read_text())
        selected = {}
        for season, collection in enumerate(baseline['collections'], 1):
            files = {f['id']: f['name'] for f in collection['files']}
            for e in collection['expected_episodes']:
                selected[(collection['id'], e['file_id'])] = (season, e['number'], files[e['file_id']])
        records = rows()
        self.assertEqual(274, len(records))
        self.assertEqual(len(records), len({(r['collection_id'], r['file_id']) for r in records}))
        for r in records:
            self.assertEqual(selected[r['collection_id'], r['file_id']],
                             (r['season'], r['edit_number'], r['source_filename']))
            self.assertRegex(r['source_sha256'], r'^[a-f0-9]{64}$')
            self.assertTrue(r['correspondence_evidence']['qualification'])
            self.assertTrue(r['correspondence_evidence']['source_url'].startswith('https://'))

    def test_accepted_numbers_match_frozen_published_facts(self):
        facts = {e['file_id']: e for e in json.loads((ROOT/'metadata/phase4b/reference-facts.json').read_text())['entries']}
        for r in rows():
            self.assertNotEqual('DIRECTLY_VERIFIED', r['mapping_status'])
            if r['mapping_status'] == 'ONE_PACE_DERIVED':
                ref = facts[r['file_id']]
                expected = set()
                for group in ref['original_label'].split(','):
                    span = list(map(int, group.strip().split('-')))
                    expected.update(range(span[0], span[-1]+1))
                self.assertEqual(sorted(expected), r['accepted_original_episodes'])
                self.assertEqual(ref['title'], r['display_title'])
            else:
                self.assertEqual([], r['accepted_original_episodes'])
                self.assertEqual('UNKNOWN', r['mapping_status'])
                self.assertEqual('EDITORIAL_SCENE_LABEL', r['title_kind'])
                self.assertTrue(r['correspondence_evidence']['observations'])
                self.assertEqual('https://pixeldrain.dev/api/file/'+r['file_id'], r['title_source_url'])

    def test_synopses_are_individual_stories_without_internal_labels(self):
        records = rows()
        # The separately retained Fishman alternate may describe the same story.
        descriptions = [r['synopsis'] for r in records if r['season'] != 10]
        self.assertEqual(len(descriptions), len(set(descriptions)))
        for r in records:
            self.assertTrue(35 <= len(r['synopsis']) <= 500)
            for text in [r['display_title'], r['synopsis']]:
                self.assertNotRegex(text.lower(), r'muhn pace|metadata|unverified|original episodes unknown|unavailable|navigation|derived|mapping|source file')
            self.assertFalse(re.match(r'^\d+[.]', r['display_title']))

    def test_curated_image_identity_integrity_and_uniqueness(self):
        images = json.loads((ROOT/'assets/phase4b/manifest.json').read_text())['images']
        thumbs = {i['file_id']: i for i in images if i['role'] == 'episode_thumbnail'}
        self.assertFalse(audit.near_duplicates(list(thumbs.values())))
        for r in rows():
            image = thumbs[r['file_id']]
            self.assertEqual(r['collection_id'], image['collection_id'])
            self.assertEqual(r['source_filename'], image['source_filename'])
            self.assertEqual(image['file'], r['thumbnail']['file'])
            self.assertRegex(r['thumbnail']['artwork_commit'], r'^[a-f0-9]{40}$')
        for image in images:
            measured = audit.inspect(ROOT/image['file'])
            for key in ['width', 'height', 'sha256', 'dhash', 'format']:
                self.assertEqual(image[key], measured[key])
            self.assertGreater(measured['mean_luma'], 10)
            self.assertGreater(measured['stddev_luma'], 10)
            self.assertLess((ROOT/image['file']).stat().st_size, 600_000)
        poster, background = [i for i in images if i['role'].startswith('series_')]
        self.assertEqual((768, 1152), (poster['width'], poster['height']))
        self.assertEqual((1280, 721), (background['width'], background['height']))
        self.assertEqual('IMG_9122.jpeg', poster['source_filename'])
        self.assertEqual('IMG_9123.jpeg', background['source_filename'])
        self.assertEqual(background['source_sha256'], background['sha256'],
                         'Picture 2 must remain the unchanged supplied JPEG')
        self.assertNotEqual(poster['source_sha256'], background['source_sha256'])


if __name__ == '__main__':
    unittest.main()
