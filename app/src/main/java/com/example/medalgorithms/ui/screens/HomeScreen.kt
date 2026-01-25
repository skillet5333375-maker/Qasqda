// File: app/src/main/java/com/example/medalgorithms/ui/screens/HomeScreen.kt
package com.example.medalgorithms.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenAlgorithms: () -> Unit
) {
    Scaffold(
        topBar = { TopBar("Главная") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Разделы",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = onOpenAlgorithms,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Алгоритмы")
            }
        }
    }
}
