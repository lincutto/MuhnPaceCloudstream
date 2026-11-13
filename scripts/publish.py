#!/usr/bin/env python3
"""Assemble a Cloudstream repository from a *genuinely compiled* .cs3 file."""
import argparse
import hashlib
import json
from pathlib import Path
import shutil


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--build-dir', required=True, type=Path)
    parser.add_argument('--repo', required=True)
    parser.add_argument('--out', required=True, type=Path)
    args = parser.parse_args()

    artifacts = sorted(args.build_dir.glob('*.cs3'))
    if len(artifacts) != 1:
        raise SystemExit(f'Expected exactly one compiled .cs3; found {len(artifacts)}: {artifacts}')
    if '/' not in args.repo or any(c.isspace() for c in args.repo):
        raise SystemExit('Invalid owner/repository name')

    args.out.mkdir(parents=True, exist_ok=True)
    source = artifacts[0]
    destination = args.out / source.name
    shutil.copy2(source, destination)
    base = f'https://raw.githubusercontent.com/{args.repo}/builds'
    sha256 = hashlib.sha256(destination.read_bytes()).hexdigest()
    entry = {
        'internalName': 'MuhnPace',
        'name': 'MuhnPace',
        'description': 'Muhn Pace English dub from public Pixeldrain lists; subject to host restrictions.',
        'version': 2,
        'apiVersion': 1,
        'status': 3,
        'authors': ['Independent prototype'],
        'tvTypes': ['TvSeries'],
        'language': 'en',
        'repositoryUrl': f'https://github.com/{args.repo}',
        'url': f'{base}/{destination.name}',
        'fileSize': destination.stat().st_size,
        'fileHash': f'sha256-{sha256}',
    }
    (args.out / 'plugins.json').write_text(json.dumps([entry], indent=2) + '\n', encoding='utf-8')
    (args.out / 'repo.json').write_text(json.dumps({
        'name': 'Muhn Pace Direct (English Dub)',
        'description': 'Read-only Pixeldrain catalog for English dub edits',
        'manifestVersion': 1,
        'pluginLists': [f'{base}/plugins.json'],
    }, indent=2) + '\n', encoding='utf-8')
    print(f'Published files for {args.repo}: {source.name}, plugins.json, repo.json')
    print(f'Cloudstream repository: {base}/repo.json')


if __name__ == '__main__':
    main()
