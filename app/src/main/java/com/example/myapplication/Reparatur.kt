package com.example.myapplication


data class Reparatur(
    var id: Int = -1,
    var reparatur_name: String = "leer",
    var reparatur_kategorie: String = "leer",
    var reparatur_preis: Float = 0.00F,
    var anzahl: Int = 1
)
