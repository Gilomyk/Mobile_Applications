package com.example.mobile_application.ui.pages


import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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

    val scanBoxSize = 250.dp
    val scanBoxSizePx = with(LocalDensity.current) { scanBoxSize.toPx() }

    // Animacja przesuwania linii od 0 do scanBoxSizePx i z powrotem (loop)
    val infiniteTransition = rememberInfiniteTransition()
    val lineOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = scanBoxSizePx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        if (scannedValue == null) {
            QRScannerView { code -> scannedValue = code }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .size(scanBoxSize)
                    .align(Alignment.Center)
                    .border(3.dp, Color.White, RoundedCornerShape(8.dp))
            ) {
                // Animowana linia
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .offset { IntOffset(0, lineOffsetY.toInt()) }
                        .background(Color.Cyan.copy(alpha = 0.7f))
                )
            }

            Text(
                text = "Zeskanuj kod QR",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    scannedValue?.let {
        val qrData: Movie? = try {
            gson.fromJson(it, Movie::class.java)
        } catch (e: Exception) {
            Log.e("QRScanner", "Error in JSON parse", e)
            null
        }

        qrData?.let { data -> onScanned(data.id) } ?: run { showError = true }
    }

    LaunchedEffect(showError) {
        if (showError) {
            Toast.makeText(context, "Nieprawidłowy kod QR", Toast.LENGTH_SHORT).show()
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