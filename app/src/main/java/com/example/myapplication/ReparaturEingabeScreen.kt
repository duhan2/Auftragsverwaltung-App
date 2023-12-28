package com.example.myapplication

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ReparaturEingabeScreen(
    kategorieViewModel: KategorieViewModel,
    navController: NavController,
    kategorieChanges: KategorieChanges,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(modifier = Modifier.size(30.dp))

        val options = kategorieViewModel.getallKategorien().observeAsState(listOf())

        //ID Liste um die größte ID zu bekommen
        val idListe = mutableListOf<Int>()
        //Für jede Kategorie
        options.value.forEach { kategorie ->
            //Für jede Reparatur innerhalb der Kategorie
            kategorie.reparaturliste.forEach {
                //Füge der idListe die ID hinzu
                idListe.add(it.id)
            }
        }

        //Algorithmus zum erschaffen der kleinstmöglichen ID
        val reparaturID: Int = when (val z = idListe.minByOrNull { it }) {
            //Wenn Liste leer ID = 1
            null -> 1
            //Wenn kleinste ID 1, dann neue ID = größte ID + 1
            1 -> (idListe.maxBy { it } + 1)
            //Neue ID = kleinste ID-1
            else -> (z - 1)
        }

        var expanded by remember { mutableStateOf(false) }

        var selectedOptionText by remember { mutableStateOf("noch leer") }

        //Wenn eine Kategorie bereits in Changes, dann das als Selectedoptiontext
        if(kategorieChanges.kategoriename != "leer"){
            selectedOptionText = kategorieChanges.kategoriename
        }

        val context = LocalContext.current

        var buttonenabler by remember { mutableStateOf(true) }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
            expanded = !expanded
        }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = {
                    Text(
                        text = "Kategorie"
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                })

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                options.value.forEach { selectedOption ->

                    //Für Kategorie löschen
                    val openAlertDialog = remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .combinedClickable(onClick = {
                                selectedOptionText = selectedOption.kategorie_name
                                //println(selectedOption.kategorie_name)
                                expanded = false
                            },
                                onLongClick = {//gilt iwie für alle items
                                    openAlertDialog.value = true
                                })
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    {
                        Text(text = selectedOption.kategorie_name)
                        when {
                            openAlertDialog.value -> {
                                //println("Ich bin in Kategorie ${selectedOption.kategorie_name} ")
                                AlertDialog(
                                    onDismissRequest = { openAlertDialog.value = false },
                                    onConfirmation = {
                                        kategorieViewModel.delete(selectedOption)
                                        openAlertDialog.value = false
                                        kategorieChanges.resetchanges()
                                        selectedOptionText = "gelöscht"
                                        //println("Confirmation registered")
                                    },
                                    dialogText = "Kategorie ${selectedOption.kategorie_name} wird gelöscht",
                                    dialogTitle = "Kategorie löschen ?"
                                )
                            }
                        }
                    }
                }

                Row(modifier = Modifier
                    .clickable {
                        selectedOptionText = "Neue Kategorie"
                        navController.navigate("kategorieeingabe")
                        expanded = false
                    }
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Text(text = "Neue Kategorie")

                }

            }

        }

        Spacer(modifier = Modifier.size(100.dp))

        val repname = remember { mutableStateOf("") }
        TextField(
            label = { Text("Reparaturenname") },
            singleLine = true,
            value = repname.value,
            onValueChange = { repname.value = it }
        )

        Spacer(modifier = Modifier.size(100.dp))


        val reppreis = remember {
            mutableStateOf("")
        }
        TextField(
            label = { Text(text = "Preis") },
            singleLine = true,
            value = reppreis.value,
            onValueChange = {
                reppreis.value = it
                buttonenabler = try {
                    reppreis.value.toFloat()
                    true
                } catch (e: NumberFormatException) {
                    false
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )




        Spacer(modifier = Modifier.size(130.dp))

        Button(enabled = buttonenabler, onClick = {

            if ((selectedOptionText == "Neue Katgeorie") || (selectedOptionText == "gelöscht") || (selectedOptionText == "noch leer")) {
                Toast.makeText(context, "ungültige Kategorie", Toast.LENGTH_SHORT).show()
            } else {

                //Findmethode unnötig wenn Liste nicht random fehler haben sollte. Direkter Zugriff auf Element möglich
                options.value.find { it.kategorie_name == selectedOptionText }?.let {

                    //add Methode macht keine Probleme. Ausser wenn es in Zukunft Probleme gibt, muss Liste geklont mit .toMutablelist() werden
                    it.reparaturliste.add(
                        Reparatur(
                            reparaturID,
                            repname.value,
                            selectedOptionText,
                            reppreis.value.toFloat()
                        )
                    )
                    kategorieViewModel.update(
                        Kategorie(
                            selectedOptionText,
                            reparaturliste = it.reparaturliste
                        )
                    )

                }
                //staged Changes wieder leeren
                kategorieChanges.resetchanges()
                navController.navigate("reparaturen")
            }
        }) {
            Text(text = "Bestätigen")
        }

    }


}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogText: String,
    dialogTitle: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle, color = Color.Black, fontWeight = Bold, fontSize = 16.sp)
        },
        text = { Text(text = dialogText, color = Color.Black) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = "Bestätigen", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = "Abbrechen", color = Color.Black)
            }
        }
    )

}



