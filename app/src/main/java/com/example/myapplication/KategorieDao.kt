package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface KategorieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKategorie(kategorie: Kategorie) : Long

    @Delete
    suspend fun removeKategorie(kategorie: Kategorie) : Int

    @Update (onConflict = OnConflictStrategy.ABORT)
    suspend fun updateKategorie(kategorie: Kategorie) : Int

    @Query("SELECT * FROM kategorie_tabelle")
    fun getAllKategorien(): Flow<List<Kategorie>>
}