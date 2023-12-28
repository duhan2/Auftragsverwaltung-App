package com.example.myapplication

import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(34)
@Composable
fun AuftragScreen(navController: NavController, reparaturChanges: ReparaturChanges, kundeViewModel: KundeViewModel) {

    val phoneNumber: String = reparaturChanges.numberinput
    var message: String
    val context = LocalContext.current
    // on below line we are creating a column,
    Column(
        // on below line we are adding a modifier to it,
        modifier = Modifier
            .fillMaxSize()
            // on below line we are adding a padding.
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {

        Text(text = "Kunden ID: ${reparaturChanges.kundenid}")
        Text(text = "Name: ${reparaturChanges.nameinput}")
        Text(text = "Nummer: ${reparaturChanges.numberinput}")
        Text(text = "Eingangsdatum: ${reparaturChanges.localDate}")

        var status by remember {
            mutableStateOf(reparaturChanges.auftragsstatus)
        }
        Text(text = "Auftragsstatus: $status")

        //println("Auftragsstatus: ${reparaturChanges.auftragsstatus}")

        Spacer(modifier = Modifier.height(20.dp))

        reparaturChanges.gesamtreps.forEach {
            Text(text = "- ${it.anzahl} x ${it.reparatur_kategorie} : ${it.reparatur_name}")
        }
        Text(text = "Gesamtpreis = ${"%.2f".format(reparaturChanges.gesamtpreis)}€")

        message =
            "Hallo Herr/Frau " + reparaturChanges.nameinput + ",\nIhr Fahrrad ist abholbereit. Die Reperaturkosten betragen " + "%.2f".format(
                reparaturChanges.gesamtpreis
            ) + " €.\nMit freundlichen Grüßen\n" +
                    "Ihr Hobbymarkt Team"

        // on below line we are adding a spacer.
        Spacer(modifier = Modifier.height(20.dp))

        val openAlertDialog = remember { mutableStateOf(false) }

        // on below line adding a button to send SMS
        Button(onClick = {
            if (reparaturChanges.auftragsstatus == "eingegangen") {
                //Altert Dialog öffnen
                openAlertDialog.value = true
            }else{
                Toast.makeText(
                    context,
                    "Auftrag bereits abgeschlossen",
                    Toast.LENGTH_LONG
                ).show()
            }
        }) {
            // on below line creating a text for our button.
            Text(
                // on below line adding a text ,
                // padding, color and font size.
                text = "Send SMS",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 15.sp
            )
            when {
                openAlertDialog.value -> {
                    //println("Ich bin in Kategorie ${selectedOption.kategorie_name} ")
                    AlertDialog(
                        onDismissRequest = { openAlertDialog.value = false },
                        onConfirmation = {// on below line running a try catch block for sending sms.
                            try {
                                // on below line initializing sms manager.
                                val smsManager: SmsManager =
                                    context.getSystemService(SmsManager::class.java)
                                // on below line sending sms
                                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                                // on below line displaying
                                // toast message as sms send.
                                Toast.makeText(
                                    context,
                                    "Message Sent",
                                    Toast.LENGTH_LONG
                                ).show()
                                openAlertDialog.value = false
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