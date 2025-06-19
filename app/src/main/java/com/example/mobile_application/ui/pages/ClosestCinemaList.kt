package com.example.mobile_application.ui.pages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.R
import com.example.mobile_application.ui.theme.LocalAppColors
import com.example.mobile_application.viewmodel.CinemaViewModel
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClosestCinemaList(
    viewModel: CinemaViewModel = viewModel(),
    onCinemaClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val appColors = LocalAppColors.current
    var permissionGranted by remember { mutableStateOf(false) }
    val cinemaList by viewModel.cinemas.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> permissionGranted = granted }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            permissionGranted = true
        }
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            requestSingleLocationUpdate(context) { location ->
                if (location != null) {
                    viewModel.fetchClosestCinemas(location.latitude, location.longitude)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(appColors.background)) {
        if (!permissionGranted) {
            Text(
                text = stringResource(R.string.location_permission_denied),
                color = appColors.text,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.nearest_cinemas),
                    style = MaterialTheme.typography.titleLarge,
                    color = appColors.text,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cinemaList) { cinema ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCinemaClick(cinema.id) }
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = cinema.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = appColors.text
                                )
                                Text(
                                    text = cinema.location_city,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = appColors.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun requestSingleLocationUpdate(
    context: android.content.Context,
    onLocationReceived: (android.location.Location?) -> Unit
) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        1000L
    ).apply {
        setMaxUpdates(1)
    }.build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            onLocationReceived(result.lastLocation)
            fusedClient.removeLocationUpdates(this)
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            if (!availability.isLocationAvailable) {
                onLocationReceived(null)
                fusedClient.removeLocationUpdates(this)
            }
        }
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    } else {
        onLocationReceived(null)
    }
}