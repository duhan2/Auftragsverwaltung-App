package com.example.myapplication.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.archiv.ArchivChanges
import com.example.myapplication.archiv.ArchivViewModel
import com.example.myapplication.reparatur.ReparaturChanges

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun KundenAnsichtScreen(
    navController: NavController,
    stagedReparaturChanges: ReparaturChanges,
    archivViewModel: ArchivViewModel,
    archivChanges: ArchivChanges
) {

    val archiv = archivChanges.createobj()

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
                    text = "Name: ",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = archiv.name,
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
                    text = archiv.telNummer,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(archiv.auftragsliste) {
            Row(modifier = Modifier
                .clickable {
                    stagedReparaturChanges.setfromKundeobj(it)
                    navController.navigate("archivansicht")
                }
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 8.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Datum: " + it.eingansdatum,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface

                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Preis: ${"%.2f".format(it.gesPreis)} €",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Spacer(modifier = Modifier.size(20.dp))
            var openAlertDialog by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        openAlertDialog = true
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
                openAlertDialog -> AlertDialog(
                    onDismissRequest = { openAlertDialog = false },
                    onConfirmation = {
                        archivViewModel.delete(archiv)
                        openAlertDialog = false
                        navController.navigate("archiv")
                    },
                    dialogText = "Kunden löschen und zum Kunden-Screen zurückkehren.",
                    dialogTitle = "Kunden wirklich löschen ?"
                )
            }
        }
    }

}