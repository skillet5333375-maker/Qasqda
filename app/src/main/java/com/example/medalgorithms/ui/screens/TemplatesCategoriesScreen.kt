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

@Composable
fun TemplatesCategoriesScreen(
    onBack: () -> Unit,
    onOpenCategory: (String) -> Unit
) {
    Scaffold(
        topBar = { TopBar("Шаблоны", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp)
        ) {
            items(TEMPLATE_CATEGORIES) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onOpenCategory(category) }
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
