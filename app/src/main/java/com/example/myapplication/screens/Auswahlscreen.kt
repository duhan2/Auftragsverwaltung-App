package com.example.myapplication.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.kategorie.Kategorie
import com.example.myapplication.reparatur.Reparatur
import com.example.myapplication.reparatur.ReparaturChanges

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuswahlScreen(
    navController: NavController,
    //kategorieViewModel: KategorieViewModel,
    stagedReparaturChanges: ReparaturChanges,
    kategorieliste: State<List<Kategorie>>
) {

//    val list by kategorieViewModel.getallKategorien().observeAsState(listOf())

    val reparaturen = mutableListOf<Reparatur>()

    //Wenn im ViewModel eine Liste vorhanden ist, diese an die Screeninterne Liste übergeben
    if (stagedReparaturChanges.gesamtreps.isNotEmpty()) {
        stagedReparaturChanges.gesamtreps.forEach {
            //Kopie der Liste übergeben damit stagedChanges nicht bearbeitet wird,
            // solange man nicht den bestätigen Knopf drückt
            //FRAUDWATCH
            reparaturen.add(it.copy())
            /*
            if (it.reparatur_kategorie != "Extras") {
                reparaturen.add(it.copy())
            }*/
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        kategorieliste.value.forEach { kategorie ->

            stickyHeader {
                Text(
                    text = kategorie.kategorie_name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )
            }

            items(items = kategorie.reparaturliste, key = { item: Reparatur ->
                // Return a stable + unique key for the item
                item.id
            }) { item ->

                var expanded by remember {
                    mutableStateOf(false)
                }
                var amount by remember {
                    mutableIntStateOf(0)
                }
                //Wenn die Liste prepopulated ist, dann ist amount = der Anzahl der Reparaturen
                val check = reparaturen.find { it.id == item.id }
                if (check != null) {
                    amount = check.anzahl
                }
                Row(modifier = Modifier
                    .clickable { expanded = !expanded }
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)) {

                    Column {

                        Text(
                            text = item.reparatur_name,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )
                        if (expanded) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Button(
                                    modifier = Modifier
                                        //.weight(1f)
                                        .padding(horizontal = 16.dp),
                                    shape = RectangleShape,
                                    onClick = {
                                        when (amount) {
                                            0 -> reparaturen.remove(item)
                                            1 -> {
                                                reparaturen.remove(item)
                                                amount -= 1
                                            }

                                            else -> {
                                                amount -= 1

                                                var index = 0
                                                reparaturen.forEach {
                                                    if (it.id == item.id) {

                                                        reparaturen[index].anzahl = amount

                                                    }
                                                    index += 1
                                                }

                                            }

                                        }
                                    }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Clear,
                                        contentDescription = "",
                                        tint = Color.Red
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(color = Color.White)
                                        //.weight(1f)
                                        .padding(horizontal = 32.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = amount.toString(),
                                        color = MaterialTheme.colorScheme.background,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Button(
                                    modifier = Modifier
                                        //.weight(1f)
                                        .padding(horizontal = 16.dp),
                                    shape = RectangleShape,
                                    onClick = {
                                        when (amount) {
                                            0 -> {
                                                amount += 1
                                                reparaturen.add(item.copy(anzahl = amount))
                                            }

                                            else -> {

                                                amount += 1

                                                var index = 0
                                                reparaturen.forEach {
                                                    if (it.id == item.id) {
                                                        reparaturen[index].anzahl = amount
                                                    }
                                                    index += 1
                                                }

                                            }
                                        }

                                    }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "",
                                        tint = Color.Green
                                    )
                                }
                            }
                        }

                    }
                }
                HorizontalDivider(thickness = 1.dp, color = Color.DarkGray)
            }

        }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier.align(Alignment.CenterEnd), onClick = {

                    //Auswahlscreeninterne ReparaturenListe in das ViewModel setzen
                    stagedReparaturChanges.gesamtreps = reparaturen

                    navController.navigate("edit")
                }) {
                    Text(text = "Auswahl bestätigen")
                }
            }
        }
    }
}
