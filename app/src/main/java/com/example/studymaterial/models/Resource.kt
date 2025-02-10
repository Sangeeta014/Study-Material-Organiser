package com.example.studymaterial.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "resources",
    foreignKeys = [
        ForeignKey(
            entity = Subtopic::class,
            parentColumns = arrayOf("subtopicId"),
            childColumns = arrayOf("subtopicId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class Resource(
    @PrimaryKey(autoGenerate = true)
    val resourceId : Int = 0,
    val subtopicId: Int,
    val resourceName : String,
    val resourceUrl: String,
)
