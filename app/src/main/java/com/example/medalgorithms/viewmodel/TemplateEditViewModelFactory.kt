package com.example.medalgorithms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medalgorithms.data.TemplateRepository

class TemplateEditViewModelFactory(
    private val repository: TemplateRepository,
    private val templateId: Long,
    private val presetCategory: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TemplateEditViewModel(repository, templateId, presetCategory) as T
    }
}
