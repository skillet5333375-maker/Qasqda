package com.example.medalgorithms.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.example.medalgorithms.data.TemplateRepository
import com.example.medalgorithms.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController, repository: TemplateRepository) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenAlgorithms = { navController.navigate(Routes.ALGORITHMS) },
                onOpenTemplates = { navController.navigate(Routes.CATEGORIES) }
            )
        }
        composable(Routes.ALGORITHMS) {
            AlgorithmsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CATEGORIES) {
            TemplatesCategoriesScreen(
                onBack = { navController.popBackStack() },
                onOpenCategory = { category ->
                    navController.navigate("${Routes.TEMPLATES_LIST}?category=${encode(category)}")
                }
            )
        }
        composable(
            route = "${Routes.TEMPLATES_LIST}?category={category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category").orEmpty()
            TemplatesListScreen(
                repository = repository,
                category = decode(category),
                onBack = { navController.popBackStack() },
                onCreate = { navController.navigate("${Routes.TEMPLATE_EDIT}?category=${encode(decode(category))}") },
                onOpenTemplate = { id -> navController.navigate("${Routes.TEMPLATE_EDIT}?id=$id") }
            )
        }
        composable(
            route = "${Routes.TEMPLATE_EDIT}?id={id}&category={category}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType; defaultValue = 0L },
                navArgument("category") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val category = decode(backStackEntry.arguments?.getString("category").orEmpty())
            TemplateEditScreen(
                repository = repository,
                templateId = id,
                presetCategory = category,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun encode(value: String): String = java.net.URLEncoder.encode(value, "UTF-8")
private fun decode(value: String): String = java.net.URLDecoder.decode(value, "UTF-8")
