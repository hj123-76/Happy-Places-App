package com.example.happyplacesapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.happyplacesapp.HappyPlace
import com.example.happyplacesapp.HappyPlaceDao

@Database(entities = [HappyPlace::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun happyPlaceDao(): HappyPlaceDao
}