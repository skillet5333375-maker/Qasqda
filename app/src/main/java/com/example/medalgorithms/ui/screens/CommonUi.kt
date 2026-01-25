// File: app/src/main/java/com/example/medalgorithms/ui/screens/CommonUi.kt
package com.example.medalgorithms.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) { Text("←") }
            }
        },
        modifier = modifier
    )
}

