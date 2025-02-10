package com.example.studymaterial.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studymaterial.models.PrepopulateRoomCallback
import com.example.studymaterial.models.Resource
import com.example.studymaterial.models.Subtopic
import com.example.studymaterial.models.Topic

@Database(
    entities = [Topic::class, Subtopic::class, Resource::class],
    version = 2,
    exportSchema = false
)
abstract class StudyMaterialDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao
    abstract fun subtopicDao(): SubtopicDao
    abstract fun resourceDao(): ResourceDao


    companion object {

        @Volatile
        private var INSTANCE: StudyMaterialDatabase? = null

        fun getDatabase(context: Context): StudyMaterialDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyMaterialDatabase::class.java,
                    "study_material_database"
                )
                    .addCallback(PrepopulateRoomCallback(context))

                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}