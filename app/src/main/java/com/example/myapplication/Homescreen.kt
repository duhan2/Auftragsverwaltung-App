package com.example.myapplication

import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

    //Erstmal clearen
    //stagedReparaturChanges.resetchanges()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(list) { item: Kunde ->
            Row(
                modifier = Modifier
                    .clickable {
                        stagedReparaturChanges.setfromKundeobj(item)
                        navController.navigate("auftrag")
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.Name, fontWeight = FontWeight.Bold)
                Kunde_EditButton(
                    item,
                    navController,
                    stagedReparaturChanges = stagedReparaturChanges
                )
                Kunde_LoeschButton(item, kundeViewModel)
            }
        }
    }

}

@Composable
fun RowScope.Kunde_LoeschButton(kunde: Kunde, kundeViewModel: KundeViewModel) {


    IconButton(
        //ganz wichtig sonst schreibt der nicht nebeneinenader
        modifier = Modifier.weight(1f), onClick = {
            kundeViewModel.delete(kunde)
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                //modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Outlined.Delete, contentDescription = "", tint = Color.Red
            )
            //Spacer(modifier = Modifier.width(width = 8.dp))
            Text(text = "Löschen", color = Color.Gray)
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
                imageVector = Icons.Outlined.Edit, contentDescription = "", tint = Color.Gray
            )
            //Spacer(modifier = Modifier.width(width = 8.dp))
            Text(text = "Bearbeiten", color = Color.Gray)
        }
    }


}


