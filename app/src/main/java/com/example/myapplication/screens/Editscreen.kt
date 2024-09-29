package com.example.myapplication.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.archiv.Archiv
import com.example.myapplication.archiv.ArchivViewModel
import com.example.myapplication.kunde.Kunde
import com.example.myapplication.kunde.KundeViewModel
import com.example.myapplication.reparatur.Reparatur
import com.example.myapplication.reparatur.ReparaturChanges
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@RequiresApi(34)
@Composable
fun Editscreen(
    kundeViewModel: KundeViewModel,
    navController: NavController,
    archivliste: State<List<Archiv>>,
    stagedReparaturChanges: ReparaturChanges,
    archivViewModel: ArchivViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        val openAlertDialog = remember { mutableStateOf(false) }

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
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Kunden-ID: ",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$kundenid",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }


        var expanded by remember { mutableStateOf(false) }

        var nameinput by remember {
            mutableStateOf(
                TextFieldValue(
                    text = "", selection = TextRange(0)
                )
            )
        }

        //Wenn
        if (stagedReparaturChanges.nameinput != "") {
            nameinput = TextFieldValue(
                text = stagedReparaturChanges.nameinput,
                selection = TextRange(stagedReparaturChanges.nameinput.length)
            )
        }
        var numberinput by remember { mutableStateOf("") }
        if (stagedReparaturChanges.numberinput != "") {
            numberinput = stagedReparaturChanges.numberinput
        }

        var options by remember {
            mutableStateOf(emptyList<Archiv>())
        }

        val localFocusManager = LocalFocusManager.current

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
            stagedReparaturChanges.archivid = -1
            stagedReparaturChanges.nameinput = ""
            nameinput = TextFieldValue(
                text = "", selection = TextRange(0)
            )
        }) {
            Row {
                TextField(modifier = Modifier.menuAnchor(),
                    value = nameinput,
                    onValueChange = { textinput ->
                        nameinput = textinput
                        options = archivliste.value.filter {
                            (nameinput.text != "") && (it.name.contains(
                                nameinput.text, ignoreCase = true
                            ))
                        }
                        expanded = nameinput.text.isNotBlank()
                    },
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    //Eventuell das Keyboard noch schliessen
                    keyboardActions = KeyboardActions(onDone = {
                        stagedReparaturChanges.nameinput = nameinput.text
                        expanded = false
                        localFocusManager.clearFocus()
                    }),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = "search"
                        )
                    }

                )
            }

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = {
                                nameinput = TextFieldValue(
                                    text = it.name, selection = TextRange(it.name.length)
                                )
                                stagedReparaturChanges.nameinput = nameinput.text
                                stagedReparaturChanges.numberinput = it.telNummer
                                stagedReparaturChanges.archivid = it.id
                                localFocusManager.clearFocus()
                                expanded = false
                            })
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = it.name)
                    }
                }

            }
        }


        TextField(value = numberinput, onValueChange = {
            numberinput = it
            stagedReparaturChanges.numberinput = it
        }, singleLine = true, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ), keyboardActions = KeyboardActions(onDone = {
            stagedReparaturChanges.numberinput = numberinput
            localFocusManager.clearFocus()
        }), label = { Text(text = "Telefonnummer") })


        //prepopulate localDate
        val pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var formattedDate: String = LocalDate.now().format(pattern) //17.02.2022
        if (stagedReparaturChanges.eingangsdatum != "") {
            formattedDate = stagedReparaturChanges.eingangsdatum
        }

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Eingangsdatum: ",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formattedDate,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        //Gesamtreps eine Kopie von StagedChanges
        //Liste von an Stagedchanges übergebenes Objekt einlesen
        val gesamtreps = mutableStateListOf<Reparatur>()
        stagedReparaturChanges.gesamtreps.forEach {
            gesamtreps.add(it)
        }


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

        LazyColumn(
            modifier = Modifier.height(250.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = "Bezeichnung",
                        modifier = Modifier
                            .weight(4f)
                            .padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Anzahl",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Einzelpreis",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Gesamtpreis",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleSmall
                    )

                }
            }
            items(gesamtreps) {
                if (it.reparatur_kategorie != "Extras") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        //Bezeichnung
                        Text(
                            text = "${it.reparatur_kategorie} : ${it.reparatur_name}",
                            modifier = Modifier
                                .weight(4f)
                                .padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        //Anzahl
                        Text(
                            text = "${it.anzahl}",
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        //Einzelpreis
                        Text(
                            text = "${"%.2f".format(it.reparatur_preis)}€",
                            modifier = Modifier.weight(2f),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        //Gesamtpreis
                        Text(
                            text = "${"%.2f".format(it.reparatur_preis * it.anzahl)}€",
                            modifier = Modifier.weight(2f),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            items(gesamtreps) {
                if (it.reparatur_kategorie == "Extras") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        //Bezeichnung
                        Text(
                            text = "${it.reparatur_kategorie} : ${it.reparatur_name}",
                            modifier = Modifier
                                .weight(4f)
                                .padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        //Anzahl
                        Text(
                            text = "${it.anzahl}",
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        //Einzelpreis
                        Text(
                            text = "${"%.2f".format(it.reparatur_preis)}€",
                            modifier = Modifier.weight(2f),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        //Gesamtpreis
                        Text(
                            text = "${"%.2f".format(it.reparatur_preis * it.anzahl)}€",
                            modifier = Modifier.weight(2f),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

        }

        Text(
            text = "Extras: ",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
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
                modifier = Modifier.weight(2f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                )
            )
            OutlinedTextField(
                value = extrasnum,
                onValueChange = {
                    extrasnum = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                label = { Text(text = "Aufpreis") },
                modifier = Modifier.weight(1f),

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
            modifier = Modifier.height(100.dp)
        ) {
            items(gesamtreps) {
                if (it.reparatur_kategorie == "Extras") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Bezeichnung
                        Text(
                            text = it.reparatur_name,
                            modifier = Modifier
                                .weight(2f)
                                .padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        //Einzelpreis
                        Text(
                            text = "${"%.2f".format(it.reparatur_preis)}€",
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
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
                    HorizontalDivider(
                        thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }

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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            )
        )

        var status = "eingegangen"
        if (stagedReparaturChanges.auftragsstatus == "abgeschlossen") {
            status = "abgeschlossen"
        }

        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {

            if (nameinput.text != "") {
                if (numberinput != "") {
                    if (gesamtreps.isNotEmpty()) {
                        if (stagedReparaturChanges.archivid != -1) {
                            val temp =
                                archivliste.value.firstOrNull { it.id == stagedReparaturChanges.archivid }
                            if (temp != null) {

                                if (temp.telNummer != numberinput) {
                                    openAlertDialog.value = true
                                } else {
                                    kundeViewModel.insert(
                                        Kunde(
                                            id = kundenid,
                                            name = nameinput.text,
                                            gesPreis = gesamtpreis,
                                            telNummer = numberinput,
                                            status = status,
                                            reparaturliste = gesamtreps,
                                            extras = notiztext,
                                            eingansdatum = formattedDate,
                                            archivid = stagedReparaturChanges.archivid
                                        )
                                    )

                                    stagedReparaturChanges.resetchanges()
                                    navController.navigate("home")
                                }
                            } else {
                                val toast = Toast.makeText(
                                    context, "Kunden nicht im Archiv gefunden", Toast.LENGTH_SHORT
                                ) // in Activity
                                toast.show()
                            }
                        } else {                        //Wegen OnConflictStrategy.REPLACE sollte das hier bei gleicher ID den Kunden ersetzen
                            kundeViewModel.insert(
                                Kunde(
                                    id = kundenid,
                                    name = nameinput.text,
                                    gesPreis = gesamtpreis,
                                    telNummer = numberinput,
                                    status = status,
                                    reparaturliste = gesamtreps,
                                    extras = notiztext,
                                    eingansdatum = formattedDate,
                                    archivid = stagedReparaturChanges.archivid
                                )
                            )

                            stagedReparaturChanges.resetchanges()
                            navController.navigate("home")
                        }

                    } else {
                        val toast = Toast.makeText(
                            context, "Bitte Reparaturen auswählen", Toast.LENGTH_SHORT
                        ) // in Activity
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        context, "Bitte Nummer eingeben", Toast.LENGTH_SHORT
                    ) // in Activity
                    toast.show()
                }
            } else {
                val toast = Toast.makeText(
                    context, "Bitte Name eingeben", Toast.LENGTH_SHORT
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

        if (openAlertDialog.value) {
            androidx.compose.material3.AlertDialog(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                textContentColor = MaterialTheme.colorScheme.onSurface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                title = {
                    Text(
                        text = "Neue Nummer erkannt"
                    )
                },
                text = { Text(text = "Soll die Nummer für ${nameinput.text} aktualisiert werden") },
                onDismissRequest = { openAlertDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val temp =
                                archivliste.value.firstOrNull { it.id == stagedReparaturChanges.archivid }
                            if (temp != null) {
                                temp.telNummer = numberinput
                                archivViewModel.update(temp)
                            }

                            kundeViewModel.insert(
                                Kunde(
                                    id = kundenid,
                                    name = nameinput.text,
                                    gesPreis = gesamtpreis,
                                    telNummer = numberinput,
                                    status = status,
                                    reparaturliste = gesamtreps,
                                    extras = notiztext,
                                    eingansdatum = formattedDate,
                                    archivid = stagedReparaturChanges.archivid
                                )
                            )

                            stagedReparaturChanges.resetchanges()
                            openAlertDialog.value = false
                            navController.navigate("home")

                        }
                    ) {
                        Text("Bestätigen")
                    }
                }, dismissButton = {
                    TextButton(
                        onClick = {
                            openAlertDialog.value = false
                        }
                    ) {
                        Text("Abbrechen")
                    }
                }
            )

        }

    }

}