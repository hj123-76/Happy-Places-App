package com.example.happyplacesapp

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HappyPlacesScreen(viewModel: HappyPlaceViewModel) {
    val places by viewModel.places.collectAsState()
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<HappyPlace?>(null) }


    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Happy Places") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Neuen Ort hinzufügen")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { ctx ->
                    val mapView = MapView(ctx)
                    mapView.setTileSource(TileSourceFactory.MAPNIK)
                    mapView.setMultiTouchControls(true)
                    mapView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        600
                    )

                    val defaultPoint = if (selectedPlace != null) {
                        GeoPoint(selectedPlace!!.latitude, selectedPlace!!.longitude)
                    } else if (places.isNotEmpty()) {
                        GeoPoint(places.first().latitude, places.first().longitude)
                    } else {
                        GeoPoint(52.52, 13.405)
                    }

                    mapView.controller.setZoom(12.0)
                    mapView.controller.setCenter(defaultPoint)

                    places.forEach { place ->
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(place.latitude, place.longitude)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = place.name
                        mapView.overlays.add(marker)
                    }
                    mapView
                },
                update = { mapView ->
                    mapView.overlays.clear()
                    val defaultPoint = if (places.isNotEmpty()) {
                        GeoPoint(places.first().latitude, places.first().longitude)
                    } else {
                        GeoPoint(52.52, 13.405)
                    }
                    mapView.controller.setCenter(defaultPoint)
                    mapView.controller.setZoom(12.0)
                    places.forEach { place ->
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(place.latitude, place.longitude)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = place.name
                        mapView.overlays.add(marker)
                    }
                    mapView.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (places.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Keine Einträge vorhanden!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(places) { place ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPlace = place }, // Hier wird der Ort ausgewählt
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(text = place.name, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = place.note, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Lat: ${place.latitude}, Lon: ${place.longitude}",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Button(
                                        onClick = { viewModel.deletePlace(place) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Text("Löschen")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showAddDialog) {
                Dialog(onDismissRequest = { showAddDialog = false }) {
                    AddPlaceScreen(
                        onSave = { name, description ->
                            val newPlace = HappyPlace(name = name, note = description, latitude = 0.0, longitude = 0.0)
                            viewModel.addPlace(newPlace)
                            selectedPlace = newPlace
                            showAddDialog = false
                        },
                        onCancel = { showAddDialog = false }
                    )
                }
            }
        }
    }
}
