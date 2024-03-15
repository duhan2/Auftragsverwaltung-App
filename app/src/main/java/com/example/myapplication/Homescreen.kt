package com.example.myapplication

import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@RequiresApi(34)
@Composable
fun Homescreen(
    kundeViewModel: KundeViewModel,
    navController: NavController,
    stagedReparaturChanges: ReparaturChanges
) {

    val list by kundeViewModel.getallKunden().observeAsState(listOf())


    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(list) { item: Kunde ->
            val color = when(item.status){
                "abgeschlossen" -> MaterialTheme.colorScheme.outline
                "eingegangen" -> MaterialTheme.colorScheme.onTertiaryContainer
                //Sollte nie vorkommen
                else -> Color.Green
            }
            Row(
                modifier = Modifier
                    .clickable {
                        stagedReparaturChanges.setfromKundeobj(item)
                        navController.navigate("auftrag")
                    }
                    .background(color = color)
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.Name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f),color = MaterialTheme.colorScheme.background)
                Kunde_EditButton(
                    item,
                    navController,
                    stagedReparaturChanges = stagedReparaturChanges
                )
                Kunde_LoeschButton(item, kundeViewModel)

            }
            Divider(thickness = 1.dp, color = Color.DarkGray)
        }
    }

}

@Composable
fun RowScope.Kunde_LoeschButton(kunde: Kunde, kundeViewModel: KundeViewModel) {
    //Normalerweise referenz übergeben, aber keine Ahnung wie das geht
    val openAlertDialog = remember { mutableStateOf(false) }

    IconButton(
        //ganz wichtig sonst schreibt der nicht nebeneinenader
        modifier = Modifier.weight(1f), onClick = {
            openAlertDialog.value = true
            //kundeViewModel.delete(kunde)
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                //modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Outlined.Delete, contentDescription = "", tint = Color.Red
            )
            //Spacer(modifier = Modifier.width(width = 8.dp))
            Text(text = "Löschen", color = Color.Black)
        }
        when {
            openAlertDialog.value -> {
                //println("Ich bin in Kategorie ${selectedOption.kategorie_name} ")
                AlertDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        kundeViewModel.delete(kunde)
                        openAlertDialog.value = false

                        //println("Confirmation registered")
                    },
                    dialogText = "Diese Aktion kann nicht rückgängig gemacht werden.",
                    dialogTitle = "Kunde löschen ?"
                )
            }
        }
    }


}

@RequiresApi(34)
@Composable
fun RowScope.Kunde_EditButton(
    kunde: Kunde, navController: NavController, stagedReparaturChanges: ReparaturChanges
) {

    IconButton(
        //ganz wichtig sonst schreibt der nicht nebeneinenader
        modifier = Modifier.weight(1f), onClick = {
            //Ausgewählten Kunden in StagedChanges speichern
            stagedReparaturChanges.setfromKundeobj(kunde)
            navController.navigate("edit")

        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                //modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Outlined.Edit, contentDescription = "", tint = Color.White
            )
            //Spacer(modifier = Modifier.width(width = 8.dp))
            Text(text = "Bearbeiten", color = Color.Black)
        }
    }


}


