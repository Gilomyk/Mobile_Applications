package com.example.mobile_application.ui.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.R
import com.example.mobile_application.network.ApiClient
import com.example.mobile_application.repository.UserRepository
import com.example.mobile_application.ui.components.MovieCard
import com.example.mobile_application.ui.components.SearchHeader
import com.example.mobile_application.ui.theme.LocalAppColors
import com.example.mobile_application.viewmodel.MovieViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(
    viewModel: MovieViewModel = viewModel(),
    onMovieClick: (Int) -> Unit,
    onLocationClick: () -> Unit,
    onLoginClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQrClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val appColors = LocalAppColors.current
    var searchQuery by remember { mutableStateOf("") }
    var isLogged by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    LaunchedEffect(Unit) {
        try {
            // suspend-friendly pobranie tokena
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            Log.d("HomeScreen", "FCM Token: $fcmToken")

            userRepository.registerDevice(fcmToken)
            Log.d("HomeScreen", "Device registered successfully")
        } catch (e: Exception) {
            Log.e("HomeScreen", "Device registration failed: ${e.localizedMessage}")
        }
    }


    // Check login status
    LaunchedEffect(Unit) {
        try {
            val api = ApiClient.create(context).userService
            val response = api.getProfile()
            if (response.isSuccessful) {
                isLogged = true
            }
        } catch (_: Exception) { }
    }

    // Fetch movies
    LaunchedEffect(searchQuery) {
        viewModel.fetchMovies(searchQuery.ifBlank { null })
    }

    val movies by viewModel.movies.observeAsState()

    Column(modifier = Modifier.fillMaxSize().background(appColors.background)) {
        // Search Header
        SearchHeader(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { viewModel.fetchMovies(searchQuery.ifBlank { null }) }
        )

        // Top action icons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onLocationClick) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = appColors.icon)
                }
                if (isLogged) {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = appColors.icon)
                    }
                } else {
                    Button(
                        onClick = onLoginClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = appColors.primary,
                            contentColor = appColors.buttonText
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.login_button))
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onQrClick) {
                    Icon(painterResource(R.drawable.ic_qr_code), contentDescription = null, tint = appColors.icon)
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = appColors.icon)
                }
            }
        }

        // Movie Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(movies.orEmpty(), span = { index, _ -> if (index == 0) GridItemSpan(2) else GridItemSpan(1) }) { _, movie ->
                MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
            }
        }
    }
}


