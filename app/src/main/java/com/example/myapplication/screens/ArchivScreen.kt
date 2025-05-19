package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.archiv.Archiv
import com.example.myapplication.archiv.ArchivChanges

@Composable
fun ArchivScreen(
    navController: NavController,
    archivListState: State<List<Archiv>>,
    archivChanges: ArchivChanges
) {
    // State for the search input
    var searchInput by remember {
        mutableStateOf("")
    }
    // State for the dropdown menu's expanded state

    val focusManager = LocalFocusManager.current

    val filteredOptions = remember(searchInput) {
        if (searchInput.isBlank()) emptyList()
        else archivListState.value.filter { it.name.contains(searchInput, ignoreCase = true) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchInput,
                onValueChange = {
                    searchInput = it
                },
                singleLine = true,
                trailingIcon = {
                    if (searchInput.isNotEmpty()) {
                        IconButton(onClick = {
                            searchInput = ""
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Löschen")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(200.dp)) // Simuliertes nachfolgendes UI
            Text("Darunterliegendes UI", style = MaterialTheme.typography.bodyLarge)
        }
        if (filteredOptions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = 72.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        MaterialTheme.shapes.medium
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.medium
                    )
            ) {
                items(filteredOptions) { archiv ->
                    Text(
                        text = archiv.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                archivChanges.setfromobj(archiv)
                                searchInput = archiv.name
                                focusManager.clearFocus()
                                navController.navigate("kundenansicht")
                            }
                            .padding(12.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
            }


        }
    }
}