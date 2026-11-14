#!/usr/bin/env python3
"""Render original, geometric catalogue artwork. No downloaded art or fonts.

Optional authoring dependency: CairoSVG==2.8.2. Runtime/CI do not render assets.
Run from the repository root: python scripts/generate_artwork.py
"""

from html import escape
import hashlib
import json
from pathlib import Path

import cairosvg


OUT = Path(__file__).resolve().parents[1] / "assets" / "artwork"

# ID, slug, title lines, collection label, palette (night, sky, light, accent).
ARCS = [
    ("6VyLVkB2", "enies-lobby", ["ENIES", "LOBBY"], "JUDICIAL ISLAND", ("101d38", "315c83", "cededc", "f4c278")),
    ("jVhFqouN", "post-enies-lobby", ["POST-ENIES", "LOBBY"], "BACK IN WATER SEVEN", ("17283a", "408b9e", "d8e9cf", "efb989")),
    ("NwJKTg21", "thriller-bark", ["THRILLER", "BARK"], "THROUGH THE FLORIAN TRIANGLE", ("21192f", "685a88", "d3c2e3", "edb572")),
    ("KztkSCuY", "sabaody", ["SABAODY", "ARCHIPELAGO"], "THE MANGROVE GROVES", ("112f36", "408d8e", "bce6d9", "e8d79c")),
    ("TuZJgwRL", "amazon-lily", ["AMAZON", "LILY"], "ISLAND OF WARRIORS", ("19332f", "4e8874", "d7e5b0", "ed9e9b")),
    ("4AN9Xxht", "impel-down", ["IMPEL", "DOWN"], "THE UNDERWATER PRISON", ("112132", "3c647e", "b4d2d8", "e3a168")),
    ("oa8oZvZx", "marineford", ["MARINEFORD"], "NAVY HEADQUARTERS", ("15263c", "557b9d", "e2e6d4", "dba9a1")),
    ("MH7AQkgr", "post-marineford", ["POST-", "MARINEFORD"], "TOWARD THE NEXT VOYAGE", ("322538", "a27382", "f1d7b6", "e5bb74")),
    ("ZQhstabS", "fishman-island", ["FISHMAN", "ISLAND"], "TEN THOUSAND METRES BELOW", ("13253c", "3d7d9b", "bcf0e6", "d9b0e8")),
    ("kuoQYqfR", "fishman-special", ["FISHMAN", "ISLAND"], "EDIT 15 / CORRECTED GROUP SHOT", ("18323a", "438e8d", "ccf3de", "ebc8a5")),
    ("A1jFtPvQ", "punk-hazard", ["PUNK", "HAZARD"], "AN ISLAND OF FIRE AND ICE", ("24273b", "5e718b", "d3eaf1", "ef9c78")),
    ("f8HTXmAk", "dressrosa", ["DRESSROSA"], "THE KINGDOM OF TOYS", ("37293e", "aa727f", "f7dfbb", "e9af85")),
    ("totP9Xmm", "zou", ["ZOU"], "A HOMELAND ABOVE THE SEA", ("183538", "558e83", "d8e6c0", "e1c590")),
    ("AyvkmFFZ", "whole-cake", ["WHOLE CAKE", "ISLAND"], "IN BIG MOM'S TERRITORY", ("382338", "a06482", "f3cfd6", "eed18e")),
    ("QZw3Ejpy", "wano-acts-1-2", ["WANO"], "ACTS 1 & 2", ("20273e", "657c9a", "e1dfd7", "edaeae")),
    ("sF9stHJo", "wano-act-3", ["WANO"], "ACT 3 / LATER EDITS", ("29233c", "7f5683", "e4d1d8", "ed9a7c")),
]


def rect(x, y, w, h, fill, radius=0, extra=""):
    return f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{radius}" fill="{fill}" {extra}/>'


def path(d, fill, extra=""):
    return f'<path d="{d}" fill="{fill}" {extra}/>'


def circle(x, y, r, fill, extra=""):
    return f'<circle cx="{x}" cy="{y}" r="{r}" fill="{fill}" {extra}/>'


def text(x, y, value, size, color, extra=""):
    return f'<text x="{x}" y="{y}" font-family="DejaVu Sans, sans-serif" font-size="{size}" fill="{color}" {extra}>{escape(value)}</text>'


