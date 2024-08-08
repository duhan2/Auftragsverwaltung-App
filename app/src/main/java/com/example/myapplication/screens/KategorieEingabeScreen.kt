package com.example.myapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.kategorie.Kategorie
import com.example.myapplication.kategorie.KategorieChanges
import com.example.myapplication.kategorie.KategorieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategorieEingabeScreen(
    kategorieViewModel: KategorieViewModel,
    navController: NavController,
    kategorieChanges: KategorieChanges,
) {
    val query = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()            // on below line we are adding a padding.
        .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        TextField(value = query.value, singleLine = true, onValueChange = { query.value = it })
        Button(onClick = {
            if (query.value != "") {
                kategorieViewModel.insert(
                    Kategorie(
                        query.value,
                        reparaturliste = mutableListOf()
                    )
                )
                //Einsetzen der Kategorie in Changes
                kategorieChanges.kategoriename = query.value

                navController.navigate("reparatureingabe")
            }
        }) {
            Text(text = "Bestätigen")
        }
    }


}