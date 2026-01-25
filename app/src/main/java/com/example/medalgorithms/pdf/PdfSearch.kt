// File: app/src/main/java/com/example/medalgorithms/pdf/PdfSearch.kt
package com.example.medalgorithms.pdf

import android.content.Context
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class PdfHit(
    val pageIndex: Int,
    val snippet: String,
    val highlightStart: Int,
    val highlightLength: Int,
)

/**
 * Full-text search in a PDF stored in /assets.
 * Returns a list of hits (0-based pageIndex). Each hit corresponds to a single match.
 */
suspend fun searchInPdfAssets(
    context: Context,
    assetName: String,
    query: String,
    snippetRadius: Int = 60,
    maxHitsPerPage: Int = 50,
    maxTotalHits: Int = 500,
): List<PdfHit> {
    val q = query.trim()
    if (q.isEmpty()) return emptyList()

    fun normalize(s: String): String =
        s.replace("\n", " ").replace(Regex("\\s+"), " ").trim()

    return withContext(Dispatchers.Default) {
        val hits = mutableListOf<PdfHit>()

        context.assets.open(assetName).use { input ->
            val doc = PDDocument.load(input)
            try {
                val stripper = PDFTextStripper()
                val pages = doc.numberOfPages

                for (p in 1..pages) {
                    if (hits.size >= maxTotalHits) break

                    stripper.startPage = p
                    stripper.endPage = p
                    val text = stripper.getText(doc)

                    var fromIndex = 0
                    var pageHits = 0

                    while (pageHits < maxHitsPerPage && hits.size < maxTotalHits) {
                        val idx = text.indexOf(q, startIndex = fromIndex, ignoreCase = true)
                        if (idx < 0) break

                        val start = (idx - snippetRadius).coerceAtLeast(0)
                        val end = (idx + q.length + snippetRadius).coerceAtMost(text.length)

                        val snippet = normalize(text.substring(start, end))

                        val hs = snippet.indexOf(q, ignoreCase = true).coerceAtLeast(0)
                        val hl = if (hs >= 0) q.length.coerceAtMost(snippet.length - hs) else 0

                        hits += PdfHit(
                            pageIndex = p - 1,
                            snippet = snippet,
                            highlightStart = hs,
                            highlightLength = hl,
                        )

                        pageHits++
                        fromIndex = idx + q.length
                    }
                }
            } finally {
                doc.close()
            }
        }

        hits
    }
}

