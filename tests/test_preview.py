"""Safety boundaries for generated preview sources and read-only validation CI."""
import importlib.util
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location("prepare_preview", ROOT / "scripts/prepare_preview.py")
preview = importlib.util.module_from_spec(spec)
spec.loader.exec_module(preview)


class PreviewSafetyChecks(unittest.TestCase):
    def test_generated_provider_changes_identity_but_not_playback(self):
        original = (preview.SOURCE / "MuhnPaceProvider.kt").read_text()
        generated = preview.transform("MuhnPaceProvider.kt", original)
        self.assertEqual(original.split("override suspend fun loadLinks", 1)[1],
                         generated.split("override suspend fun loadLinks", 1)[1])
        self.assertIn('name = "Muhn Pace v3 Preview"', generated)
        self.assertNotIn('"$mainUrl/l/', generated)
        self.assertNotIn('"https://pixeldrain.com/l/', generated)
        self.assertIn('"$mainUrl/api/list/${arc.listId}"', generated)
        self.assertIn('"$mainUrl/api/file/$fileId"', generated)

    def test_unexpected_source_change_fails_closed(self):
        original = (preview.SOURCE / "MuhnPaceProvider.kt").read_text()
        with self.assertRaises(ValueError):
            preview.transform("MuhnPaceProvider.kt", original.replace('$mainUrl/l/', '$mainUrl/new-route/'))

    def test_preview_is_opt_in_and_development_ci_builds_it(self):
        self.assertIn('if (providers.gradleProperty("preview").orNull == "true")',
                      (ROOT / "settings.gradle.kts").read_text())
        ci = (ROOT / ".github/workflows/validate.yml").read_text()
        self.assertIn("-Ppreview=true", ci)
        self.assertIn(":MuhnPacePreview:testDebugUnitTest", ci)
        self.assertIn(":MuhnPacePreview:make", ci)
        self.assertIn("scripts/assemble_preview.py", ci)
        self.assertNotIn("MuhnPacePreview", (ROOT / ".github/workflows/build.yml").read_text())


if __name__ == "__main__":
    unittest.main()
