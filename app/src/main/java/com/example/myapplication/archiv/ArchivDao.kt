package com.example.myapplication.archiv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ArchivDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArchiv(archiv: Archiv) : Long

    @Delete
    suspend fun removeArchiv(archiv: Archiv) : Int

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateArchiv(archiv: Archiv): Int

    @Query("SELECT * FROM archiv_tabelle")
    fun getAllArchiv() : Flow<List<Archiv>>
}