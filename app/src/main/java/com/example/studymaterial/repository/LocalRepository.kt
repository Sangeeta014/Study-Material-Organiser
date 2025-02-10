package com.example.studymaterial.repository

import com.example.studymaterial.db.ResourceDao
import com.example.studymaterial.db.SubtopicDao
import com.example.studymaterial.db.TopicDao
import com.example.studymaterial.models.Resource
import com.example.studymaterial.models.Subtopic
import com.example.studymaterial.models.Topic
import kotlinx.coroutines.flow.Flow

class LocalRepository(
    private val topicDao: TopicDao,
    private val subtopicDao: SubtopicDao,
    private val resourceDao: ResourceDao
) {
    // Topic methods
    fun getAllTopics(): Flow<List<Topic>> = topicDao.getAllTopics()
    suspend fun insertTopic(topic: Topic) = topicDao.insertTopic(topic)
    suspend fun deleteTopic(topic: Topic) = topicDao.deleteTopic(topic)

    // Subtopic methods
    fun getSubtopicsForTopic(topicId : Int): Flow<List<Subtopic>> = subtopicDao.getSubtopicForTopic(topicId)
    suspend fun insertSubtopic(subtopic: Subtopic) = subtopicDao.insertSubtopic(subtopic)
    suspend fun updateSubtopic(subtopic: Subtopic) = subtopicDao.updateSubtopic(subtopic)
    suspend fun deleteSubtopic(subtopic: Subtopic) = subtopicDao.deleteSubtopic(subtopic)

    fun getSubtopicCount(topicId: Int): Flow<Int> {
        return subtopicDao.getSubtopicCountForTopic(topicId)
    }

    fun getUnreadSubtopicCount(topicId: Int): Flow<Int> {
        return subtopicDao.getUnreadSubtopicCountForTopic(topicId)
    }

    suspend fun markSubtopicsAsRead(subtopicId: Int) {
        subtopicDao.markSubtopicAsRead(subtopicId )
    }

    // Resource methods
    fun getResourcesForSubtopic(subtopicId: Int): Flow<List<Resource>> = resourceDao.getResourceForSubtopic(subtopicId)
    suspend fun insertResource(resource: Resource) = resourceDao.insertResource(resource)
    suspend fun updateResource(resource: Resource) = resourceDao.updateResource(resource)
    suspend fun deleteResource(resource: Resource) = resourceDao.deleteResource(resource)
}