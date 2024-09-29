package com.example.myapplication.kategorie

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Kategorie::class], version = 1)
@TypeConverters(KategorieConverter::class)
abstract class KategorieDatabase : RoomDatabase() {

    abstract fun kategorieDao(): KategorieDao

    companion object {

        @Volatile
        private var INSTANCE: KategorieDatabase? = null


        fun getDatabase(context: Context): KategorieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KategorieDatabase::class.java,
                    "kategorie_database.db"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }


}

