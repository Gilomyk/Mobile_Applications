package com.example.mobile_application.ui.pages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mobile_application.viewmodel.ClosestCinemaListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClosestCinemaList(
    viewModel: ClosestCinemaListViewModel = viewModel(),
    onClick: () -> Unit = {}) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    val cinemaList by viewModel.cinemas.collectAsState()

    //Permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            permissionGranted = true
        }
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
                fusedClient.lastLocation.addOnSuccessListener { location ->
                    Log.d("LOCATION", "lastLocation result: $location")
                    if (location != null) {
                        viewModel.fetchClosestCinemas(location.latitude, location.longitude)
                    } else {
                        Log.w("LOCATION", "Brak lokalizacji! Nie wywołano fetchClosestCinemas()")
                    }

                }
            }
        }
    }

    if (!permissionGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Log.d("LOCATION", "No permission")
            Text("Brak zgody na lokalizację")
        }
    } else {
        Log.d("LOCATION", "Starting loading")
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Najbliższe kina:",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cinemaList) { cinema ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = onClick)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(text = cinema.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = cinema.location_city, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}