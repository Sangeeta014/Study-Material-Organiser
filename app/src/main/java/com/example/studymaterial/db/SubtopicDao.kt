package com.example.studymaterial.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studymaterial.models.Subtopic
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtopicDao {

    @Query("SELECT * FROM subtopics WHERE topicId = :topicId")
    fun getSubtopicForTopic(topicId : Int) : Flow<List<Subtopic>>

    @Insert
    suspend fun insertSubtopic(subtopic: Subtopic)

    @Update
    suspend fun updateSubtopic(subtopic: Subtopic)

    @Delete
    suspend fun deleteSubtopic(subtopic: Subtopic)

    //Count
    @Query("SELECT COUNT(*) FROM subtopics WHERE topicId = :topicId")
    fun getSubtopicCountForTopic(topicId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM subtopics WHERE topicId = :topicId AND isRead = 0")
    fun getUnreadSubtopicCountForTopic(topicId: Int): Flow<Int>

    @Query("UPDATE subtopics SET isRead = 1 WHERE subtopicId = :subtopicId")
    suspend fun markSubtopicAsRead(subtopicId: Int)


}
