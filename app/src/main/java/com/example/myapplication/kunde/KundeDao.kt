package com.example.myapplication.kunde

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KundeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKunde(kunde: Kunde) : Long

    @Delete
    suspend fun removeKunde(kunde: Kunde) : Int

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateKunde(kunde: Kunde): Int

    @Query("SELECT * FROM kunden_tabelle")
    fun getAllKunden() : Flow<List<Kunde>>

}