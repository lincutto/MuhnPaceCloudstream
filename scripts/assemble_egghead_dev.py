"""Check the compiled development plugin and prepare its Fire TV repository locally."""
import hashlib
import json
import shutil
from pathlib import Path
from zipfile import ZipFile

MODULE = "MuhnPaceEggheadDev"
NAME = "Muhn Pace Rework • Egghead Dev (by lincutto)"
BRANCH = "codex/egghead-expansion"
REPO = "lincutto/MuhnPaceCloudstream"
BASE = f"https://raw.githubusercontent.com/{REPO}/{BRANCH}/dev-install"
BUILD = Path(__file__).resolve().parents[1] / MODULE / "build"


def main():
    package = BUILD / f"{MODULE}.cs3"
    with ZipFile(package) as archive:
        assert archive.testzip() is None
        manifest = json.loads(archive.read("manifest.json"))
        assert manifest["name"] == MODULE and manifest["version"] == 1
        assert manifest["pluginClassName"] == "com.muhnpaceeggheaddev.EggheadDevPlugin"
        dex = archive.read("classes.dex")
        assert dex.startswith(b"dex\n")
        for value in (b"Lcom/muhnpaceeggheaddev/EggheadDevPlugin;", b"Lcom/muhnpaceeggheaddev/EggheadDevProvider;",
                      b"/muhnpace-egghead-dev/one-piece", NAME.encode()):
            assert value in dex
        assert b"Lcom/muhnpacedirect/" not in dex and b"Lcom/muhnpacepreview/" not in dex
    out = BUILD / "fire-tv"
    out.mkdir(exist_ok=True)
    shutil.copyfile(package, out / package.name)
    digest = hashlib.sha256(package.read_bytes()).hexdigest()
    entry = dict(internalName=MODULE, name=NAME, version=manifest["version"], apiVersion=1, status=3,
                 description="Development test of v7 coexistence and English-only playback selection.",
                 authors=["lincutto"], tvTypes=["TvSeries"], language="en",
                 repositoryUrl=f"https://github.com/{REPO}/tree/{BRANCH}",
                 url=f"{BASE}/{package.name}", fileSize=package.stat().st_size, fileHash="sha256-" + digest)
    index = dict(name=NAME, manifestVersion=1, pluginLists=[f"{BASE}/plugins.json"])
    for name, data in (("repo.json", index), ("plugins.json", [entry])):
        (out / name).write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n")
    print(f"{package.name} passed manifest, DEX and identity checks. SHA-256 {digest}")


if __name__ == "__main__":
    main()
