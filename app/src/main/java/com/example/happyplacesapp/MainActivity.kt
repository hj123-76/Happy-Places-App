package com.example.happyplacesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "happy_places.db").build()
        val repo = HappyPlaceRepository(db.happyPlaceDao())
        setContent {
            val viewModel: HappyPlaceViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return HappyPlaceViewModel(repo) as T
                }
            })
            HappyPlacesScreen(viewModel = viewModel)
        }
    }
}