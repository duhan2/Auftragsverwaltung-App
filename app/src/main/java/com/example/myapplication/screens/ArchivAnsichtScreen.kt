package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.reparatur.ReparaturChanges


@Composable
fun ArchivAnsichtScreen(reparaturChanges: ReparaturChanges) {
    //Daten werden vorher in Zwischenspeicher reparaturChanges gespeichert
    //und hier wieder abgefragt

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
                    text = reparaturChanges.auftragsstatus,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item {
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
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
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
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
            Row {
                Text(
                    modifier = Modifier.weight(2f),
                    text = "Extras: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface

                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Aufpreis: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }
        }

        items(reparaturChanges.gesamtreps) {
            if (it.reparatur_kategorie == "Extras") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                style = MaterialTheme.typography.titleMedium,
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
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = reparaturChanges.extrasachen, color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

    }
}