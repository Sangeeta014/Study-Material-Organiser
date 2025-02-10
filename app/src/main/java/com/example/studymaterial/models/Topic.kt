package com.example.studymaterial.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey(autoGenerate = true)
    val topicId : Int = 0,
    val topicName : String
)
