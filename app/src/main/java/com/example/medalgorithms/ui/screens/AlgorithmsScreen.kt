package com.example.medalgorithms.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.medalgorithms.ALGORITHMS_PDF_ASSET
import com.example.medalgorithms.pdf.PdfHit
import com.example.medalgorithms.pdf.searchInPdfAssets
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AlgorithmsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var hits by remember { mutableStateOf<List<PdfHit>>(emptyList()) }
    var searching by remember { mutableStateOf(false) }
    var jumpToPage by remember { mutableStateOf<Int?>(null) }

    var pdfViewRef by remember { mutableStateOf<PDFView?>(null) }

    Scaffold(
        topBar = { TopBar("Алгоритмы", onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Поиск по PDF") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        searching = true
                        hits = emptyList()
                        scope.launch {
                            try {
                                hits = searchInPdfAssets(context, ALGORITHMS_PDF_ASSET, query)
                            } finally {
                                searching = false
                            }
                        }
                    },
                    enabled = query.isNotBlank() && !searching
                ) {
                    Text("Найти")
                }
            }

            if (searching) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (hits.isNotEmpty()) {
                Text(
                    text = "Найдено: ${hits.size}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    items(hits) { hit ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    jumpToPage = hit.pageIndex
                                    // if pdf already loaded, jump immediately
                                    pdfViewRef?.jumpTo(hit.pageIndex, true)
                                }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text("Страница: ${hit.pageIndex + 1}", style = MaterialTheme.typography.labelMedium)
                                Spacer(Modifier.height(6.dp))
                                Text(hit.snippet)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            // PDF viewer
            AndroidView(
                factory = { ctx ->
                    PDFView(ctx, null).apply {
                        fromAsset(ALGORITHMS_PDF_ASSET)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .defaultPage(0)
                            .onLoad {
                                pdfViewRef = this
                                // apply pending jump
                                jumpToPage?.let { this.jumpTo(it, true) }
                            }
                            .load()
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { view ->
                    // keep reference
                    pdfViewRef = view
                    jumpToPage?.let { view.jumpTo(it, true) }
                }
            )
        }
    }
}
