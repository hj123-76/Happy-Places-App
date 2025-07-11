package com.example.happyplacesapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HappyPlaceDao {
    @Query("SELECT * FROM happy_places")
    fun getAll(): Flow<List<HappyPlace>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(place: HappyPlace)

    @Delete
    suspend fun delete(place: HappyPlace)
}