// File: app/src/main/java/com/example/medalgorithms/ui/screens/TemplatesCategoriesScreen.kt
// ВАЖНО: если твой файл сильно отличается — просто применяй правки по сути:
// 1) импорт TEMPLATE_CATEGORIES
// 2) category.id как String
package com.example.medalgorithms.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medalgorithms.TEMPLATE_CATEGORIES
import com.example.medalgorithms.TemplateCategory

@Composable
fun TemplatesCategoriesScreen(
    onBack: () -> Unit,
    onOpenCategory: (categoryId: String) -> Unit
) {
    Scaffold(
        topBar = { TopBar("Категории", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(TEMPLATE_CATEGORIES) { category: TemplateCategory ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenCategory(category.id) }
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(category.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text("ID: ${category.id}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
