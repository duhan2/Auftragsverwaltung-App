package com.example.myapplication.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.kategorie.Kategorie
import com.example.myapplication.kategorie.KategorieChanges
import com.example.myapplication.kategorie.KategorieViewModel
import com.example.myapplication.reparatur.Reparatur


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReparaturScreen(
    kategorieViewModel: KategorieViewModel,
    navController: NavController,
    kategorieChanges: KategorieChanges,
    kategorieliste: State<List<Kategorie>>

) {
    //sollte nur einmal ausgeführt werden
    //kategorieChanges.resetchanges()

    //Eventuell Alühabetisch sortieren
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        kategorieliste.value.forEach {

            stickyHeader {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = it.kategorie_name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(items = it.reparaturliste, key = { item: Reparatur -> item.id }) { item ->

                val openAlertDialog = remember { mutableStateOf(false) }

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .combinedClickable(
                            onClick = {
                                kategorieChanges.kategoriename = item.reparatur_kategorie
                                kategorieChanges.reparaturname = item.reparatur_name
                                kategorieChanges.reparaturpreis = item.reparatur_preis
                                kategorieChanges.repid = item.id
                                navController.navigate("reparatureingabe")
                            },
                            onLongClick = {
                                openAlertDialog.value = true
                            }
                        )
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = item.reparatur_name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(3f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "%.2f".format(item.reparatur_preis) + "€",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    when {
                        openAlertDialog.value -> {
                            //println("In Item ${item.reparatur_name}")
                            AlertDialog(
                                onDismissRequest = { openAlertDialog.value = false },
                                onConfirmation = {

                                    //Ganz (toMutableList) wichtig um call by referenze zu verhindern
                                    val newreplist = it.reparaturliste.toMutableList()
                                    newreplist.remove(item)
                                    //Ersetze alte Liste an der Stelle durch neue Liste
                                    kategorieViewModel.update(
                                        Kategorie(
                                            it.kategorie_name,
                                            newreplist
                                        )
                                    )

                                    openAlertDialog.value = false

                                    println("Confirmation registered")
                                },
                                dialogText = "Reparatur ${item.reparatur_name} wird gelöscht",
                                dialogTitle = "Reparatur löschen ?"
                            )
                        }
                    }

                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }


        }


    }

}