def arches(x, y, count, gap, w, h, fill):
    return "".join(rect(x + i * gap, y, w, h, fill, w / 2) for i in range(count))


def waves(light):
    return "".join(path(f"M 35 {y} Q 110 {y-22} 185 {y} T 335 {y} T 485 {y} T 635 {y}", "none",
                        f'stroke="{light}" stroke-width="3" opacity="{opacity}"')
                   for y, opacity in ((550, .35), (582, .2), (614, .12)))


def motif(slug, palette):
    """Original landscape pictograms in a 700 x 650 coordinate space."""
    night, sky, light, accent = ["#" + c for c in palette]
    s = circle(360, 285, 235, sky, 'opacity=".38"')
    s += circle(515, 150, 65, light, 'opacity=".8"')
    if slug == "enies-lobby":
        s += path("M85 505 L130 375 H570 L615 505Z", sky)
        s += rect(145, 342, 410, 145, light) + arches(170, 389, 6, 63, 35, 100, night)
        s += rect(265, 160, 170, 215, light) + rect(244, 150, 212, 24, accent)
        s += rect(290, 113, 120, 40, light) + rect(325, 67, 10, 55, light)
        s += path("M335 67 H391 L373 88 H335Z", accent)
        s += arches(288, 208, 3, 46, 27, 71, night)
        s += rect(315, 298, 70, 76, sky, 32)
        s += path("M244 340 L199 507 H501 L456 340Z", accent, 'opacity=".35"')
    elif slug == "post-enies-lobby":
        s += path("M80 480 Q350 300 620 480 L650 530 H50Z", sky)
        for x, y, h in ((125, 300, 142), (205, 243, 174), (435, 274, 151), (515, 320, 140)):
            s += rect(x, y, 65, h, light)
            s += path(f"M{x-8} {y} L{x+32} {y-40} L{x+73} {y}Z", accent)
            s += arches(x+13, y+24, 2, 26, 12, 25, sky)
        s += rect(290, 187, 115, 213, light) + rect(280, 178, 135, 17, accent)
        s += path("M280 178 L347 122 L415 178Z", accent)
        s += circle(347, 236, 23, sky)
        s += path("M205 448 Q350 270 495 448 L475 474 Q350 330 225 474Z", accent)
        s += path("M327 421 Q285 476 298 531 H405 Q391 477 370 421Z", light, 'opacity=".6"')
    elif slug == "thriller-bark":
        s += path("M72 430 Q350 540 628 430 L575 526 H139Z", light)
        s += path("M185 446 L199 287 H253 V210 H302 V320 H368 V242 H415 V309 H483 L518 446Z", night)
        for x, y in ((199, 287), (253, 210), (368, 242), (435, 309)):
            s += path(f"M{x-14} {y} L{x+25} {y-83} L{x+64} {y}Z", accent)
            s += rect(x+18, y+27, 15, 36, light, 7)
        s += rect(326, 72, 9, 384, light)
        s += path("M343 91 Q433 112 493 94 L476 230 Q416 250 343 225Z", sky)
        s += path("M87 391 Q350 432 611 391", "none", f'stroke="{accent}" stroke-width="7"')
    elif slug == "sabaody":
        for x, top, r in ((150, 208, 100), (346, 112, 132), (550, 242, 96)):
            s += path(f"M{x-35} 520 Q{x-18} 365 {x-25} {top} H{x+25} Q{x+10} 360 {x+45} 520Z", light)
            s += circle(x, top, r, sky) + circle(x-29, top-25, r*.7, accent, 'opacity=".25"')
        for x, y, r in ((120, 103, 32), (264, 351, 46), (510, 94, 37), (587, 401, 53), (410, 440, 25)):
            s += circle(x, y, r, "none", f'stroke="{light}" stroke-width="3" opacity=".7"')
            s += path(f"M{x-r*.55} {y} Q{x-r*.6} {y-r*.6} {x} {y-r*.7}", "none", f'stroke="{light}" stroke-width="4"')
        s += path("M80 520 Q350 445 620 520Z", accent)
    elif slug == "amazon-lily":
        s += path("M66 517 L168 215 L238 332 L355 82 L453 315 L540 175 L638 517Z", sky)
        s += path("M230 510 L354 108 L434 510Z", light, 'opacity=".4"')
        s += rect(211, 360, 278, 148, light) + arches(240, 404, 4, 60, 35, 105, night)
        s += path("M174 364 Q350 322 526 364 L460 317 H243Z", accent)
        s += rect(288, 264, 124, 65, light) + path("M265 270 Q350 228 435 270 L389 232 H311Z", accent)
        for x in (159, 540):
            s += path(f"M{x} 518 Q{x-45} 466 {x} 427 Q{x+45} 390 {x} 352", "none", f'stroke="{accent}" stroke-width="16"')
    elif slug == "impel-down":
        s += rect(95, 254, 510, 267, sky, 35, 'opacity=".5"')
        s += rect(204, 222, 292, 300, light, 25) + rect(179, 204, 342, 36, accent)
        s += path("M214 204 V149 H253 V174 H292 V149 H331 V174 H370 V149 H409 V174 H448 V149 H487 V204Z", light)
        s += rect(270, 292, 160, 230, night, 70)
        for x in range(290, 430, 28):
            s += rect(x, 305, 7, 217, sky)
        s += rect(269, 378, 162, 8, sky) + rect(269, 449, 162, 8, sky)
        s += path("M60 267 Q350 235 640 267", "none", f'stroke="{light}" stroke-width="5" opacity=".5"')
        for x, y in ((114, 369), (553, 330), (582, 460)):
            s += circle(x, y, 9, light, 'opacity=".4"')
    elif slug == "marineford":
        s += path("M80 517 L129 355 H571 L620 517Z", light)
        s += rect(137, 331, 426, 27, accent) + rect(218, 227, 264, 106, light)
        s += path("M187 233 Q350 187 513 233 L459 189 H242Z", accent)
        s += rect(281, 133, 138, 65, light) + path("M253 140 Q350 98 447 140 L400 101 H300Z", accent)
        s += arches(154, 395, 7, 57, 28, 82, night)
        for x in (163, 525):
            s += rect(x, 203, 6, 130, light) + path(f"M{x+6} 204 H{x+68} L{x+48} 242 H{x+6}Z", accent)
        s += path("M94 515 Q144 445 192 515 T290 515 T390 515 T490 515 T590 515", "none", f'stroke="{sky}" stroke-width="19"')
    elif slug == "post-marineford":
        s += circle(350, 306, 167, "none", f'stroke="{light}" stroke-width="3" opacity=".45"')
        s += path("M350 100 L373 276 L555 306 L373 330 L350 511 L326 330 L146 306 L326 276Z", light, 'opacity=".4"')
        s += path("M350 140 L365 290 L350 306 L335 290Z", accent)
        s += path("M198 369 Q350 289 502 369 Q514 416 350 430 Q184 416 198 369Z", accent)
        s += path("M251 370 Q257 249 350 249 Q443 249 449 370 Q350 403 251 370Z", light)
        s += path("M257 335 Q350 366 443 335 L449 370 Q350 403 251 370Z", sky)
        s += path("M62 496 Q350 468 638 496", "none", f'stroke="{accent}" stroke-width="4"')
    elif slug in ("fishman-island", "fishman-special"):
        s += circle(350, 296, 220, "none", f'stroke="{light}" stroke-width="5" opacity=".65"')
        s += path("M140 465 Q350 361 560 465 L585 514 H114Z", sky)
        s += rect(211, 325, 278, 158, light, 8) + arches(237, 371, 5, 48, 27, 112, night)
        s += path("M195 330 Q350 279 505 330 L464 294 H236Z", accent)
        for x, y, w, h in ((260, 211, 65, 93), (343, 175, 84, 128)):
            s += rect(x, y, w, h, light)
            s += path(f"M{x-7} {y} Q{x+w/2} {y-92} {x+w+7} {y}Z", accent)
        for x, y in ((100, 408), (600, 389)):
            s += path(f"M{x} 526 V{y} M{x} {y+35} L{x-31} {y+8} M{x} {y+65} L{x+33} {y+32}", "none", f'stroke="{accent}" stroke-width="14" stroke-linecap="round"')
        for x, y, r in ((158, 188, 13), (565, 264, 20), (247, 86, 9)):
            s += circle(x, y, r, "none", f'stroke="{light}" stroke-width="2" opacity=".65"')
    elif slug == "punk-hazard":
        s += path("M47 516 L176 162 L324 516Z", accent)
        s += path("M110 342 L176 162 L240 344 L202 318 L174 356 L145 314Z", light, 'opacity=".4"')
        s += path("M376 516 L510 120 L657 516Z", light)
        s += path("M510 120 L488 370 L551 314 L580 516 H657Z", sky)
        s += path("M154 145 Q130 79 182 41 Q211 103 189 145Z", accent, 'opacity=".65"')
        s += rect(260, 365, 180, 152, night, 8) + rect(243, 349, 214, 29, accent)
        s += arches(288, 399, 3, 50, 24, 70, light) + rect(319, 298, 60, 53, light)
    elif slug == "dressrosa":
        s += path("M76 511 L134 378 L211 311 L291 357 L354 221 L446 335 L538 277 L624 511Z", sky)
        s += rect(424, 206, 95, 131, light) + rect(412, 195, 119, 20, accent)
        s += path("M408 197 L471 140 L535 197Z", accent) + rect(466, 107, 7, 44, light)
        s += path("M473 107 H521 L506 126 H473Z", accent)
        s += path("M114 401 Q350 308 586 401 V489 Q350 566 114 489Z", light)
        s += path("M115 404 Q350 470 585 404", "none", f'stroke="{accent}" stroke-width="16"')
        s += arches(146, 430, 8, 52, 26, 64, night)
        for x, y in ((95, 326), (596, 240)):
            for dx, dy in ((0, -13), (13, 0), (0, 13), (-13, 0)):
                s += circle(x+dx, y+dy, 13, accent)
            s += circle(x, y, 8, light)
    elif slug == "zou":
        s += path("M137 341 Q153 248 289 250 H413 Q487 248 528 317 L553 369 Q571 414 543 472 L520 510 L497 494 Q526 416 489 405 L463 521 H418 L401 420 H272 L253 521 H207 L195 414 L151 408Z", light)
        s += path("M166 280 Q335 195 483 280 L465 324 H180Z", sky)
        for x, y, r in ((218, 239, 49), (288, 201, 63), (367, 223, 58), (429, 252, 42)):
            s += rect(x-7, y, 14, 90, accent) + circle(x, y, r, accent)
        s += path("M443 333 Q471 294 502 333 L484 394 Q448 404 443 333Z", sky)
        s += circle(512, 353, 5, night)
        s += path("M494 385 Q518 416 540 391", "none", f'stroke="{accent}" stroke-width="10"')
    elif slug == "whole-cake":
        for x, y, w, h in ((158, 399, 384, 116), (208, 295, 284, 107), (267, 204, 166, 95)):
            s += rect(x, y, w, h, light, 14)
            s += path(f"M{x} {y+24} Q{x+w*.125} {y+62} {x+w*.25} {y+24} T{x+w*.5} {y+24} T{x+w*.75} {y+24} T{x+w} {y+24} V{y+9} Q{x+w} {y} {x+w-12} {y} H{x+12} Q{x} {y} {x} {y+9}Z", accent)
        s += arches(197, 452, 6, 54, 27, 63, sky)
        s += rect(332, 130, 36, 76, light, 9) + circle(350, 106, 27, accent)
        for x, y in ((125, 338), (574, 357)):
            s += rect(x-5, y, 10, 168, light) + circle(x, y, 43, accent)
            s += circle(x, y, 27, "none", f'stroke="{light}" stroke-width="5"')
    elif slug == "wano-acts-1-2":
        s += path("M48 518 L271 173 L495 518Z", sky)
        s += path("M215 260 L271 173 L330 264 L295 248 L271 271 L246 249Z", light)
        s += rect(355, 322, 166, 194, light) + arches(385, 432, 3, 42, 22, 84, night)
        for y, w in ((326, 226), (264, 179), (210, 131)):
            s += path(f"M{438-w/2} {y} Q438 {y-5} {438+w/2} {y} L{438+w/2-38} {y-32} H{438-w/2+38}Z", accent)
            s += rect(438-w/2+38, y-32, w-76, 50, light)
        s += path("M107 516 L120 331 M241 516 L228 331 M84 324 Q176 343 268 324 M109 366 H244", "none", f'stroke="{accent}" stroke-width="17"')
        for x, y in ((145, 155), (563, 328), (587, 189)):
            s += circle(x, y, 8, accent) + circle(x+11, y+8, 7, accent) + circle(x+3, y+17, 6, accent)
    elif slug == "wano-act-3":
        s += path("M81 522 L131 431 L204 400 L252 287 L350 242 L449 291 L497 405 L569 433 L624 522Z", sky)
        s += path("M253 332 Q131 241 175 108 Q189 240 291 264Z", light)
        s += path("M449 332 Q572 241 528 108 Q514 240 411 264Z", light)
        s += path("M154 523 L252 395 L347 354 L452 394 L548 523Z", night)
        s += rect(307, 394, 86, 126, light) + rect(330, 448, 39, 72, night, 18)
        s += path("M272 399 Q350 383 428 399 L389 365 H311Z", accent)
        for x, y in ((117, 347), (572, 363), (209, 236), (499, 219)):
            s += rect(x-7, y, 14, 23, accent, 5)
            s += path(f"M{x} {y-10} V{y}", "none", f'stroke="{light}" stroke-width="2"')
    return s + waves(light)


