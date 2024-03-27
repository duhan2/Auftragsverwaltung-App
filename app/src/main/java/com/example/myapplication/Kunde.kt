package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.time.LocalDate


@Entity(tableName = "kunden_tabelle")
data class Kunde (
    //Datenbank manuell löschen Wenn hier Änderungen im Aufbau der Tabelle vorgenommen werden

    @PrimaryKey(autoGenerate = true)
    var id: Int = -1,    //wenn bei -1 dann inkrementiert nicht:d

    @ColumnInfo(name = "Name")
    var name: String = "leer",

    @ColumnInfo(name = "Gesamtpreis")
    var gesPreis: Float = 0.00F,

    @ColumnInfo(name= "Auftragsdatum")
    var gebrachtam: LocalDate,

    @ColumnInfo(name = "Telefonnummer")
    var telNummer: String = "",

    @ColumnInfo(name = "Auftragsstatus")
    var status: String = "",

    //EVENTUELL DAS  MUTABLE
    @ColumnInfo(name = "Reparaturenliste")
    var reparaturliste: List<Reparatur>,

    @ColumnInfo(name = "Extras", defaultValue = "")
    var extras: String = "",

    /*@ColumnInfo(name = "Aufpreis", defaultValue = "0.00")
    var aufpreis: Float = 0.00F,
*/
)

class KundeConverter {

    @TypeConverter
    fun listToJson(value: List<Reparatur>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value:String) = Gson().fromJson(value,Array<Reparatur>::class.java).toList()
}

class DatumConverter{
    @TypeConverter
    fun timeToString(time:LocalDate): String{
        return time.toString()
    }

    @TypeConverter
    fun stringToTime(string: String): LocalDate {
        return LocalDate.parse(string)
    }
}