package com.example.medalgorithms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medalgorithms.data.TemplateEntity
import com.example.medalgorithms.data.TemplateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TemplateEditState(
    val id: Long = 0,
    val category: String = "",
    val title: String = "",
    val content: String = "",
    val error: String = ""
)

class TemplateEditViewModel(
    private val repository: TemplateRepository,
    private val templateId: Long,
    presetCategory: String
) : ViewModel() {

    private val _state = MutableStateFlow(TemplateEditState(id = templateId, category = presetCategory))
    val state: StateFlow<TemplateEditState> = _state.asStateFlow()

    init {
        if (templateId != 0L) {
            viewModelScope.launch {
                val existing = repository.getById(templateId)
                if (existing != null) {
                    _state.value = TemplateEditState(
                        id = existing.id,
                        category = existing.category,
                        title = existing.title,
                        content = existing.content,
                        error = ""
                    )
                }
            }
        }
    }

    fun setCategory(v: String) { _state.value = _state.value.copy(category = v, error = "") }
    fun setTitle(v: String) { _state.value = _state.value.copy(title = v, error = "") }
    fun setContent(v: String) { _state.value = _state.value.copy(content = v, error = "") }

    fun save(onSaved: () -> Unit) {
        val s = _state.value
        if (s.category.isBlank()) {
            _state.value = s.copy(error = "Категория не должна быть пустой")
            return
        }

        viewModelScope.launch {
            val entity = TemplateEntity(
                id = s.id,
                category = s.category.trim(),
                title = s.title.trim(),
                content = s.content,
                updatedAt = System.currentTimeMillis()
            )
            repository.upsert(entity)
            onSaved()
        }
    }
}
