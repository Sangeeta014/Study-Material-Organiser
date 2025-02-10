package com.example.studymaterial.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "subtopics",
    foreignKeys = [
        ForeignKey(
            entity = Topic::class,
            parentColumns = arrayOf("topicId"),
            childColumns = arrayOf("topicId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Subtopic(
    @PrimaryKey(autoGenerate = true)
    val subtopicId : Int = 0,
    val topicId : Int,
    val subtopicName : String,
    var isRead: Boolean = false
)