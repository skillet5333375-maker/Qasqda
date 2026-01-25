package com.example.medalgorithms.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenAlgorithms: () -> Unit,
    onOpenTemplates: () -> Unit
) {
    Scaffold(
        topBar = { TopBar("Главный экран") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onOpenAlgorithms,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Алгоритмы") }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onOpenTemplates,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Шаблоны") }
        }
    }
}
