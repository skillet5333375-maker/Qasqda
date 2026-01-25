// File: app/src/main/java/com/example/medalgorithms/ui/AppNavGraph.kt
package com.example.medalgorithms.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.medalgorithms.ui.screens.AlgorithmsScreen
import com.example.medalgorithms.ui.screens.HomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                onOpenAlgorithms = { navController.navigate(Routes.Algorithms.route) }
            )
        }

        composable(Routes.Algorithms.route) {
            AlgorithmsScreen(onBack = { navController.popBackStack() })
        }
    }
}
