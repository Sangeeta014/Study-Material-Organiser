package com.example.studymaterial.models

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.studymaterial.R
import com.example.studymaterial.db.StudyMaterialDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prePopulateTopics(context)
            prePopulateSubtopics(context)
            prePopulateResource(context)
        }
    }
}

suspend fun prePopulateTopics(context: Context) {
    try {
        val topicDao = StudyMaterialDatabase.getDatabase(context).topicDao()

        val topicList: JSONArray =
            context.resources.openRawResource(R.raw.topics).bufferedReader().use {
                JSONArray(it.readText())
            }

        topicList.takeIf { it.length() > 0 }?.let { list ->
            for (index in 0 until list.length()) {
                val topicsObj = list.getJSONObject(index)
                topicDao.insertTopic(
                    Topic(
                        topicsObj.getInt("topicId"),
                        topicsObj.getString("topicName"),
                    )
                )

            }
            Log.e("Topics Init Data", "successfully pre-populated topics into database")
        }
    } catch (exception: Exception) {
        Log.e(
            "User App",
            exception.localizedMessage ?: "failed to pre-populate topics into database"
        )
    }
}

suspend fun prePopulateSubtopics(context: Context) {
    try {
        val subtopicDao = StudyMaterialDatabase.getDatabase(context).subtopicDao()

        val topicList: JSONArray =
            context.resources.openRawResource(R.raw.subtopics).bufferedReader().use {
                JSONArray(it.readText())
            }

        topicList.takeIf { it.length() > 0 }?.let { list ->
            for (index in 0 until list.length()) {
                val subtopicObj = list.getJSONObject(index)
                subtopicDao.insertSubtopic(
                    Subtopic(
                        subtopicObj.getInt("subtopicId"),
                        subtopicObj.getInt("topicId"),
                        subtopicObj.getString("subtopicName"),
                        subtopicObj.getBoolean("isRead")
                    )
                )

            }
            Log.e("Subtopics Init Data", "successfully pre-populated subtopics into database")
        }
    } catch (exception: Exception) {
        Log.e(
            "Subtopics Init Data",
            exception.localizedMessage ?: "failed to pre-populate subtopics into database"
        )
    }
}

suspend fun prePopulateResource(context: Context) {
    try {
        val resourceDao = StudyMaterialDatabase.getDatabase(context).resourceDao()

        val resourceList: JSONArray =
            context.resources.openRawResource(R.raw.resources).bufferedReader().use {
                JSONArray(it.readText())
            }

        resourceList.takeIf { it.length() > 0 }?.let { list ->
            for (index in 0 until list.length()) {
                val resourceObj = list.getJSONObject(index)
                resourceDao.insertResource(
                    Resource(
                        resourceObj.getInt("resourceId"),
                        resourceObj.getInt("subtopicId"),
                        resourceObj.getString("resourceName"),
                        resourceObj.getString("resourceUrl")
                    )
                )

            }
            Log.e("Resources Init Data", "successfully pre-populated resources into database")
        }
    } catch (exception: Exception) {
        Log.e(
            "Resources Init Data",
            exception.localizedMessage ?: "failed to pre-populate resources into database"
        )
    }
}