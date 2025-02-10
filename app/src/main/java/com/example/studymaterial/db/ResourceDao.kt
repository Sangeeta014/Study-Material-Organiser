package com.example.studymaterial.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studymaterial.models.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {

    @Query("SELECT * FROM resources WHERE subtopicId = :subtopicId")
    fun getResourceForSubtopic(subtopicId : Int) : Flow<List<Resource>>

    @Insert
    suspend fun insertResource(resource: Resource)

    @Update
    suspend fun updateResource(resource: Resource)

    @Delete
    suspend fun deleteResource(resource: Resource)

}