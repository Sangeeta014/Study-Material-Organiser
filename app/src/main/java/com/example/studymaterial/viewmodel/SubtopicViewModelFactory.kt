package com.example.studymaterial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studymaterial.repository.LocalRepository

class SubtopicViewModelFactory(
    private val localRepository: LocalRepository,
    private val topicId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubtopicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubtopicViewModel(localRepository, topicId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


