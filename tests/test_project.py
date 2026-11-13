"""Offline structural checks. Not a replacement for a real Android/Cloudstream build."""
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parents[1]
SOURCE = (ROOT / 'MuhnPace/src/main/kotlin/com/muhnpacedirect/MuhnPaceProvider.kt').read_text()

class MuhnPaceSourceChecks(unittest.TestCase):
    def test_sixteen_real_guide_collection_ids(self):
        links = re.findall(r'Arc\("[^"]+", "([A-Za-z0-9_-]+)"\)', SOURCE)
        self.assertEqual(len(links), 16)
        self.assertEqual(len(set(links)), 16)
        self.assertIn('f8HTXmAk', links)
        self.assertEqual(links[-1], 'sF9stHJo')

    def test_direct_https_not_torrent(self):
        self.assertIn('"$mainUrl/api/list/${arc.listId}"', SOURCE)
        self.assertIn('"$mainUrl/api/file/$fileId"', SOURCE)
        self.assertIn('ExtractorLinkType.VIDEO', SOURCE)
        self.assertNotIn('magnet:', SOURCE)
        self.assertNotIn('torrent://', SOURCE)

    def test_previous_raw_file_id_bug_fixed(self):
        self.assertNotIn('newEpisode(file.getString("id"))', SOURCE)
        self.assertIn('newEpisode("$mainUrl/api/file/$fileId")', SOURCE)

    def test_consistent_playback_url_and_id_validation(self):
        self.assertIn('DIRECT_FILE_URL.matches(data)', SOURCE)
        self.assertIn('FILE_ID.matches(id)', SOURCE)
        self.assertIn('newExtractorLink', SOURCE)

    def test_workflow_builds_using_official_gradle_plugin(self):
        workflow = (ROOT / '.github/workflows/build.yml').read_text()
        self.assertIn('repository: recloudstream/extensions', workflow)
        self.assertIn('./gradlew :MuhnPace:make', workflow)
        self.assertIn('publish.py', workflow)

if __name__ == '__main__':
    unittest.main()
