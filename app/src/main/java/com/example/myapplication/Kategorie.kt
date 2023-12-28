package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "kategorie_tabelle")
data class Kategorie(
    @PrimaryKey
    @ColumnInfo(name = "Kategorie-Bezeichnung")
    var kategorie_name: String = "leer",
    @ColumnInfo(name = "Reparaturen")
    var reparaturliste: MutableList<Reparatur>
)


public class KategorieConverter {

    @TypeConverter
    fun listToJson(value: MutableList<Reparatur>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value:String) = Gson().fromJson(value,Array<Reparatur>::class.java).toMutableList()
}