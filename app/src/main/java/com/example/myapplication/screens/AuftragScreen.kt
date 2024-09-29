package com.example.myapplication.screens

import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.archiv.Archiv
import com.example.myapplication.archiv.ArchivViewModel
import com.example.myapplication.kunde.KundeViewModel
import com.example.myapplication.reparatur.ReparaturChanges

@RequiresApi(34)
@Composable
fun AuftragScreen(
    navController: NavController,
    reparaturChanges: ReparaturChanges,
    kundeViewModel: KundeViewModel,
    archivViewModel: ArchivViewModel,
    archivList: State<List<Archiv>>
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
    ) {
        item {
            Row {
                Text(
                    text = "Kunden ID: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${reparaturChanges.kundenid}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item {
            Row {
                Text(
                    text = "Name: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = reparaturChanges.nameinput,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item {
            Row {
                Text(
                    text = "Nummer: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = reparaturChanges.numberinput,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item {
            Row {
                Text(
                    text = "Eingangsdatum: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = reparaturChanges.eingangsdatum,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        item {

            Row {
                Text(
                    text = "Auftragsstatus: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = status,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item {
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    text = "Bezeichnung",
                    modifier = Modifier.weight(4f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Anzahl",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Einzelpreis",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Gesamtpreis",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        items(reparaturChanges.gesamtreps) {
            if (it.reparatur_kategorie != "Extras") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                ) {
                    //Bezeichnung
                    Text(
                        text = "${it.reparatur_kategorie} : ${it.reparatur_name}",
                        modifier = Modifier.weight(4f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    //Anzahl
                    Text(
                        text = "${it.anzahl}",
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    //Einzelpreis
                    Text(
                        text = "${"%.2f".format(it.reparatur_preis)}€",
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    //Gesamtpreis
                    Text(
                        text = "${"%.2f".format(it.reparatur_preis * it.anzahl)}€",
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Extras: ",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "Aufpreis: ",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

            }
        }

        items(reparaturChanges.gesamtreps) {
            if (it.reparatur_kategorie == "Extras") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(horizontal = 16.dp)
                ) {
                    //Bezeichnung
                    Text(
                        text = it.reparatur_name,
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    //Einzelpreis
                    Text(
                        text = "${"%.2f".format(it.reparatur_preis)}€",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
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
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // on below line we are adding a spacer.
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(
                text = "Notiz: ",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = reparaturChanges.extrasachen,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }

        item {
            var openAlertDialog1 by remember { mutableStateOf(false) }
            var openAlertDialog2 by remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {

                OutlinedButton(modifier = Modifier.weight(2f),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = Color.Gray,
                        disabledContainerColor = Color.DarkGray
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
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
                                    "Hallo Herr/Frau " + reparaturChanges.nameinput + ",\nIhr Fahrrad ist zur Reparatur eingegangen." + "\n\nMit freundlichen Grüßen\n" + "Fahrradwelt Fischeln"
                                openAlertDialog1 = true
                            } else {
                                Toast.makeText(
                                    context, "Auftrag bereits abgeschlossen", Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }) {
                    Text(
                        // on below line adding a text ,
                        // padding, color and font size.
                        text = "Eingangsbestätigung senden",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center
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
                                            phoneNumber, null, parts, null, null
                                        )
                                        Toast.makeText(
                                            context, "Message Sent", Toast.LENGTH_LONG
                                        ).show()
                                        openAlertDialog1 = false

                                    } catch (e: Exception) {
                                        // on below line handling error and
                                        // displaying toast message.
                                        Toast.makeText(
                                            context, "Error : " + e.message, Toast.LENGTH_LONG
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

                Spacer(modifier = Modifier
                    .width(10.dp)
                    .weight(1f))

                // on below line adding a button to send SMS
                OutlinedButton(
                    modifier = Modifier.weight(2f),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = Color.Gray,
                        disabledContainerColor = Color.DarkGray
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
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
                                    ) + " €. Bitte holen Sie Ihr Fahrrad, innerhalb unserer Öffnungszeiten ab" + "\nMit freundlichen Grüßen,\n" + "Fahrradwelt Fischeln"
                                //Altert Dialog öffnen
                                openAlertDialog2 = true
                            } else {
                                Toast.makeText(
                                    context, "Auftrag bereits abgeschlossen", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }) {
                    // on below line creating a text for our button.
                    Text(
                        // on below line adding a text ,
                        // padding, color and font size.
                        text = "Auftrag abschließen",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
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
                                            phoneNumber, null, parts, null, null
                                        )
                                        // on below line displaying
                                        // toast message as sms send.
                                        Toast.makeText(
                                            context, "Message Sent", Toast.LENGTH_LONG
                                        ).show()
                                        openAlertDialog2 = false
                                        reparaturChanges.auftragsstatus = "abgeschlossen"
                                        status = "abgeschlossen"
                                        kundeViewModel.update(reparaturChanges.createKundenobj())

                                        //Wenn Archiveintrag besteht
                                        if (reparaturChanges.archivid != -1) {
                                            val temp =
                                                archivList.value.find { it.id == reparaturChanges.archivid }
                                            if (temp != null) {
                                                //setz den neuen Auftrag in die Liste und aktualisiere das Archiv
                                                temp.auftragsliste.add(reparaturChanges.createKundenobj())
                                                archivViewModel.update(temp)
                                            }

                                        }
                                        //Wenn der Eintrag noch nicht besteht
                                        else {
                                            //Erschaffe nächstgroße ID
                                            val temp = archivList.value.maxByOrNull { it.id }
                                            var archivid = 1
                                            if (temp != null) {
                                                archivid = temp.id + 1
                                            }
                                            //Erschaffe folgenden Archiveintrag mit diesem Auftrag zur Liste hinzugefügt
                                            archivViewModel.insert(
                                                Archiv(
                                                    archivid,
                                                    reparaturChanges.nameinput,
                                                    reparaturChanges.numberinput,
                                                    mutableListOf(reparaturChanges.createKundenobj())
                                                )
                                            )

                                        }


                                        //navController.navigate("home")
                                    } catch (e: Exception) {
                                        // on below line handling error and
                                        // displaying toast message.
                                        Toast.makeText(
                                            context, "Error : " + e.message, Toast.LENGTH_LONG
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
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
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
                openAlertDialog3 -> AlertDialog(
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
