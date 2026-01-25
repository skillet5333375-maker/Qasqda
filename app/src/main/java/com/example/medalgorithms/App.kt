// File: app/src/main/java/com/example/medalgorithms/App.kt
package com.example.medalgorithms

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.medalgorithms.ui.AppNavGraph

@Composable
fun App() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}
