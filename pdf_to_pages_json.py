# File: tools/pdf_to_pages_json.py
"""
PDF -> algorithms_pages.json (page-by-page text) for offline search / Room FTS import.

Usage:
  1) pip install pypdf
  2) python tools/pdf_to_pages_json.py \
       --pdf app/src/main/assets/algorithms.pdf \
       --out app/src/main/assets/algorithms_pages.json

Notes:
- Works best for TEXT PDFs (not scanned images).
- Output pageIndex is 0-based (0 -> page 1 in viewer).
- Cleans hyphenated line breaks and normalizes whitespace.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from dataclasses import dataclass
from pathlib import Path
from typing import List


@dataclass(frozen=True)
class PageRecord:
    pageIndex: int
    text: str


def _clean_text(text: str) -> str:
    if not text:
        return ""
    text = text.replace("\u00ad", "")  # soft hyphen
    # join hyphenated line breaks: "лек-\nарство" -> "лекарство"
    text = re.sub(r"(\w)-\s*\n\s*(\w)", r"\1\2", text)
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    text = re.sub(r"[ \t]+", " ", text)
    text = re.sub(r"\n{3,}", "\n\n", text)
    # trim spaces around newlines
    text = re.sub(r"[ \t]+\n", "\n", text)
    text = re.sub(r"\n[ \t]+", "\n", text)
    return text.strip()


def _extract_pages_pypdf(pdf_path: Path) -> List[str]:
    try:
        from pypdf import PdfReader  # type: ignore
    except Exception:
        raise SystemExit(
            "Не найдена библиотека pypdf.\n"
            "Установи так: pip install pypdf\n"
        )

    reader = PdfReader(str(pdf_path))
    pages: List[str] = []
    for page in reader.pages:
        pages.append(page.extract_text() or "")
    return pages


def build_records(pdf_path: Path, min_chars: int, max_pages: int) -> List[PageRecord]:
    raw_pages = _extract_pages_pypdf(pdf_path)
    records: List[PageRecord] = []

    for i, raw in enumerate(raw_pages[:max_pages]):
        cleaned = _clean_text(raw)
        if len(cleaned) < min_chars:
            cleaned = ""
        records.append(PageRecord(pageIndex=i, text=cleaned))

    return records


def write_json(records: List[PageRecord], out_path: Path) -> None:
    out_path.parent.mkdir(parents=True, exist_ok=True)
    payload = [{"pageIndex": r.pageIndex, "text": r.text} for r in records]
    out_path.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")


def print_stats(records: List[PageRecord], min_chars: int) -> None:
    total = len(records)
    empty = sum(1 for r in records if not r.text)
    shortish = sum(1 for r in records if 0 < len(r.text) < (min_chars * 2))
    max_len = max((len(r.text) for r in records), default=0)

    print(f"Pages: {total}")
    print(f"Empty pages: {empty}")
    print(f"Very short pages (< {min_chars*2} chars): {shortish}")
    print(f"Max page text length: {max_len}")

    # show a few sample pages with text
    examples = [r for r in records if r.text][:3]
    if examples:
        print("\nSample extracted text (first 200 chars):")
        for r in examples:
            preview = (r.text[:200] + "…") if len(r.text) > 200 else r.text
            print(f"- page {r.pageIndex+1}: {preview.replace('\\n', ' ')}")


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--pdf", required=True, help="Path to PDF (e.g. app/src/main/assets/algorithms.pdf)")
    ap.add_argument("--out", required=True, help="Output JSON (e.g. app/src/main/assets/algorithms_pages.json)")
    ap.add_argument("--min-chars", type=int, default=20, help="Treat pages with less text as empty")
    ap.add_argument("--max-pages", type=int, default=10_000, help="Safety limit")
    ap.add_argument("--stats", action="store_true", help="Print stats after generation")
    args = ap.parse_args()

    pdf_path = Path(args.pdf).resolve()
    out_path = Path(args.out).resolve()

    if not pdf_path.exists():
        raise SystemExit(f"PDF не найден: {pdf_path}")

    records = build_records(pdf_path, min_chars=args.min_chars, max_pages=args.max_pages)
    write_json(records, out_path)

    print(f"OK: wrote {len(records)} pages -> {out_path}")
    if args.stats:
        print_stats(records, min_chars=args.min_chars)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        sys.exit(130)
