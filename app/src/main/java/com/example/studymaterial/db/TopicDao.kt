package com.example.studymaterial.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.studymaterial.models.Topic
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics")
    fun getAllTopics(): Flow<List<Topic>> // Use Flow to observe changes in the database

    @Insert
    suspend fun insertTopic(topic : Topic)

    @Delete
    suspend fun deleteTopic(topic : Topic)
}