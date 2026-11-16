#!/usr/bin/env python3
"""Explicit live identity/image validation; no uploads or full-video downloads."""
import concurrent.futures
from datetime import datetime, timezone
import hashlib
import io
import json
from pathlib import Path
import urllib.request
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
BASE = 'https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream'


def get(url):
    for attempt in range(3):
        try:
            with urllib.request.urlopen(url, timeout=45) as response:
                return response.read(), response.status, response.headers.get_content_type()
        except Exception:
            if attempt == 2:
                raise


def main():
    rows = [r for p in sorted((ROOT/'metadata/phase4b').glob('season-*.json'))
            for r in json.loads(p.read_text())['entries']]
    baseline = json.loads((ROOT/'MuhnPace/src/test/resources/catalogue-2026-11-14.json').read_text())['collections']
    images = json.loads((ROOT/'assets/phase4b/manifest.json').read_text())['images']
    image_urls = {r['thumbnail']['file']: f"{BASE}/{r['thumbnail']['artwork_commit']}/{r['thumbnail']['file']}" for r in rows}
    series = json.loads((ROOT/'metadata/phase4b/series.json').read_text())
    for key in ['poster', 'background']:
        image_urls[series[key]] = f"{BASE}/{series['artwork_commit']}/{series[key]}"

    def check_collection(c):
        url = 'https://pixeldrain.dev/api/list/'+c['id']
        body, status, _ = get(url)
        live = json.loads(body)
        assert live['success']
        # Source ordering matters when equally preferred variants compete.
        assert [(f['id'], f['name']) for f in live['files']] == [(f['id'], f['name']) for f in c['files']], c['id']
        by_id = {f['id']: f for f in live['files']}
        for r in rows:
            if r['collection_id'] == c['id']:
                assert by_id[r['file_id']]['hash_sha256'] == r['source_sha256'], r['file_id']
        return dict(collection_id=c['id'], url=url, status=status,
                    baseline_files=len(c['expected_episodes']), response_sha256=hashlib.sha256(body).hexdigest())

    def check_image(e):
        url = image_urls[e['file']]
        body, status, mime = get(url)
        assert hashlib.sha256(body).hexdigest() == e['sha256'], e['file']
        with Image.open(io.BytesIO(body)) as decoded:
            decoded.load()
            assert decoded.size == (e['width'], e['height'])
            assert decoded.format == 'JPEG' and mime == 'image/jpeg'
        return dict(file=e['file'], url=url, status=status, content_type=mime,
                    width=e['width'], height=e['height'], sha256=e['sha256'])

    with concurrent.futures.ThreadPoolExecutor(max_workers=8) as pool:
        collections = list(pool.map(check_collection, baseline))
        art = list(pool.map(check_image, images))
    report = dict(checked_at=datetime.now(timezone.utc).isoformat(), collections=collections, images=art,
                  limitations='API/media-reported identity and image delivery checks only; no Fire TV playback, persisted resume or visual test.')
    (ROOT/'docs/research/phase4b-live-validation.json').write_text(json.dumps(report, indent=2)+'\n')
    print(f"Live checks passed: {len(collections)} collections, {sum(c['baseline_files'] for c in collections)} unchanged files and {len(art)} published JPEGs.")


if __name__ == '__main__':
    main()
