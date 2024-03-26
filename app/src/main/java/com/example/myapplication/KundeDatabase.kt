package com.example.myapplication

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Kunde::class], version = 2,
    //autoMigrations = [AutoMigration(from = 1, to = 2)]
)
@TypeConverters(KundeConverter::class, DatumConverter::class)
abstract class KundeDatabase : RoomDatabase() {

    abstract fun kundenDao(): KundeDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: KundeDatabase? = null

        fun getDatabase(
            context: Context
        ): KundeDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KundeDatabase::class.java,
                    "kunde_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}