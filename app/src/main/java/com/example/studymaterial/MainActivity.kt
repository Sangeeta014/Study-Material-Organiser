package com.example.studymaterial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studymaterial.db.StudyMaterialDatabase
import com.example.studymaterial.repository.LocalRepository
import com.example.studymaterial.repository.RemoteRepository
import com.example.studymaterial.screens.ResourceScreen
import com.example.studymaterial.screens.SubtopicScreen
import com.example.studymaterial.screens.TopicScreen
import com.example.studymaterial.viewmodel.ResourceViewModel
import com.example.studymaterial.viewmodel.ResourceViewModelFactory
import com.example.studymaterial.viewmodel.SubtopicViewModel
import com.example.studymaterial.viewmodel.SubtopicViewModelFactory
import com.example.studymaterial.viewmodel.TopicViewModel
import com.example.studymaterial.viewmodel.TopicViewModelFactory


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StudyMaterialApp()
        }
    }
}

@Composable
fun StudyMaterialApp() {
    val navController = rememberNavController()
    val db = StudyMaterialDatabase.getDatabase(LocalContext.current)
    val repository = LocalRepository(db.topicDao(), db.subtopicDao(), db.resourceDao())
    val remoteRepository = RemoteRepository()

    val topicViewModel: TopicViewModel = viewModel(factory = TopicViewModelFactory(repository, remoteRepository))

    NavHost(navController = navController, startDestination = "topicList") {
        composable("topicList") {
            TopicScreen(navController = navController, topicViewModel = topicViewModel)
        }
        composable("subtopicList/{topicId}") { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId")?.toIntOrNull() ?: 0
            val subtopicViewModel: SubtopicViewModel = viewModel(factory = SubtopicViewModelFactory(repository, topicId))
            SubtopicScreen(navController = navController, subtopicViewModel = subtopicViewModel)
        }
        composable("resourceList/{subtopicId}") { backStackEntry ->
            val subtopicId = backStackEntry.arguments?.getString("subtopicId")?.toIntOrNull() ?: 0
            val resourceViewModel: ResourceViewModel = viewModel(
                factory = ResourceViewModelFactory(repository, subtopicId)
            )
            ResourceScreen(navController = navController,resourceViewModel = resourceViewModel)
        }
    }
}