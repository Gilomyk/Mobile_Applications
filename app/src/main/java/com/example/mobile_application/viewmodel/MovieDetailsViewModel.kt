package com.example.mobile_application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.MovieCrew
import com.example.mobile_application.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> = _movie

    private val _crew = MutableLiveData<MovieCrew?>()
    val crew: LiveData<MovieCrew?> = _crew

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val movieResult = repository.fetchMovieById(movieId)
            _movie.value = movieResult

            val crewResult = repository.fetchMovieCrewById(movieId)
            _crew.value = crewResult
        }
    }
}