package com.example.studymaterial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymaterial.models.Resource
import com.example.studymaterial.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ResourceViewModel(
    private val localRepository: LocalRepository,
    val subtopicId: Int
) : ViewModel(){
    val allResource: Flow<List<Resource>> = localRepository.getResourcesForSubtopic(subtopicId)

    fun insertResource(resource: Resource) = viewModelScope.launch {
        localRepository.insertResource(resource)
    }
    fun updateResource(resource: Resource) = viewModelScope.launch {
        localRepository.updateResource(resource)
    }
    fun deleteResource(resource: Resource) = viewModelScope.launch {
        localRepository.deleteResource(resource)
    }
}
