package com.example.studymaterial.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studymaterial.models.Resource
import com.example.studymaterial.viewmodel.ResourceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceScreen(
    navController: NavController,
    resourceViewModel: ResourceViewModel = viewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedResource by remember { mutableStateOf<Resource?>(null) }
    val resources by resourceViewModel.allResource.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text("Resources")
                },
                navigationIcon = {
                    Icon(Icons.Default.ArrowBack,
                        contentDescription = "Home",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                },
                actions = {
                    Icon(Icons.Default.Home,
                        contentDescription = "Home",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                navController.popBackStack("topicList", inclusive = false)
                            }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true},
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (resources.isEmpty()) {
                Text("No resource available", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(resources) { resource ->
                        ResourceCard(
                            resource = resource,
                            onEditClick = {
                                selectedResource = it
                                showEditDialog = true
                            },
                            onDeleteClick = {
                                selectedResource = it
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        AddResource(
            resourceViewModel = resourceViewModel,
            onDismiss = { showAddDialog = false }
        )
    }

    if (showEditDialog && selectedResource != null) {
        UpdateResource(
            resourceViewModel = resourceViewModel,
            resource = selectedResource!!,
            onDismiss = { showEditDialog = false }
        )
    }

    if (showDeleteDialog && selectedResource != null) {
        DeleteResource(
            resourceViewModel = resourceViewModel,
            resource = selectedResource!!,
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun ResourceCard(
    resource: Resource,
    onEditClick: (Resource) -> Unit,
    onDeleteClick: (Resource) -> Unit
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text =  resource.resourceUrl,
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.resourceUrl))
                    intent.setPackage("com.android.chrome")
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        intent.setPackage(null)
                        context.startActivity(intent)
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { onEditClick(resource) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.run { buttonColors(containerColor = MaterialTheme.colorScheme.primary) }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }

                Button(
                    onClick = { onDeleteClick(resource) },
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
fun AddResource(
    resourceViewModel: ResourceViewModel,
    onDismiss: () -> Unit
) {
    var resourceUrlState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add New Resource") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = resourceUrlState,
                    onValueChange = { resourceUrlState = it },
                    label = { Text("Resource Url") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (resourceUrlState.isNotBlank()) {
                        resourceViewModel.insertResource(
                            Resource(
                                resourceName = "Resource",
                                subtopicId = resourceViewModel.subtopicId,
                                resourceUrl = resourceUrlState
                            )
                        )
                        onDismiss()
                    }
                }
            ) {
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
fun UpdateResource(
    resourceViewModel: ResourceViewModel,
    resource: Resource,
    onDismiss: () -> Unit
) {
    var updatedResourceUrl by remember { mutableStateOf(resource.resourceUrl) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Resource") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = updatedResourceUrl,
                    onValueChange = { updatedResourceUrl = it },
                    label = { Text("Resource Name") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (updatedResourceUrl.isNotBlank()) {
                        resourceViewModel.updateResource(resource.copy(resourceUrl = updatedResourceUrl))
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
fun DeleteResource(
    resourceViewModel: ResourceViewModel,
    resource: Resource,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Delete Resource") },
        text = { Text("Are you sure you want to delete '${resource.resourceUrl}'?") },
        confirmButton = {
            Button(
                onClick = {
                    resourceViewModel.deleteResource(resource)
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




