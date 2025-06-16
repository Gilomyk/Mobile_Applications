package com.example.mobile_application.ui.pages

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.mobile_application.model.Movie


import com.example.mobile_application.viewmodel.QRScannerView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson

@Composable
fun QRCodeScreen(onScanned: (Int) -> Unit = {}) {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>(null) }
    val gson = remember { Gson() }
    var showError by remember { mutableStateOf(false) }

    if (scannedValue == null) {
        QRScannerView { code ->
            scannedValue = code
        }
    } else {
        val qrData: Movie? = try {
            gson.fromJson(scannedValue, Movie::class.java)
        } catch (e: Exception) {
            Log.e("QRScanner", "Error in JSON parse", e)
            null
        }

        qrData?.let { data ->
            onScanned(data.id)
        } ?: run {
            showError = true
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
            Toast.makeText(
                context,
                "Nieprawidłowy kod QR",
                Toast.LENGTH_SHORT
            ).show()
            // Resetujemy wartość, żeby umożliwić ponowne skanowanie
            scannedValue = null
            showError = false
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScreenWithPermission(onScanned: (Int) -> Unit = {}) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    if (cameraPermissionState.status.isGranted) {
        QRCodeScreen(onScanned = onScanned)
    } else {
        Text("Potrzebne uprawnienie do aparatu.")
    }
}