package com.example.myapplication

import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.remember

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
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp)
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

        val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate: String = localDate.format(pattern) //17-02-2022

        Text(text = "Eingangsdatum $formattedDate")

        //TODO Überprüfen wie das formatiert sein muss

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


        //Gesamtreps eine Kopie von StagedChanges
        //Liste von an Stagedchanges übergebenes Objekt einlesen
        val gesamtreps = mutableListOf<Reparatur>()
        stagedReparaturChanges.gesamtreps.forEach {
            gesamtreps.add(it)
        }

        Button(onClick = {
            navController.navigate("auswahl")
        }) {
            Text(text = "Zum Auswahl Screen")
        }

        gesamtreps.forEach {
            Text(text = "- ${it.anzahl} x ${it.reparatur_kategorie} : ${it.reparatur_name}")
        }
        var gesamtpreis = 0.0F
        gesamtreps.forEach { rep -> gesamtpreis += (rep.reparatur_preis * rep.anzahl) }

        Button(onClick = {

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
                                gesamtreps
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
            Text(text = "Hinzufügen")
        }

    }
}