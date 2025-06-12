package com.example.mobile_application.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.Cinema
import com.example.mobile_application.repository.CinemaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClosestCinemaListViewModel: ViewModel(){
    private val cinemaRepository = CinemaRepository()

    private val _cinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val cinemas: StateFlow<List<Cinema>> = _cinemas

    fun fetchClosestCinemas(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _cinemas.value = cinemaRepository.fetchClosestCinemas(latitude, longitude) ?: emptyList()
            Log.d("LOCATION", "Location: ${latitude}, ${longitude}")
            Log.d("LOCATION", "Cinemas: ${_cinemas.value}")
        }
    }
}