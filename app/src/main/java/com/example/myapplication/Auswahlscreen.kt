package com.example.myapplication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuswahlScreen(
    navController: NavController,
    kategorieViewModel: KategorieViewModel,
    stagedReparaturChanges: ReparaturChanges
) {

    val list by kategorieViewModel.getallKategorien().observeAsState(listOf())

    val reparaturen = mutableListOf<Reparatur>()

    //Wenn im ViewModel eine Liste vorhanden ist, diese an die Screeninterne Liste übergeben
    if (stagedReparaturChanges.gesamtreps.isNotEmpty()) {
        stagedReparaturChanges.gesamtreps.forEach {
            //Kopie der Liste übergeben damit stagedChanges nicht bearbeitet wird,
            // solange man nicht den bestätigen Knopf drückt
            reparaturen.add(it.copy())
        }
    }

    Column {

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            list.forEach { kategorie ->

                stickyHeader {
                    Text(
                        text = kategorie.kategorie_name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(kategorie.reparaturliste) { item ->

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
                        .fillMaxWidth()) {

                        Column {

                            Text(text = item.reparatur_name, fontWeight = FontWeight.Bold)
                            if (expanded) {
                                Row {

                                    Button(onClick = {
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
                                    Text(text = amount.toString())
                                    Button(onClick = {
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
                }

            }

        } //
        Button(onClick = {

            //Auswahlscreeninterne ReparaturenListe in das ViewModel setzen
            stagedReparaturChanges.gesamtreps = reparaturen

            navController.navigate("edit")
        }) {
            Text(text = "Auswahl bestätigen")
        }
    }
}