// File: app/src/main/java/com/example/medalgorithms/ui/Routes.kt
package com.example.medalgorithms.ui

/**
 * Все маршруты приложения держим тут.
 * Если у тебя уже есть другие экраны — добавляй сюда новые routes.
 */
sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Algorithms : Routes("algorithms")
}

