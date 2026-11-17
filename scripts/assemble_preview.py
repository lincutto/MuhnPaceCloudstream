#!/usr/bin/env python3
"""Assemble/validate preview-only repository files locally. Never uploads anything."""
import argparse
import hashlib
import json
from pathlib import Path
import re
import shutil
from zipfile import ZipFile

REPO = "lincutto/MuhnPaceCloudstream"
BRANCH = "v3-preview"
BASE = f"https://raw.githubusercontent.com/{REPO}/{BRANCH}"
INTERNAL = "MuhnPacePreview"
DISPLAY = "Muhn Pace v3 Preview"
PACKAGE = INTERNAL + ".cs3"


def validate(out):
    repo = json.loads((out / "repo.json").read_text())
    [entry] = json.loads((out / "plugins.json").read_text())
    proof = json.loads((out / "preview-build.json").read_text())
    package = out / PACKAGE
    digest = hashlib.sha256(package.read_bytes()).hexdigest()
    assert repo["name"] == DISPLAY and repo["manifestVersion"] == 1
    assert repo["pluginLists"] == [f"{BASE}/plugins.json"]
    assert entry["name"] == DISPLAY and entry["internalName"] == INTERNAL
    assert entry["url"] == f"{BASE}/{PACKAGE}"
    assert entry["version"] == 5 and entry["apiVersion"] == 1 and entry["status"] == 3
    assert entry["fileHash"] == "sha256-" + digest
    assert entry["fileSize"] == package.stat().st_size
    assert proof["sha256"] == digest and proof["artifact_branch"] == BRANCH
    assert re.fullmatch(r"[a-f0-9]{40}", proof["source_commit"])
    with ZipFile(package) as z:
        assert z.testzip() is None
        manifest = json.loads(z.read("manifest.json"))
        assert manifest["name"] == INTERNAL and manifest["version"] == entry["version"]
        assert manifest["pluginClassName"] == "com.muhnpacepreview.MuhnPacePreviewPlugin"
        dex = z.read("classes.dex")
        assert dex.startswith(b"dex\n")
        assert b"Lcom/muhnpacepreview/MuhnPacePreviewPlugin;" in dex
        assert b"Lcom/muhnpacepreview/MuhnPacePreviewProvider;" in dex
        assert b"Lcom/muhnpacedirect/" not in dex
        assert b"/muhnpace-v3-preview/l/" in dex
        assert b"/muhnpace-v3-preview/one-piece" in dex
        for value in (b"ONE_PACE_DERIVED", b"CONFLICTING", b"Q5RqeqED", b"White Town", b"SeasonData"):
            assert value in dex
        assert "One Piece • Muhn Pace (English dub)".encode("utf-8") in dex
    assert not (out / "MuhnPace.cs3").exists(), "Production artifact must never enter preview repository"
    print(f"Validated {PACKAGE}: isolated manifest/classes/routes, DEX, URLs, SHA-256 and size.")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--build-dir", type=Path, default=Path("MuhnPacePreview/build"))
    parser.add_argument("--out", type=Path, required=True)
    parser.add_argument("--source-sha")
    parser.add_argument("--verify-only", action="store_true")
    args = parser.parse_args()
    if not args.verify_only:
        if not re.fullmatch(r"[a-f0-9]{40}", args.source_sha or ""):
            parser.error("--source-sha requires the full source commit")
        args.out.mkdir(parents=True, exist_ok=True)
        source = args.build_dir / PACKAGE
        shutil.copy2(source, args.out / PACKAGE)
        digest = hashlib.sha256(source.read_bytes()).hexdigest()
        entry = dict(internalName=INTERNAL, name=DISPLAY, version=5, apiVersion=1, status=3,
                     description="Isolated v3 visual preview; separate temporary watch history; no progress migration.",
                     authors=["Independent prototype"], tvTypes=["TvSeries"], language="en",
                     repositoryUrl=f"https://github.com/{REPO}/tree/codex/v3-overhaul",
                     url=f"{BASE}/{PACKAGE}", fileSize=source.stat().st_size, fileHash="sha256-" + digest)
        files = {
            "repo.json": dict(name=DISPLAY, description="Temporary Fire TV preview; production installation is separate.",
                              manifestVersion=1, pluginLists=[f"{BASE}/plugins.json"]),
            "plugins.json": [entry],
            "preview-build.json": dict(source_commit=args.source_sha, artifact_branch=BRANCH,
                                       sha256=digest, history="Separate preview identities; no transfer promise."),
        }
        for name, data in files.items():
            (args.out / name).write_text(json.dumps(data, indent=2) + "\n")
    validate(args.out)


if __name__ == "__main__":
    main()
