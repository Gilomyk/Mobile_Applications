package com.example.mobile_application.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.R
import com.example.mobile_application.network.ApiClient
import com.example.mobile_application.ui.components.MovieCard
import com.example.mobile_application.ui.components.SearchHeader
import com.example.mobile_application.viewmodel.MovieViewModel

@Composable
fun HomeScreen(
    viewModel: MovieViewModel = viewModel(),
    onMovieClick: (Int) -> Unit,
    onLocationClick: () -> Unit,
    onLoginClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQrClick: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var isLogged by remember { mutableStateOf(false) }

    val context = LocalContext.current

    //Coś do testów API
    LaunchedEffect(Unit) {
        try {
            val api = ApiClient.create(context).userService
            val response = api.getProfile()
            Log.d("API_TEST", "Response headers: ${response.headers()}")
            Log.d("API_TEST", "HTTP message: ${response.code()} - ${response.message()}")
            if (response.isSuccessful) {
                Log.d("API_TEST", "Response: ${response.body()}")
                isLogged = true
            } else {
                Log.e("API_TEST", "HTTP error: ${response.code()} - ${response.message()}")
                Log.e("API_TEST", "Response body: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API_TEST", "Exception: ${e.localizedMessage}")
            Log.e("API_TEST", "Stacktrace: ${Log.getStackTraceString(e)}")

        }
    }



    // Automatycznie fetchujemy filmy przy pierwszym renderze lub zmianie query
    LaunchedEffect(searchQuery) {
        viewModel.fetchMovies(searchQuery.ifBlank { null })
    }

    val movies by viewModel.movies.observeAsState() // lub .observeAsState() jeśli używasz LiveData

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SearchHeader(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        viewModel.fetchMovies(searchQuery.ifBlank { null })
                    }
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { onLocationClick() }) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Lokalizacja"
                )
            }

            if (isLogged) {
                IconButton(
                    onClick = {
                        onProfileClick() // Przejdź do profilu
                        Log.d("PROFILE_BUTTON", "Profile button clicked")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, // Ikona profilu
                        contentDescription = "Profil"
                    )
                }
            } else {
                TextButton(
                    onClick = {
                        onLoginClick()
                        Log.d("LOGIN_BUTTON", "Login button clicked")
                    }
                ) {
                    Text("Zaloguj się")
                }
            }

            IconButton(onClick = { onQrClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_qr_code),
                    contentDescription = "Skanuj kod QR",
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = movies.orEmpty(),
                span = { index, _ ->
                    if (index == 0) GridItemSpan(2) else GridItemSpan(1)
                }
                // dzięki Ci Span że używasz indeksów, bo prawie wywaliłem ten komputer za okno
            ) { index, movie ->
                MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
            }
        }


    }
}


