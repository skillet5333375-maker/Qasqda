package com.example.medalgorithms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medalgorithms.data.TemplateEntity
import com.example.medalgorithms.data.TemplateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TemplatesListViewModel(
    private val repository: TemplateRepository,
    private val category: String
) : ViewModel() {

    val items: StateFlow<List<TemplateEntity>> = repository
        .observeByCategory(category)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun delete(item: TemplateEntity) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}
