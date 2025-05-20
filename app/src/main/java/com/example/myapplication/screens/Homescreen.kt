package com.example.myapplication.screens

import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.kunde.Kunde
import com.example.myapplication.kunde.KundeViewModel
import com.example.myapplication.reparatur.ReparaturChanges

@RequiresApi(34)
@Composable
fun Homescreen(
    kundeViewModel: KundeViewModel,
    navController: NavController,
    stagedReparaturChanges: ReparaturChanges,
    kundenliste: State<List<Kunde>>
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(items = kundenliste.value, key = { item: Kunde ->
            item.id
        }) { item: Kunde ->
            var color = Color.Green
            var textcolor = Color.Green
            when (item.status) {
                "abgeschlossen" -> {
                    color = MaterialTheme.colorScheme.surfaceContainerLowest
                    textcolor = MaterialTheme.colorScheme.onSurface
                }
                "eingegangen" -> {
                    color = MaterialTheme.colorScheme.inverseSurface
                    textcolor = MaterialTheme.colorScheme.inverseOnSurface
                }
                //Sollte nie vorkommen
                else -> Color.Green
            }
            Row(modifier = Modifier
                .clickable {
                    stagedReparaturChanges.setfromKundeobj(item)
                    navController.navigate("auftrag")
                }
                .background(color = color)
                .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "" + item.id,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = textcolor
                )
                Column(modifier = Modifier.weight(4f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        //modifier = Modifier.weight(1f),
                        color = textcolor
                    )
                    Text(
                        text = item.status,
                        style = MaterialTheme.typography.bodyMedium,
                        //fontWeight = FontWeight.Bold,
                        //modifier = Modifier.weight(1f),
                        color = textcolor
                    )
                }

                Kunde_EditButton(
                    item, navController, stagedReparaturChanges = stagedReparaturChanges
                )
                Spacer(modifier = Modifier.size(5.dp))
                Kunde_LoeschButton(item, kundeViewModel)

            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

}

@Composable
fun RowScope.Kunde_LoeschButton(kunde: Kunde, kundeViewModel: KundeViewModel) {
    //Normalerweise referenz übergeben, aber keine Ahnung wie das geht
    val openAlertDialog = remember { mutableStateOf(false) }

    OutlinedButton(modifier = Modifier.weight(2f),
        colors = ButtonColors(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            Color.DarkGray,
            Color.Gray
        ),
        shape = CircleShape,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        onClick = {
            openAlertDialog.value = true
        }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Outlined.Delete, contentDescription = "",
            )
            Text(
                text = "Löschen",
                style = MaterialTheme.typography.labelMedium
            )
        }
        when {
            openAlertDialog.value -> {
                AlertDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        kundeViewModel.delete(kunde)
                        openAlertDialog.value = false
                    },
                    dialogText = "Diese Aktion kann nicht rückgängig gemacht werden.",
                    dialogTitle = "Kunde löschen ?",

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

    OutlinedButton(
        //ganz wichtig sonst schreibt der nicht nebeneinenader
        modifier = Modifier.weight(2f),
        colors = ButtonColors(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            Color.DarkGray,
            Color.Gray
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        shape = CircleShape,
        onClick = {
            //Ausgewählten Kunden in StagedChanges speichern
            stagedReparaturChanges.setfromKundeobj(kunde)
            stagedReparaturChanges.editparam = "edit"
            navController.navigate("edit")

        }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Outlined.Edit,
                contentDescription = "löschen"
            )
            Text(
                text = "Bearbeiten", style = MaterialTheme.typography.labelMedium
            )
        }
    }


}


