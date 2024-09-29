package com.example.myapplication.archiv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.myapplication.kunde.Kunde
import com.google.gson.Gson

@Entity(tableName = "archiv_tabelle")
data class Archiv(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1,

    @ColumnInfo(name = "Name")
    var name: String = "leer",

    @ColumnInfo(name = "Telefonnummer")
    var telNummer: String = "",

    @ColumnInfo(name = "Auftragsliste")
    var auftragsliste: MutableList<Kunde>
)

//Gucken ob das ausreichend ist
class AuftragsConverter {
    @TypeConverter
    fun listToJson(value: MutableList<Kunde>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Kunde>::class.java).toMutableList()
}