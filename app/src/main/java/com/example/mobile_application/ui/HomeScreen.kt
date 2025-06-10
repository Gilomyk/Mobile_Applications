package com.example.mobile_application.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.model.Movie
import com.example.mobile_application.viewmodel.MovieViewModel

@Composable
fun HomeScreen(viewModel: MovieViewModel = viewModel(), onMovieClick: (Movie) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    // Automatycznie fetchujemy filmy przy pierwszym renderze lub zmianie query
    LaunchedEffect(searchQuery) {
        viewModel.fetchMovies(searchQuery.ifBlank { null })
    }

    val movies by viewModel.movies.observeAsState() // lub .observeAsState() jeśli używasz LiveData

    Column(modifier = Modifier.fillMaxSize()) {
        SearchHeader(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                viewModel.fetchMovies(searchQuery.ifBlank { null })
            }
        )

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
                MovieCard(movie = movie, onClick = { onMovieClick(movie) })
            }
        }


    }
}


