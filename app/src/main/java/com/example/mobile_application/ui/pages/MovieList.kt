package com.example.mobile_application.ui.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.ui.components.MovieCard
import com.example.mobile_application.viewmodel.CinemaViewModel
import com.example.mobile_application.viewmodel.MovieViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieList(
    cinemaId: Int,
    viewModel: MovieViewModel = viewModel(),
    cinemaViewModel: CinemaViewModel = viewModel(),
    onMovieClick: (Int) -> Unit = {}) {

    LaunchedEffect(cinemaId) {
        viewModel.fetchMoviesByCinema(cinemaId)
        cinemaViewModel.fetchCinema(cinemaId)
    }

    val movies by viewModel.movies.observeAsState()
    val cinema by cinemaViewModel.cinema.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        cinema?.let {
            Text(
                text = it.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            items(movies.orEmpty()) { movie ->
                MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
            }
        }
}}