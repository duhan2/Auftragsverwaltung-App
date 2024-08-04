package com.example.myapplication

import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(34)
@Composable
fun AuftragScreen(
    navController: NavController,
    reparaturChanges: ReparaturChanges,
    kundeViewModel: KundeViewModel
) {

    //Daten werden vorher in Zwischenspeicher reparaturChanges gespeichert
    //und hier wieder abgefragt

    val phoneNumber: String = reparaturChanges.numberinput
    var message = ""
    val context = LocalContext.current

    //Eventuell muss das raus wenn Probleme mit aktualisieren bei scroll
    var status by remember {
        mutableStateOf(reparaturChanges.auftragsstatus)
    }
    // on below line we are creating a column,
    LazyColumn(
        // on below line we are adding a modifier to it,
        modifier = Modifier
            .fillMaxSize()
            // on below line we are adding a padding.
            .padding(all = 30.dp),
        //horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        item {
            Row {
                Text(
                    text = "Kunden ID: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
                )
                Text(
                    text = "${reparaturChanges.kundenid}",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
        item {
            Row {
                Text(
                    text = "Name: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )
                Text(
                    text = reparaturChanges.nameinput,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
        item {
            Row {
                Text(
                    text = "Nummer: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
                )
                Text(
                    text = reparaturChanges.numberinput,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
        item {
            Row {
                Text(
                    text = "Eingangsdatum: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
                )
                Text(
                    text = reparaturChanges.eingangsdatum,
                    modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.background
                )
            }
        }

        item {

            Row {
                Text(
                    text = "Auftragsstatus: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
                )
                Text(
                    text = status,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
        item {
            HorizontalDivider(thickness = 1.dp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(20.dp))
        }
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
        items(reparaturChanges.gesamtreps) {
            if (it.reparatur_kategorie != "Extras") {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //Bezeichnung
                    Text(
                        text = "${it.reparatur_kategorie} : ${it.reparatur_name}",
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
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            }
        }

        item {
            Row {
                Text(
                    text = "Extras: ",
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "Aufpreis: ",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.weight(1f)
                )

            }
        }

        items(reparaturChanges.gesamtreps) {
            if (it.reparatur_kategorie == "Extras") {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //Bezeichnung
                    Text(
                        text = it.reparatur_name,
                        modifier = Modifier.weight(2f), color = MaterialTheme.colorScheme.background
                    )
                    //Einzelpreis
                    Text(
                        text = "${"%.2f".format(it.reparatur_preis)}€",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.background
                    )

                }
            }

        }

        item {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Summe = ${"%.2f".format(reparaturChanges.gesamtpreis)}€",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background
            )
        }

        // on below line we are adding a spacer.
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(
                text = "Notiz: ",
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = reparaturChanges.extrasachen,
                color = MaterialTheme.colorScheme.background
            )
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }

        item {
            var openAlertDialog1 by remember { mutableStateOf(false) }
            var openAlertDialog2 by remember { mutableStateOf(false) }

            Row {

                Button(
                    modifier = Modifier.weight(1f),
                    shape = CutCornerShape(10),
                    border = BorderStroke(1.dp, Color.Black),
                    onClick = {
                        if (reparaturChanges.numberinput.startsWith("02151")) {
                            Toast.makeText(
                                context,
                                "Kann keine SMS an Festnetznummer versenden",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            if (reparaturChanges.auftragsstatus == "eingegangen") {
                                //Altert Dialog öffnen
                                message =
                                    "Hallo Herr/Frau " + reparaturChanges.nameinput + ",\nIhr Fahrrad ist zur Reparatur eingegangen." +
                                            "\n\nMit freundlichen Grüßen\n" +
                                            "Fahrradwelt Fischeln"
                                openAlertDialog1 = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Auftrag bereits abgeschlossen",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }) {
                    Text(
                        // on below line adding a text ,
                        // padding, color and font size.
                        text = "Eingangsbestätigung senden",
                        modifier = Modifier.padding(10.dp),
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    when {
                        openAlertDialog1 -> {

                            AlertDialog(
                                onDismissRequest = { openAlertDialog1 = false },
                                onConfirmation = {// on below line running a try catch block for sending sms.
                                    try {
                                        // on below line initializing sms manager.
                                        val smsManager: SmsManager =
                                            context.getSystemService(SmsManager::class.java)
                                        // on below line sending sms
                                        val parts = smsManager.divideMessage(message)
                                        smsManager.sendMultipartTextMessage(
                                            phoneNumber,
                                            null,
                                            parts,
                                            null,
                                            null
                                        )
                                        Toast.makeText(
                                            context,
                                            "Message Sent",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        openAlertDialog1 = false

                                    } catch (e: Exception) {
                                        // on below line handling error and
                                        // displaying toast message.
                                        Toast.makeText(
                                            context,
                                            "Error : " + e.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                dialogText = message,
                                dialogTitle = "Folgende SMS senden ?"
                            )
                        }
                    }
                }

                //Divider(thickness = 1.dp, color = Color.DarkGray)

                Spacer(modifier = Modifier.width(10.dp))

                // on below line adding a button to send SMS
                Button(
                    modifier = Modifier.weight(1f),
                    shape = CutCornerShape(10),
                    border = BorderStroke(1.dp, Color.Black),
                    onClick = {
                        //Hier kann man auch abfragen ob keine Nummer vorhanden ist, aber bis jetzt ist eine Nummer Pflicht
                        //Wenn Nummer mit Festnetznummer anfängt
                        if (reparaturChanges.numberinput.startsWith("02151")) {
                            reparaturChanges.auftragsstatus = "abgeschlossen"
                            status = "abgeschlossen"
                            kundeViewModel.update(reparaturChanges.createKundenobj())
                            Toast.makeText(
                                context,
                                "Kann keine SMS an Festnetznummer versenden",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            if (reparaturChanges.auftragsstatus == "eingegangen") {
                                message =
                                    "Hallo Herr/Frau " + reparaturChanges.nameinput + ",\nIhr Fahrrad ist abholbereit. Die Reparaturkosten betragen " + "%.2f".format(
                                        reparaturChanges.gesamtpreis
                                    ) + " €. Bitte holen Sie Ihr Fahrrad, innerhalb unserer Öffnungszeiten ab" +
                                            "\nMit freundlichen Grüßen,\n" +
                                            "Fahrradwelt Fischeln"
                                //Altert Dialog öffnen
                                openAlertDialog2 = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Auftrag bereits abgeschlossen",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }) {
                    // on below line creating a text for our button.
                    Text(
                        // on below line adding a text ,
                        // padding, color and font size.
                        text = "Auftrag abschließen",
                        modifier = Modifier.padding(10.dp),
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    when {
                        openAlertDialog2 -> {
                            AlertDialog(
                                onDismissRequest = { openAlertDialog2 = false },
                                onConfirmation = {// on below line running a try catch block for sending sms.
                                    try {
                                        // on below line initializing sms manager.
                                        val smsManager: SmsManager =
                                            context.getSystemService(SmsManager::class.java)
                                        // on below line sending sms
                                        val parts = smsManager.divideMessage(message)
                                        smsManager.sendMultipartTextMessage(
                                            phoneNumber,
                                            null,
                                            parts,
                                            null,
                                            null
                                        )
                                        // on below line displaying
                                        // toast message as sms send.
                                        Toast.makeText(
                                            context,
                                            "Message Sent",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        openAlertDialog2 = false
                                        reparaturChanges.auftragsstatus = "abgeschlossen"
                                        status = "abgeschlossen"
                                        kundeViewModel.update(reparaturChanges.createKundenobj())
                                        //navController.navigate("home")
                                    } catch (e: Exception) {
                                        // on below line handling error and
                                        // displaying toast message.
                                        Toast.makeText(
                                            context,
                                            "Error : " + e.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                dialogText = message,
                                dialogTitle = "Folgende SMS senden ?"
                            )
                        }
                    }
                }

            }
        }

        item {
            Spacer(modifier = Modifier.size(100.dp))
            var openAlertDialog3 by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        openAlertDialog3 = true
                    },
                    colors = ButtonColors(Color.Red, Color.White, Color.DarkGray, Color.Gray),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "abbrechen",
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            when {
                openAlertDialog3 ->
                    AlertDialog(
                        onDismissRequest = { openAlertDialog3 = false },
                        onConfirmation = {
                            kundeViewModel.delete(reparaturChanges.createKundenobj())
                            openAlertDialog3 = false
                            navController.navigate("home")
                        },
                        dialogText = "Auftrag löschen und zum Aufträge-Screen zurückkehren.",
                        dialogTitle = "Auftrag wirklich löschen ?"
                    )
            }
        }
    }
}