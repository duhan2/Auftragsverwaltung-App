package com.example.myapplication

import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(34)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Editscreen(
    kundeViewModel: KundeViewModel,
    navController: NavController,
    stagedReparaturChanges: ReparaturChanges
) {
    //TODO Bei Bildschirmdrehen wird Seite neu geladen, Bildschirm drehen blockieren
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, 8.dp)
            .verticalScroll(rememberScrollState())
    ) {

        val context = LocalContext.current

        //eventuell muss das eine statelist sein wenn anzeigefehler
        val kundenliste by kundeViewModel.getallKunden().observeAsState(listOf())

        //Algorithmus zum erschaffen der kleinstmöglichen ID
        //Eventuell crash wegen ?
        var kundenid: Int = when (val x = kundenliste.minByOrNull { it.id }?.id) {
            null -> 1
            1 -> (kundenliste.maxBy { it.id }.id + 1)
            else -> (x - 1)
        }
        //Overwrite ID wenn prepopulate
        if (stagedReparaturChanges.kundenid != 0) {
            kundenid = stagedReparaturChanges.kundenid
        }

        var nameinput by remember { mutableStateOf("") }

        //prepopulate nameinput
        if (stagedReparaturChanges.nameinput != "") {
            nameinput = stagedReparaturChanges.nameinput
        }

        TextField(value = nameinput, onValueChange = {
            nameinput = it
            stagedReparaturChanges.nameinput = it
        }, label = { Text("Name") })

        //prepopulate localDate
        val localDate: LocalDate = if (stagedReparaturChanges.localDate == LocalDate.EPOCH) {
            LocalDate.now()
        } else {
            stagedReparaturChanges.localDate
        }

        Spacer(modifier = Modifier.size(30.dp))

        var numberinput by remember { mutableStateOf("") }
        //Prepopulate numberinput
        if (stagedReparaturChanges.numberinput != "") {
            numberinput = stagedReparaturChanges.numberinput
        }

        TextField(
            value = numberinput,
            onValueChange = {
                numberinput = it
                stagedReparaturChanges.numberinput = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Telefonnummer") }
        )

        Spacer(modifier = Modifier.size(20.dp))

        val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate: String = localDate.format(pattern) //17-02-2022

        Text(
            text = "Eingangsdatum $formattedDate",
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.Bold
        )

        //Gesamtreps eine Kopie von StagedChanges
        //Liste von an Stagedchanges übergebenes Objekt einlesen
        val gesamtreps = mutableListOf<Reparatur>()
        stagedReparaturChanges.gesamtreps.forEach {
            gesamtreps.add(it)
        }

        Spacer(modifier = Modifier.size(20.dp))

        Button(onClick = {
            navController.navigate("auswahl")
        }) {
            Icon(
                Icons.Filled.List,
                contentDescription = "zum Auswahlscreen",
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(text = "Zum Auswahl Screen")
        }

        Spacer(modifier = Modifier.size(20.dp))

        LazyColumn(
            modifier = Modifier.height(600.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Bezeichnung",
                        modifier = Modifier.weight(4f),
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = "Anzahl",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = "Einzelpreis",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = "Gesamtpreis",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
                    )

                }
            }
            items(gesamtreps) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //Bezeichnung
                    Text(
                        text = " ${it.reparatur_kategorie} : ${it.reparatur_name}",
                        modifier = Modifier.weight(4f), color = MaterialTheme.colorScheme.background
                    )
                    //Anzahl
                    Text(
                        text = "${it.anzahl}",
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.background
                    )
                    //Einzelpreis
                    Text(
                        text = "${"%.2f".format(it.reparatur_preis)}€",
                        modifier = Modifier.weight(2f),
                        color = MaterialTheme.colorScheme.background
                    )
                    //Gesamtpreis
                    Text(
                        text = "${"%.2f".format(it.reparatur_preis * it.anzahl)}€",
                        modifier = Modifier.weight(2f), color = MaterialTheme.colorScheme.background
                    )
                }

            }

        }

        Spacer(modifier = Modifier.size(20.dp))

        var extrastext by remember { mutableStateOf("") }
        if (stagedReparaturChanges.extrasachen != "") {
            extrastext = stagedReparaturChanges.extrasachen
        }
        var extrasnum by remember { mutableStateOf("") }
        if (stagedReparaturChanges.aufpreis != 0.00F) {
            extrasnum = stagedReparaturChanges.aufpreis.toString()
        }

        Row {
            OutlinedTextField(
                value = extrastext,
                onValueChange = {
                    extrastext = it
                    stagedReparaturChanges.extrasachen = it
                },
                label = { Text("Extras") },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.background)
            )

            TextField(
                value = extrasnum,
                onValueChange = {
                    extrasnum = it
                    stagedReparaturChanges.aufpreis = it.toFloat()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Aufpreis") }
            )

        }


        Spacer(modifier = Modifier.size(20.dp))
        //Gesamtpreis aufaddieren
        var gesamtpreis = 0.0F
        gesamtreps.forEach { rep -> gesamtpreis += (rep.reparatur_preis * rep.anzahl) }
        //Stürzt sonst ab bei leerem String
        gesamtpreis += try {
            extrasnum.toFloat()
        } catch (e: NumberFormatException) {
            0.00F
        }

        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {

            if (nameinput != "") {
                if (numberinput != "") {

                    if (gesamtreps.isNotEmpty()) {
                        //Wegen OnConflictStrategy.REPLACE sollte das hier bei gleicher ID den Kunden ersetzen
                        kundeViewModel.insert(
                            Kunde(
                                kundenid,
                                nameinput,
                                gesamtpreis,
                                localDate,
                                numberinput,
                                "eingegangen",
                                gesamtreps,
                                extrastext,
                                try {
                                    extrasnum.toFloat()
                                } catch (e: NumberFormatException) {
                                    0.00F
                                }
                            )
                        )
                        //Keine Changes staged
                        stagedReparaturChanges.resetchanges()
                        navController.navigate("home")
                    } else {
                        val toast = Toast.makeText(
                            context,
                            "Bitte Reparaturen auswählen",
                            Toast.LENGTH_SHORT
                        ) // in Activity
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        context,
                        "Bitte Nummer eingeben",
                        Toast.LENGTH_SHORT
                    ) // in Activity
                    toast.show()
                }
            } else {
                val toast = Toast.makeText(
                    context,
                    "Bitte Name eingeben",
                    Toast.LENGTH_SHORT
                ) // in Activity
                toast.show()
            }

        }) {
            Icon(imageVector = Icons.Filled.Done, contentDescription = "Kunde hinzufügen")
            Text(text = "Auftrag Hinzufügen")
        }

    }
}