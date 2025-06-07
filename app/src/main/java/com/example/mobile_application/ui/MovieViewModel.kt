package com.example.mobile_application.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import com.example.mobile_application.model.Movie
import com.example.mobile_application.repository.MovieRepository

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun fetchMovies(title: String?) {
        viewModelScope.launch {
            val result = repository.fetchMovies(title)
            _movies.value = result ?: emptyList()
        }
    }
}

