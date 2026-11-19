#!/usr/bin/env python3
"""Assemble local repository files from a compiled .cs3; this script makes no network requests."""
import argparse
import hashlib
import json
from pathlib import Path
import shutil
from zipfile import ZipFile


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

    source = artifacts[0]
    if source.name != 'MuhnPace.cs3':
        raise SystemExit(f'Unexpected production package name: {source.name}')
    with ZipFile(source) as package:
        manifest = json.loads(package.read('manifest.json'))
        dex = package.read('classes.dex')
        if (package.testzip() is not None or manifest['name'] != 'MuhnPace' or manifest['version'] != 8
                or manifest['pluginClassName'] != 'com.muhnpacedirect.MuhnPacePlugin'
                or b'Lcom/muhnpacedirect/egghead/ReworkProvider;' not in dex
                or b'Lcom/muhnpaceeggheaddev/' in dex or b'Lcom/muhnpacepreview/' in dex):
            raise SystemExit('The compiled package does not contain the isolated production provider.')

    args.out.mkdir(parents=True, exist_ok=True)
    destination = args.out / source.name
    shutil.copy2(source, destination)
    base = f'https://raw.githubusercontent.com/{args.repo}/builds'
    sha256 = hashlib.sha256(destination.read_bytes()).hexdigest()
    entry = {
        'internalName': 'MuhnPace',
        'name': 'Muhn Pace Rework (by lincutto)',
        'description': 'Unofficial Cloudstream extension for the Muhn Pace English dub fan edit by Muhny.',
        'version': manifest['version'],
        'apiVersion': 1,
        'status': 1,
        'authors': ['lincutto'],
        'tvTypes': ['TvSeries'],
        'language': 'en',
        'repositoryUrl': f'https://github.com/{args.repo}',
        'url': f'{base}/{destination.name}',
        'fileSize': destination.stat().st_size,
        'fileHash': f'sha256-{sha256}',
    }
    (args.out / 'plugins.json').write_text(json.dumps([entry], indent=2) + '\n', encoding='utf-8')
    (args.out / 'repo.json').write_text(json.dumps({
        'name': 'Muhn Pace Rework (by lincutto)',
        'description': 'Unofficial Cloudstream extension; Muhn Pace editing project by Muhny',
        'manifestVersion': 1,
        'pluginLists': [f'{base}/plugins.json'],
    }, indent=2) + '\n', encoding='utf-8')
    print(f'Assembled {source.name}, plugins.json and repo.json for {args.repo}. Package SHA-256 {sha256}')
    print(f'Cloudstream repository: {base}/repo.json')


if __name__ == '__main__':
    main()
