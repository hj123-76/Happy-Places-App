package com.example.happyplacesapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "happy_places")
data class HappyPlace(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val note: String,
    val latitude: Double,
    val longitude: Double,
    val imageUri: String? = null
)