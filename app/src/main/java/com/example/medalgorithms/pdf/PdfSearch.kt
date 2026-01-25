package com.example.medalgorithms.pdf

import android.content.Context
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class PdfHit(val pageIndex: Int, val snippet: String)

suspend fun searchInPdfAssets(context: Context, assetName: String, query: String): List<PdfHit> {
    if (query.isBlank()) return emptyList()

    return withContext(Dispatchers.Default) {
        val hits = mutableListOf<PdfHit>()

        context.assets.open(assetName).use { input ->
            val doc = PDDocument.load(input)
            try {
                val stripper = PDFTextStripper()
                val pages = doc.numberOfPages

                for (p in 1..pages) {
                    stripper.startPage = p
                    stripper.endPage = p
                    val text = stripper.getText(doc)
                    val idx = text.indexOf(query, ignoreCase = true)
                    if (idx >= 0) {
                        val start = maxOf(0, idx - 50)
                        val end = minOf(text.length, idx + query.length + 50)
                        val snippet = text.substring(start, end)
                            .replace("\n", " ")
                            .replace(Regex("\\s+"), " ")
                            .trim()
                        hits += PdfHit(pageIndex = p - 1, snippet = snippet)
                    }
                }
            } finally {
                doc.close()
            }
        }

        hits
    }
}
