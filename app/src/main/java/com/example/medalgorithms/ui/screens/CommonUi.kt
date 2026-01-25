@file:OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
package com.example.medalgorithms.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopBar(title: String, onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Text("←")
                }
            }
        }
    )
}
