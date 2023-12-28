package com.example.myapplication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReparaturScreen(
    kategorieViewModel: KategorieViewModel
) {

    val kategorielist = kategorieViewModel.getallKategorien().observeAsState(listOf())

    //Eventuell Alühabetisch sortieren
    LazyColumn(
        modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)
    ) {
        kategorielist.value.forEach() {

            stickyHeader {
                Text(text = it.kategorie_name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            items(it.reparaturliste) { item ->

                val openAlertDialog = remember { mutableStateOf(false) }

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                //Hier kann man später das so implementieren, dass voll ausgefüllte Seite erscheint
                            },
                            onLongClick = {
                                openAlertDialog.value = true
                            }
                        )
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = item.reparatur_name,modifier = Modifier.weight(1f))
                    Text(text = item.reparatur_preis.toString())
                    when {
                        openAlertDialog.value -> {
                            println("In Item ${item.reparatur_name}")
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
                                dialogTitle ="Reparatur löschen ?"
                            )
                        }
                    }

                }

            }


        }


    }


}

