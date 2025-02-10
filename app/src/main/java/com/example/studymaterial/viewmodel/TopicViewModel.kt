package com.example.studymaterial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymaterial.api.NetworkResponse
import com.example.studymaterial.models.ImageItem
import com.example.studymaterial.models.Topic
import com.example.studymaterial.repository.LocalRepository
import com.example.studymaterial.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TopicViewModel(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    //For Image
    private val _imageResult = MutableLiveData<List<ImageItem>>()
    val imageResult : LiveData<List<ImageItem>> = _imageResult

    init {
        getImages()
    }
    private fun getImages(){
        viewModelScope.launch {
            remoteRepository.getImages().collect { response ->

                if (response is NetworkResponse.Success) {
                    _imageResult.value = response.data
                }
            }
        }
    }


    //For TopicList
    val allTopics: Flow<List<Topic>> = localRepository.getAllTopics()

    fun insertTopic(topic : Topic) = viewModelScope.launch {
        localRepository.insertTopic(topic)
    }

    fun getSubtopicCount(topicId : Int) : Flow<Int>{
        return localRepository.getSubtopicCount(topicId)
    }

    fun getUnreadSubtopicCount(topicId : Int) : Flow<Int>{
        return localRepository.getUnreadSubtopicCount(topicId)
    }

}
