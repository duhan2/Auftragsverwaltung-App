package com.example.myapplication.archiv

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Archiv::class], version = 1
)
@TypeConverters(AuftragsConverter::class)
abstract class ArchivDatabase : RoomDatabase() {

    abstract fun archivDao(): ArchivDao

    companion object {
        @Volatile
        private var INSTANCE: ArchivDatabase? = null

        fun getDatabase(
            context: Context
        ): ArchivDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArchivDatabase::class.java,
                    "archiv_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}