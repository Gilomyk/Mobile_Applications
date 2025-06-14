package com.example.mobile_application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.Movie
import com.example.mobile_application.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(application: Application): AndroidViewModel(application){
    private val repository = MovieRepository(application)

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun fetchMovies(title: String?) {
        viewModelScope.launch {
            val result = repository.fetchMovies(title)
            _movies.value = result ?: emptyList()
        }
    }

    fun fetchMoviesByCinema(cinemaId: Int) {
        viewModelScope.launch {
            val result = repository.fetchMoviesByCinema(cinemaId)
            _movies.value = result ?: emptyList()
        }
    }
}