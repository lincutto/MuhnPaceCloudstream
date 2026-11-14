"""Check the development/production boundary using parsed workflow configuration."""
from pathlib import Path
import unittest

import yaml

ROOT = Path(__file__).resolve().parents[1]


def workflow(name):
    # GitHub uses YAML 1.2; BaseLoader keeps the key 'on' from becoming YAML 1.1 True.
    return yaml.load((ROOT / '.github/workflows' / name).read_text(), Loader=yaml.BaseLoader)


class WorkflowSafetyChecks(unittest.TestCase):
    def test_development_has_only_read_permissions_and_no_checkout_credentials(self):
        data = workflow('validate.yml')
        self.assertEqual(data['permissions'], {'contents': 'read'})
        self.assertEqual(set(data['jobs']), {'validate'})
        for job in data['jobs'].values():
            self.assertEqual(job.get('permissions', data['permissions']), {'contents': 'read'})
            for step in job['steps']:
                if step.get('uses', '').startswith('actions/checkout@'):
                    self.assertEqual(step['with']['persist-credentials'], 'false')

    def test_development_triggers_and_steps_cannot_publish(self):
        data = workflow('validate.yml')
        self.assertEqual(set(data['on']), {'push', 'pull_request', 'workflow_dispatch'})
        self.assertEqual(data['on']['push']['branches'], ['codex/**'])
        allowed_actions = {'actions/checkout', 'actions/setup-java', 'actions/upload-artifact'}
        for job in data['jobs'].values():
            self.assertNotIn('uses', job)  # No hidden reusable publishing workflow.
            for step in job['steps']:
                if 'uses' in step:
                    self.assertIn(step['uses'].split('@')[0], allowed_actions)
                command = step.get('run', '')
                self.assertNotRegex(command, r'\bgit\s+[^\n]*\bpush\b|\bgh\s+(?:release|api)\b')
        self.assertNotIn('secrets.', str(data))

    def test_production_is_guarded_at_both_job_and_publish_step(self):
        data = workflow('build.yml')
        self.assertEqual(set(data['on']), {'push', 'workflow_dispatch'})
        self.assertEqual(data['on']['push']['branches'], ['main'])
        self.assertEqual(data['permissions'], {'contents': 'read'})
        self.assertEqual(set(data['jobs']), {'build'})
        job = data['jobs']['build']
        self.assertEqual(job['if'], "github.ref == 'refs/heads/main'")
        self.assertEqual(job['permissions'], {'contents': 'write'})
        [publish] = [s for s in job['steps'] if s['name'] == 'Publish the Cloudstream repository']
        self.assertEqual(publish['if'], "github.ref == 'refs/heads/main'")
        self.assertIn('HEAD:refs/heads/builds', publish['run'])

    def test_both_workflows_require_executable_kotlin_tests_and_compilation(self):
        for name, job_name in [('build.yml', 'build'), ('validate.yml', 'validate')]:
            with self.subTest(workflow=name):
                steps = workflow(name)['jobs'][job_name]['steps']
                [build_index] = [i for i, s in enumerate(steps) if ':MuhnPace:make' in s.get('run', '')]
                build = steps[build_index]
                self.assertIn(':MuhnPace:testDebugUnitTest', build['run'])
                self.assertNotIn('continue-on-error', build)
                self.assertNotIn('||', build['run'])
                self.assertNotIn('if', build)
                assemble_index = next(i for i, s in enumerate(steps) if 'scripts/publish.py' in s.get('run', ''))
                self.assertLess(build_index, assemble_index)

    def test_development_retains_test_reports_even_on_failure(self):
        steps = workflow('validate.yml')['jobs']['validate']['steps']
        [reports] = [s for s in steps if 'test-results' in s.get('with', {}).get('path', '')]
        self.assertEqual(reports['if'], 'always()')
        self.assertIn('testDebugUnitTest', reports['with']['path'])


if __name__ == '__main__':
    unittest.main()
