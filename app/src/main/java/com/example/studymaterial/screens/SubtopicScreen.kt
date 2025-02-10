package com.example.studymaterial.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studymaterial.models.Subtopic
import com.example.studymaterial.viewmodel.SubtopicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtopicScreen(
    navController: NavController,
    subtopicViewModel: SubtopicViewModel = viewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedSubtopic by remember { mutableStateOf<Subtopic?>(null) }
    val subtopics by subtopicViewModel.allSubtopic.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text("Subtopics") },
                navigationIcon = {
                    Icon(Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { navController.popBackStack() }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
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
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (subtopics.isEmpty()) {
                Text("No subtopics available", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(subtopics) { subtopic ->
                        SubtopicCard(
                            subtopic = subtopic,
                            onChatClick = {
                                subtopicViewModel.markSubtopicAsRead(subtopicId = subtopic.subtopicId)
                                navController.navigate("resourceList/${subtopic.subtopicId}")
                            },
                            onEditClick = {
                                selectedSubtopic = it
                                showEditDialog = true
                            },
                            onDeleteClick = {
                                selectedSubtopic = it
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddSubtopic(
            subtopicViewModel = subtopicViewModel,
            onDismiss = { showAddDialog = false }
        )
    }

    if (showEditDialog && selectedSubtopic != null) {
        UpdateSubtopic(
            subtopicViewModel = subtopicViewModel,
            subtopic = selectedSubtopic!!,
            onDismiss = { showEditDialog = false }
        )
    }

    if (showDeleteDialog && selectedSubtopic != null) {
        DeleteSubtopic(
            subtopicViewModel = subtopicViewModel,
            subtopic = selectedSubtopic!!,
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun SubtopicCard(
    subtopic: Subtopic,
    onChatClick: () -> Unit,
    onEditClick: (Subtopic) -> Unit,
    onDeleteClick: (Subtopic) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onChatClick)
            .padding(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = subtopic.subtopicName,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 30.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { onEditClick(subtopic) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.run { buttonColors(containerColor = MaterialTheme.colorScheme.primary) }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }

                Button(
                    onClick = { onDeleteClick(subtopic) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun AddSubtopic(
    subtopicViewModel: SubtopicViewModel,
    onDismiss: () -> Unit
) {
    var subtopicNameState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add New Subtopic") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = subtopicNameState,
                    onValueChange = { subtopicNameState = it },
                    label = { Text("Subtopic Name") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val name = subtopicNameState

                if (!name.isNullOrBlank()) {
                    subtopicViewModel.insertSubtopic(
                        Subtopic(
                            topicId = subtopicViewModel.topicId,
                            subtopicName = name,
                            isRead = false
                        )
                    )
                    subtopicNameState = ""
                    onDismiss() //
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
fun UpdateSubtopic(
    subtopicViewModel: SubtopicViewModel,
    subtopic: Subtopic,
    onDismiss: () -> Unit
) {
    var updatedSubtopicName by remember { mutableStateOf(subtopic.subtopicName) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Subtopic") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = updatedSubtopicName,
                    onValueChange = { updatedSubtopicName = it },
                    label = { Text("Subtopic Name") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (updatedSubtopicName.isNotBlank()) {
                        subtopicViewModel.updateSubtopic(subtopic.copy(subtopicName = updatedSubtopicName))
                        onDismiss()
                    }
                }
            ) {
                Text("Update")
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
fun DeleteSubtopic(
    subtopicViewModel: SubtopicViewModel,
    subtopic: Subtopic,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Delete Subtopic") },
        text = { Text("Are you sure you want to delete '${subtopic.subtopicName}'?") },
        confirmButton = {
            Button(
                onClick = {
                    subtopicViewModel.deleteSubtopic(subtopic)
                    onDismiss()
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}






