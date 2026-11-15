"""Check evidence boundaries and exact-file associations; passing tests do not verify story truth."""
import importlib.util
import json
from pathlib import Path
import subprocess
import unittest

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location('image_audit', ROOT / 'scripts/audit_episode_artwork.py')
audit = importlib.util.module_from_spec(spec)
spec.loader.exec_module(audit)


class Phase4aChecks(unittest.TestCase):
    def setUp(self):
        self.research = json.loads((ROOT / 'metadata/dressrosa-30-37.json').read_text())
        self.images = json.loads((ROOT / 'assets/phase4a/manifest.json').read_text())['images']

    def test_source_identity_and_story_records_cover_only_reviewed_sample(self):
        entries = self.research['entries']
        self.assertEqual(list(range(30, 38)), [e['edit_number'] for e in entries])
        source = json.loads((ROOT / 'MuhnPace/src/test/resources/catalogue-2026-11-14.json').read_text())
        collection = next(c for c in source['collections'] if c['id'] == 'f8HTXmAk')
        files = {f['id']: f['name'] for f in collection['files']}
        selected = {e['number']: e['file_id'] for e in collection['expected_episodes']}
        for entry in entries:
            self.assertEqual('f8HTXmAk', entry['collection_id'])
            self.assertEqual(selected[entry['edit_number']], entry['file_id'])
            self.assertEqual(files[entry['file_id']], entry['source_filename'])
            self.assertRegex(entry['source_sha256'], r'^[0-9a-f]{64}$')
            self.assertEqual(entry['file_id'], entry['thumbnail']['file_id'])
            self.assertEqual(entry['source_sha256'], entry['thumbnail']['media_sha256_reported'])
            self.assertTrue(entry['title_source_url'].endswith('/main/title.properties'))
            for key in ('method', 'exact_file_index', 'title_source', 'coverage_source', 'reviewed_on', 'qualification'):
                self.assertTrue(entry['correspondence_evidence'][key])
        self.assertEqual(8, len({e['synopsis'] for e in entries}))

    def test_conflicting_sources_do_not_supply_display_ranges(self):
        entries = self.research['entries']
        self.assertEqual(0, self.research['verified_full_mappings_added'])
        self.assertEqual(3, sum(e['mapping_status'] == 'ONE_PACE_DERIVED' for e in entries))
        self.assertEqual(5, sum(e['mapping_status'] == 'CONFLICTING' for e in entries))
        for entry in entries:
            if entry['mapping_status'] == 'CONFLICTING':
                self.assertEqual([], entry['accepted_original_episodes'])
                self.assertFalse(entry['conflict_review']['sources_agree'])
            else:
                self.assertEqual(entry['candidate_original_episodes'], entry['accepted_original_episodes'])
                self.assertTrue(entry['conflict_review']['sources_agree'])
        law = next(e for e in entries if e['file_id'] == 'Q5RqeqED')
        self.assertEqual([701], law['scene_confirmed_episodes'])
        self.assertEqual([701, 702], law['accepted_original_episodes'])

    def test_images_are_intact_distinct_and_associated_with_exact_files(self):
        thumbs = [i for i in self.images if i['role'] == 'episode_thumbnail']
        self.assertEqual(8, len(thumbs))
        self.assertEqual([], audit.near_duplicates(thumbs))
        for entry in self.images:
            image = audit.inspect(ROOT / entry['file'])
            for key in ('sha256', 'width', 'height', 'format', 'dhash'):
                self.assertEqual(entry[key], image[key])
            self.assertGreater(image['mean_luma'], 10)
            self.assertGreater(image['stddev_luma'], 10)
            self.assertGreater(entry['timestamp_seconds'], 100)
        for entry in self.research['entries']:
            self.assertEqual(entry['thumbnail'], next(i for i in thumbs if i['file_id'] == entry['file_id']))

    def test_generated_runtime_data_matches_reviewed_records(self):
        subprocess.run(['python3', str(ROOT / 'scripts/generate_episode_metadata.py'), '--check'], check=True)

    def test_no_claimed_rightsholder_licence_or_runtime_metadata_service_dependency(self):
        rights = (ROOT / 'assets/phase4a/README.md').read_text()
        self.assertIn('not an assertion of a rightsholder-granted licence', rights)
        self.assertRegex(self.research['artwork_commit'], r'^[0-9a-f]{40}$')
        generated = (ROOT / 'MuhnPace/src/main/kotlin/com/muhnpacedirect/ResearchDataset.kt').read_text()
        self.assertIn('/' + self.research['artwork_commit'] + '/assets/phase4a', generated)
        self.assertNotIn('image.tmdb.org', generated)
        self.assertNotIn('api_key=', generated)


if __name__ == '__main__':
    unittest.main()
