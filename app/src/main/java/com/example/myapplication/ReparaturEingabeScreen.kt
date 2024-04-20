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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ExposedDropdownMenuDefaults
//noinspection UsingMaterialAndMaterial3Libraries
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
    var replace = false

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

        val reparaturID: Int
        if (kategorieChanges.repid != -1) {
            //println("Repid is: " + kategorieChanges.repid)
            reparaturID = kategorieChanges.repid
            replace = true
            //kategorieChanges.resetrepid()
        }
        else{
            //println("HALO ICHBINHIER")
            reparaturID = when (val z = idListe.minByOrNull { it }) {
                //Wenn Liste leer ID = 1
                null -> 1
                //Wenn kleinste ID 1, dann neue ID = größte ID + 1
                1 -> (idListe.maxBy { it } + 1)
                //Neue ID = kleinste ID-1
                else -> (z - 1)
            }
        }

        var expanded by remember { mutableStateOf(false) }

        //var selectedOptionText by remember { mutableStateOf("noch leer") }
        var selectedOptionText by remember { mutableStateOf("noch leer") }
        //Wenn eine Kategorie bereits in Changes, dann das als Selectedoptiontext
        if (kategorieChanges.kategoriename != "leer") {
            selectedOptionText = kategorieChanges.kategoriename
            //Sonst +berschreibt der selectedOptionText ganze Zeit
            kategorieChanges.resetkatname()
        }

        val context = LocalContext.current

        var buttonenabler by remember { mutableStateOf(true) }

        //Kann eigentlich auch nach oben
        val openAlertDialog = remember { mutableStateOf(false) }

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

            var alertoption = Kategorie("leer", (mutableListOf(Reparatur())))

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                options.value.forEach { selectedOption ->
                    //println(selectedOption.kategorie_name)
                    //Für Kategorie löschen
                    Row(
                        modifier = Modifier
                            .combinedClickable(onClick = {

                                //Anzeigekategorie ist gleich gewählte Kategorie
                                selectedOptionText = selectedOption.kategorie_name

                                expanded = false
                            },
                                onLongClick = {
                                    alertoption = selectedOption
                                    openAlertDialog.value = true
                                })
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    {
                        Text(text = selectedOption.kategorie_name)

                    }
                }
                //An dieser Stelle, weil sonst für jedes Listen-Element AlertDialog geöffnet wird
                when {
                    openAlertDialog.value -> {
                        //println("Ich bin in Kategorie ${selectedOption.kategorie_name} ")
                        AlertDialog(
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                kategorieViewModel.delete(alertoption)
                                openAlertDialog.value = false
                                kategorieChanges.resetchanges()
                                selectedOptionText = "gelöscht"
                                //println("Confirmation registered")
                            },
                            dialogText = "Kategorie ${alertoption.kategorie_name} wird gelöscht",
                            dialogTitle = "Kategorie löschen ?"
                        )
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

        var repname by remember { mutableStateOf("") }

        if (kategorieChanges.reparaturname != "leer") {
            repname = kategorieChanges.reparaturname
            kategorieChanges.resetrepname()
        }

        TextField(
            label = { Text("Reparaturenname") },
            singleLine = true,
            value = repname,
            onValueChange = { repname = it }
        )

        Spacer(modifier = Modifier.size(100.dp))


        var reppreis by remember {
            mutableStateOf("")
        }
        if (kategorieChanges.reparaturpreis != 0.0f) {
            reppreis = kategorieChanges.reparaturpreis.toString()
            kategorieChanges.resetreppreis()
        }

        TextField(
            label = { Text(text = "Preis") },
            singleLine = true,
            value = reppreis,
            onValueChange = {
                reppreis = it
                buttonenabler = try {
                    reppreis.toFloat()
                    true
                } catch (e: NumberFormatException) {
                    false
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )


        Spacer(modifier = Modifier.size(130.dp))

        Button(enabled = buttonenabler, shape = CircleShape ,onClick = {

            if ((selectedOptionText == "Neue Katgeorie") || (selectedOptionText == "gelöscht") || (selectedOptionText == "leer")) {
                Toast.makeText(context, "ungültige Kategorie", Toast.LENGTH_SHORT).show()
            } else if ((repname == "") || (reppreis == "")) {
                Toast.makeText(
                    context,
                    "kein Reparaturname oder Preis angegeben",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                options.value.find { it.kategorie_name == selectedOptionText }?.let { it ->
                    //Wenn Element ersetzt werden soll
                    if (replace) {
                        println("yesman")
                        it.reparaturliste[it.reparaturliste.indexOf(it.reparaturliste.find { it.id == reparaturID })] =
                            Reparatur(
                                reparaturID,
                                repname,
                                selectedOptionText,
                                reppreis.toFloat()
                            )

                    } else {
                        //add Methode macht keine Probleme. Ausser wenn es in Zukunft Probleme gibt, muss Liste geklont mit .toMutablelist() werden
                        println("Reparaturid is: $reparaturID")
                        it.reparaturliste.add(
                            Reparatur(
                                reparaturID,
                                repname,
                                selectedOptionText,
                                reppreis.toFloat()
                            )
                        )
                    }
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
            Text(text = "Bestätigen", fontSize = 40.sp )
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
            Text(text = dialogTitle, color = Color.Black, fontWeight = Bold, fontSize = 26.sp)
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



