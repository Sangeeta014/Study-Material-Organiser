package com.example.studymaterial.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.studymaterial.models.Topic
import com.example.studymaterial.viewmodel.TopicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    topicViewModel: TopicViewModel = viewModel(),
    navController: NavController
) {
    val topics by topicViewModel.allTopics.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    val context = LocalContext.current

    // Fetch images for each topic
    val imageResult = topicViewModel.imageResult.observeAsState()
    val imageList = imageResult.value
//    val imageCount = imageList!!.size

    Log.d("resultForImage", "$imageList")

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.outline
                ),
                title = { Text("Topics") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(topics) { index,topic ->
                val subtopicCount by topicViewModel.getSubtopicCount(topic.topicId).collectAsState(context)
                val unreadSubtopicCount by topicViewModel.getUnreadSubtopicCount(topic.topicId).collectAsState(context)
                val count = (unreadSubtopicCount as? Int) ?: 0

                var imageUrl  = ""
                if (!imageList.isNullOrEmpty()) {

                    var imageIndex = index

                    if (imageIndex >= imageList.size) {
                        imageIndex = index % imageList.size
                    }
                    imageUrl = imageList[imageIndex].download_url
                }

                TopicCard(
                    topic = topic,
                    itemCount = subtopicCount.toString(),
                    unreadItemCount = count,
                    onChatClick = {
                        selectedTopic = topic
                        navController.navigate("subtopicList/${topic.topicId}")
                    },
                    imageUrl = imageUrl
                )

            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add New Topic") },
            text = {
                AddTopic(
                    topicViewModel = topicViewModel,
                    onDismiss = { showDialog = false }
                )
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun TopicCard(
    topic: Topic,
    itemCount: String,
    unreadItemCount: Int,
    onChatClick: () -> Unit,
    imageUrl: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onChatClick() }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                clip = true
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                ImageCard(imageUrl)
            }

            Spacer(modifier = Modifier.width(2.dp))

            // Content Column
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Topic Name-Badge(Unread Count)
                BadgedBox(
                    badge = {
                        if (unreadItemCount > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Text(
                                    text = unreadItemCount.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = topic.topicName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            // Count Badge
            Badge(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = itemCount,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Composable
fun AddTopic(
    topicViewModel: TopicViewModel,
    onDismiss: () -> Unit
) {
    var topicNameState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add New Topic") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = topicNameState,
                    onValueChange = { topicNameState = it },
                    label = { Text("Topic Name") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val name = topicNameState

                if (!name.isNullOrBlank()) {
                    topicViewModel.insertTopic(
                        Topic(
                            topicName = name
                        )
                    )
                    topicNameState = ""
                    onDismiss()
                } else {
                    Log.d("AddTopic", "Invalid topic ID or topic name")
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ImageCard(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Topic Image",
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape),
        contentScale = ContentScale.Crop
    )
}
