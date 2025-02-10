package com.example.studymaterial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studymaterial.repository.LocalRepository

class ResourceViewModelFactory(
    private val localRepository: LocalRepository,
    private val subtopicId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResourceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResourceViewModel(localRepository, subtopicId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
