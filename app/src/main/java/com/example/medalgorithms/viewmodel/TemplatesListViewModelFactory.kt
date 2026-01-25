package com.example.medalgorithms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medalgorithms.data.TemplateRepository

class TemplatesListViewModelFactory(
    private val repository: TemplateRepository,
    private val category: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TemplatesListViewModel(repository, category) as T
    }
}
