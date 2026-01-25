package com.example.medalgorithms.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medalgorithms.data.TemplateRepository
import com.example.medalgorithms.viewmodel.TemplateEditViewModel
import com.example.medalgorithms.viewmodel.TemplateEditViewModelFactory

@Composable
fun TemplateEditScreen(
    repository: TemplateRepository,
    templateId: Long,
    presetCategory: String,
    onBack: () -> Unit
) {
    val vm: TemplateEditViewModel = viewModel(factory = TemplateEditViewModelFactory(repository, templateId, presetCategory))
    val state by vm.state.collectAsState()

    val clipboard = LocalClipboardManager.current

    Scaffold(
        topBar = { TopBar("Редактирование", onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = state.category,
                onValueChange = { vm.setCategory(it) },
                label = { Text("Категория") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = state.title,
                onValueChange = { vm.setTitle(it) },
                label = { Text("Название") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = state.content,
                onValueChange = { vm.setContent(it) },
                label = { Text("Текст шаблона") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 8
            )
            Spacer(Modifier.height(12.dp))

            if (state.error.isNotBlank()) {
                Text(state.error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        vm.save(onSaved = onBack)
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Сохранить") }

                OutlinedButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(state.content))
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Копировать") }
            }
        }
    }
}
