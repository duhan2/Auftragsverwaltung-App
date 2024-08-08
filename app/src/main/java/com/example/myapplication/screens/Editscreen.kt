package com.example.myapplication.screens

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
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
import com.example.myapplication.kunde.Kunde
import com.example.myapplication.kunde.KundeViewModel
import com.example.myapplication.reparatur.Reparatur
import com.example.myapplication.reparatur.ReparaturChanges
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnrememberedMutableState")
@RequiresApi(34)
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

        var size = 1
        var kundenid = 0

        //Overwrite ID wenn prepopulate
        if (stagedReparaturChanges.kundenid != 0) {
            kundenid = stagedReparaturChanges.kundenid
        } else {
            //Algorithmus zum erschaffen der kleinstmöglichen ID
            val temp = kundenliste.maxByOrNull { it.id }

            if (temp != null) {
                //Liste um 1 größer machen um bei voller Liste größte id generieren zu können
                size = temp.id + 1
            }
            val idListe = Array(size) { false }
            //Jede vergebene ID = true
            kundenliste.forEach { idListe[it.id - 1] = true }
            //Wenn id nicht vergeben ist
            loop@ for (i in 0..size) {
                if (!idListe[i]) {
                    //Dann kundenid = i+1 und raus aus der loop
                    kundenid = i + 1
                    break@loop
                }
            }
        }

        //Zum debuggen
        Text(
            text = "Kunden-ID: $kundenid",
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.Bold
        )

        var nameinput by remember { mutableStateOf("") }

        //prepopulate nameinput
        if (stagedReparaturChanges.nameinput != "") {
            nameinput = stagedReparaturChanges.nameinput
        }

        TextField(value = nameinput, onValueChange = {
            nameinput = it
            stagedReparaturChanges.nameinput = it
        }, label = { Text("Name") })

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

        //prepopulate localDate
        val pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var formattedDate: String = LocalDate.now().format(pattern) //17.02.2022
        if (stagedReparaturChanges.eingangsdatum != "") {
            formattedDate = stagedReparaturChanges.eingangsdatum
        }

        Text(
            text = "Eingangsdatum $formattedDate",
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.Bold
        )

        //Gesamtreps eine Kopie von StagedChanges
        //Liste von an Stagedchanges übergebenes Objekt einlesen
        val gesamtreps = mutableStateListOf<Reparatur>()
        stagedReparaturChanges.gesamtreps.forEach {
            gesamtreps.add(it)
        }

        Spacer(modifier = Modifier.size(20.dp))

        Button(onClick = {
            navController.navigate("auswahl")
        }) {
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = "zum Auswahlscreen",
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(text = "Zum Auswahl Screen")
        }

        Spacer(modifier = Modifier.size(20.dp))

        LazyColumn(
            modifier = Modifier.height(300.dp),
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
                if (it.reparatur_kategorie != "Extras") {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //Bezeichnung
                        Text(
                            text = "${it.reparatur_kategorie} : ${it.reparatur_name}",
                            modifier = Modifier.weight(4f),
                            color = MaterialTheme.colorScheme.background
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
                            modifier = Modifier.weight(2f),
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }

            items(gesamtreps) {
                if (it.reparatur_kategorie == "Extras") {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //Bezeichnung
                        Text(
                            text = "${it.reparatur_kategorie} : ${it.reparatur_name}",
                            modifier = Modifier.weight(4f),
                            color = MaterialTheme.colorScheme.background
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
                            modifier = Modifier.weight(2f),
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text = "Extras: ", color = MaterialTheme.colorScheme.background
        )

        var extrastext by remember { mutableStateOf("") }
        var extrasnum by remember { mutableStateOf("") }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = extrastext,
                onValueChange = {
                    extrastext = it
                },
                label = { Text("Extras") },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.background),
                modifier = Modifier.weight(2f)
            )
            OutlinedTextField(
                value = extrasnum,
                onValueChange = {
                    extrasnum = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Aufpreis") },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.background),
            )
            Spacer(modifier = Modifier.size(1.dp))
            Button(onClick = {
                if ((extrastext != "") && (extrasnum.toFloatOrNull() != null)) {

                    stagedReparaturChanges.gesamtreps.add(
                        Reparatur(
                            reparatur_name = extrastext,
                            reparatur_kategorie = "Extras",
                            reparatur_preis = extrasnum.toFloat()
                        )
                    )
                    extrasnum = ""
                    extrastext = ""

                }
            }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "bestätigen")
            }
        }

        LazyColumn(
            modifier = Modifier.height(100.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(gesamtreps) {
                if (it.reparatur_kategorie == "Extras") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Bezeichnung
                        Text(
                            text = it.reparatur_name,
                            modifier = Modifier.weight(2f),
                            color = MaterialTheme.colorScheme.background
                        )
                        //Einzelpreis
                        Text(
                            text = "${"%.2f".format(it.reparatur_preis)}€",
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.background
                        )
                        //Edit
                        Button(onClick = {
                            extrastext = it.reparatur_name
                            extrasnum = "%.2f".format(Locale.US, it.reparatur_preis)

                            stagedReparaturChanges.gesamtreps.remove(it)
                            gesamtreps.remove(it)
                        }) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "editier")
                        }
                        //Löschen
                        Button(onClick = {
                            stagedReparaturChanges.gesamtreps.remove(it)
                            gesamtreps.remove(it)
                        }) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "weg")
                        }


                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(10.dp))
        //Gesamtpreis aufaddieren
        var gesamtpreis = 0.0F
        gesamtreps.forEach { rep -> gesamtpreis += (rep.reparatur_preis * rep.anzahl) }

        var notiztext by remember { mutableStateOf("") }
        if (stagedReparaturChanges.extrasachen != "") {
            notiztext = stagedReparaturChanges.extrasachen
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = notiztext,
            onValueChange = {
                notiztext = it
                stagedReparaturChanges.extrasachen = it
            },
            label = { Text("Notizen") },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.background)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {

            if (nameinput != "") {
                if (numberinput != "") {
                    if (gesamtreps.isNotEmpty()) {
                        //Wegen OnConflictStrategy.REPLACE sollte das hier bei gleicher ID den Kunden ersetzen
                        kundeViewModel.insert(
                            Kunde(
                                id = kundenid,
                                name = nameinput,
                                gesPreis = gesamtpreis,
                                telNummer = numberinput,
                                status = "eingegangen",
                                reparaturliste = gesamtreps,
                                extras = notiztext,
                                eingansdatum = formattedDate
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
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Kunde hinzufügen",
                modifier = Modifier.size(40.dp)
            )
            Text(text = "Auftrag Hinzufügen")
        }

    }
}