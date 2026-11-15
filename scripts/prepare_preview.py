#!/usr/bin/env python3
"""Generate an isolated preview from the current provider, without editing production sources."""
import argparse
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "MuhnPace/src/main/kotlin/com/muhnpacedirect"


def replace_checked(text, old, new, count):
    if text.count(old) != count:
        raise ValueError(f"Preview source contract changed: expected {count} occurrences of {old!r}")
    return text.replace(old, new)


def transform(name, text):
    text = replace_checked(text, "package com.muhnpacedirect", "package com.muhnpacepreview", 1)
    if name == "MuhnPaceProvider.kt":
        text = replace_checked(text, "class MuhnPaceProvider", "class MuhnPacePreviewProvider", 1)
        text = replace_checked(text, 'name = "Muhn Pace Direct (English Dub)"', 'name = "Muhn Pace v3 Preview"', 1)
        text = replace_checked(text, '(Muhn Pace)"', '(Muhn Pace Preview)"', 1)
        text = replace_checked(text, '"muhn" to "Muhn Pace • English dub arcs"',
                               '"muhn-preview" to "Muhn Pace v3 Preview • English dub arcs"', 1)
        # Virtual catalogue routes are dispatched to load(), never requested from Pixeldrain.
        # They isolate the host's URL-derived series AND episode history keys.
        text = replace_checked(text, '$mainUrl/l/', '$mainUrl/muhnpace-v3-preview/l/', 2)
        text = replace_checked(text, 'https://pixeldrain.com/l/', 'https://pixeldrain.com/muhnpace-v3-preview/l/', 1)
    elif name == "MuhnPacePlugin.kt":
        text = replace_checked(text, "class MuhnPacePlugin", "class MuhnPacePreviewPlugin", 1)
        text = replace_checked(text, "MuhnPaceProvider()", "MuhnPacePreviewProvider()", 1)
    return text


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--out", type=Path, required=True)
    args = parser.parse_args()
    # Only generated build output may be replaced, never a source directory.
    expected = ROOT / "MuhnPacePreview/build/generated/preview/kotlin"
    if args.out.resolve() != expected.resolve():
        raise SystemExit(f"Output must be {expected}")
    generated = {p.name: transform(p.name, p.read_text()) for p in SOURCE.glob("*.kt")}
    if args.out.exists():
        shutil.rmtree(args.out)
    target = args.out / "com/muhnpacepreview"
    target.mkdir(parents=True)
    for name, text in generated.items():
        (target / name.replace("MuhnPaceProvider", "MuhnPacePreviewProvider")
         .replace("MuhnPacePlugin", "MuhnPacePreviewPlugin")).write_text(text)
    print(f"Generated {len(generated)} preview Kotlin files; production sources untouched.")


if __name__ == "__main__":
    main()