def svg(slug, title_lines, label, palette, background=False):
    night, sky, light, accent = ["#" + c for c in palette]
    w, h = (1920, 1080) if background else (768, 1152)
    defs = f'''<defs>
      <linearGradient id="sky" x2="0.65" y2="1"><stop stop-color="{sky}"/><stop offset="1" stop-color="{night}"/></linearGradient>
      <linearGradient id="shade"><stop stop-color="{night}"/><stop offset="1" stop-color="{night}" stop-opacity="0"/></linearGradient>
      <radialGradient id="glow"><stop stop-color="{accent}" stop-opacity=".24"/><stop offset="1" stop-color="{accent}" stop-opacity="0"/></radialGradient>
    </defs>'''
    s = f'<svg xmlns="http://www.w3.org/2000/svg" width="{w}" height="{h}" viewBox="0 0 {w} {h}" role="img"><title>{escape(" ".join(title_lines) + " — " + label)}</title>' + defs
    s += rect(0, 0, w, h, "url(#sky)")
    s += circle(w*.73, h*.35, h*.65, "url(#glow)")
    # Quiet, deterministic speckles, no borrowed textures.
    for i in range(38):
        x, y = (i * 113 + 49) % w, (i * 71 + 37) % int(h*.7)
        s += circle(x, y, 1.3 if i % 3 else 2, light, 'opacity=".18"')
    if background:
        s += f'<g transform="translate(950 20) scale(1.35)">{motif(slug, palette)}</g>'
        s += rect(0, 0, 1600, h, "url(#shade)")
        s += path("M0 969 Q500 886 940 974 T1920 981 V1080 H0Z", night, 'opacity=".35"')
    else:
        s += text(52, 84, "MUHN PACE", 24, light, 'letter-spacing="6" font-weight="bold"')
        s += rect(52, 112, 46, 4, accent)
        s += f'<g transform="translate(34 170)">{motif(slug, palette)}</g>'
        s += text(52, 873, label, 17, accent, 'letter-spacing="1.1" font-weight="bold"')
        for i, line in enumerate(title_lines):
            size = 57 if len(line) >= 11 else (65 if len(line) >= 9 else 76)
            s += text(48, 962 + 78*i, line, size, "#ffffff", 'font-weight="bold" letter-spacing="-1.5"')
        s += rect(52, 1081, 664, 1, light, extra='opacity=".25"')
        s += text(52, 1116, "ENGLISH DUB", 18, light, 'letter-spacing="2.2"')
    return s + "</svg>\n"


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    catalogue = []
    for collection_id, slug, lines, label, palette in ARCS:
        entry = {"collection_id": collection_id, "slug": slug, "title": " ".join(lines), "label": label}
        for background in (False, True):
            kind = "background" if background else "poster"
            source = svg(slug, lines, label, palette, background)
            (OUT / f"{slug}-{kind}.svg").write_text(source, encoding="utf-8")
            filename = f"{slug}-{kind}.png"
            cairosvg.svg2png(bytestring=source.encode(), write_to=str(OUT / filename))
            data = (OUT / filename).read_bytes()
            entry[kind] = {
                "file": filename,
                "width": 1920 if background else 768,
                "height": 1080 if background else 1152,
                "bytes": len(data),
                "sha256": hashlib.sha256(data).hexdigest(),
            }
        catalogue.append(entry)
    (OUT / "catalogue.json").write_text(json.dumps({"source": "Original geometric artwork for this repository", "collections": catalogue}, indent=2) + "\n")
    print(f"Rendered {len(ARCS)} original poster/background pairs in {OUT}")


if __name__ == "__main__":
    main()
