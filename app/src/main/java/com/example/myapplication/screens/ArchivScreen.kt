package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.archiv.Archiv
import com.example.myapplication.archiv.ArchivChanges

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivScreen(
    navController: NavController,
    archivListState: State<List<Archiv>>,
    archivChanges: ArchivChanges
) {
    // State for the search input
    var searchInput by remember {
        mutableStateOf(TextFieldValue())
    }

    // State for the filtered options
    var filteredOptions by remember {
        mutableStateOf(emptyList<Archiv>())
    }

    // State for the dropdown menu's expanded state
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val focusManager: FocusManager = LocalFocusManager.current
    Column(Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = it }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryEditable,
                        enabled = isDropdownExpanded
                    ),
                value = searchInput,
                onValueChange = { newSearchInput ->
                    searchInput = newSearchInput
                    isDropdownExpanded = newSearchInput.text.isNotBlank()
                    filteredOptions = filterArchivList(archivListState.value, newSearchInput.text)
                },
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                },
                singleLine = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            if (filteredOptions.isNotEmpty()) {
                ExposedDropdownMenu(
                    modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true),
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    filteredOptions.forEach { archiv ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = archiv.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Person",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            onClick = {
                                archivChanges.setfromobj(archiv)
                                navController.navigate("kundenansicht")
                                isDropdownExpanded = false
                                focusManager.clearFocus()
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        )
                    }

                }
            }
        }
    }
}

// Helper function to filter the list
fun filterArchivList(archivList: List<Archiv>, searchText: String): List<Archiv> {
    return if (searchText.isBlank()) {
        emptyList()
    } else {
        archivList.filter {
            it.name.contains(searchText, ignoreCase = true)
        }
    }
}
