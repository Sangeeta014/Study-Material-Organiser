package com.example.studymaterial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymaterial.models.Subtopic
import com.example.studymaterial.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SubtopicViewModel(
    private val localRepository: LocalRepository,
    val topicId: Int
) : ViewModel() {

    val allSubtopic: Flow<List<Subtopic>> = localRepository.getSubtopicsForTopic(topicId)

    fun insertSubtopic(subtopic: Subtopic) = viewModelScope.launch {
        localRepository.insertSubtopic(subtopic)
    }
    fun updateSubtopic(subtopic: Subtopic) = viewModelScope.launch {
        localRepository.updateSubtopic(subtopic)
    }
    fun deleteSubtopic(subtopic: Subtopic) = viewModelScope.launch {
        localRepository.deleteSubtopic(subtopic)
    }
    fun markSubtopicAsRead(subtopicId : Int) = viewModelScope.launch{
        localRepository.markSubtopicsAsRead(subtopicId)
    }
}