package com.example.happyplacesapp

class HappyPlaceRepository(private val dao: HappyPlaceDao) {
    val allPlaces = dao.getAll()
    suspend fun insert(place: HappyPlace) = dao.insert(place)
    suspend fun delete(place: HappyPlace) = dao.delete(place)
}