package com.example.mobile_application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.Cinema
import com.example.mobile_application.repository.CinemaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CinemaViewModel(application: Application): AndroidViewModel(application){
    private val cinemaRepository = CinemaRepository(application)

    private val _cinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val cinemas: StateFlow<List<Cinema>> = _cinemas

    private val _cinema = MutableStateFlow<Cinema?>(null)
    val cinema: StateFlow<Cinema?> = _cinema

    fun fetchClosestCinemas(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _cinemas.value = cinemaRepository.fetchClosestCinemas(latitude, longitude) ?: emptyList()
            Log.d("LOCATION", "Location: ${latitude}, ${longitude}")
            Log.d("LOCATION", "Cinemas: ${_cinemas.value}")
        }
    }

    fun fetchCinema(cinemaId: Int) {
        viewModelScope.launch {
            _cinema.value = cinemaRepository.fetchCinema(cinemaId)
            Log.d("CINEMA", "Cinema: ${_cinema.value}")
        }
    }

    fun fetchCinemas() {
        viewModelScope.launch {
            _cinemas.value = cinemaRepository.fetchCinemas()!!
        }
    }
}