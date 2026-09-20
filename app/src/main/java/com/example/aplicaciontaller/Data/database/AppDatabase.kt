package com.example.aplicaciontaller.Data.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.aplicaciontaller.Data.dao.CategoryDao
import com.example.aplicaciontaller.Data.model.Category

@Database(
    entities = [
        Category::class
    ],
    version = 1,
    exportSchema = false)
abstract class AppDatabase(): RoomDatabase(){
    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? =null
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "concesionaria_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also{
                        INSTANCE = it
                    }
            }
        }
    }
}