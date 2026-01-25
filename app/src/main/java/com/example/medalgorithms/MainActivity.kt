package com.example.medalgorithms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.medalgorithms.data.TemplateRepository
import com.example.medalgorithms.ui.AppNavGraph
import com.example.medalgorithms.ui.theme.MedAlgorithmsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as App
        val repo = TemplateRepository(app.db.templateDao())

        setContent {
            MedAlgorithmsTheme {
                val navController = rememberNavController()
                // repo is stable; remember it
                val repository = remember { repo }
                AppNavGraph(navController = navController, repository = repository)
            }
        }
    }
}
