package com.example.medalgorithms.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medalgorithms.data.TemplateEntity
import com.example.medalgorithms.data.TemplateRepository
import com.example.medalgorithms.viewmodel.TemplatesListViewModel
import com.example.medalgorithms.viewmodel.TemplatesListViewModelFactory

@Composable
fun TemplatesListScreen(
    repository: TemplateRepository,
    category: String,
    onBack: () -> Unit,
    onCreate: () -> Unit,
    onOpenTemplate: (Long) -> Unit
) {
    val vm: TemplatesListViewModel = viewModel(factory = TemplatesListViewModelFactory(repository, category))
    val items by vm.items.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopBar(category, onBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Text("+")
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Нет шаблонов. Нажмите + чтобы создать.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                items(items) { t ->
                    TemplateRow(
                        item = t,
                        onOpen = { onOpenTemplate(t.id) },
                        onDelete = { vm.delete(t) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TemplateRow(item: TemplateEntity, onOpen: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = item.title.ifBlank { "(Без названия)" },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { onOpen() }
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.content.take(120) + if (item.content.length > 120) "…" else "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onOpen() }
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onOpen) { Text("Открыть") }
                OutlinedButton(onClick = onDelete) { Text("Удалить") }
            }
        }
    }
}
