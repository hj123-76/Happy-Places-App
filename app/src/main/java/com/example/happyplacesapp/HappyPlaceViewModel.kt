package com.example.happyplacesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HappyPlaceViewModel(private val repo: HappyPlaceRepository) : ViewModel() {

    val places = repo.allPlaces.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addPlace(place: HappyPlace) = viewModelScope.launch {
        repo.insert(place)
    }

    fun deletePlace(place: HappyPlace) = viewModelScope.launch {
        repo.delete(place)
    }

    fun savePlace(name: String, description: String) {
        val newPlace = HappyPlace(
            name = name,
            note = description,
            latitude = 0.0,
            longitude = 0.0
        )
        addPlace(newPlace)
    }
}