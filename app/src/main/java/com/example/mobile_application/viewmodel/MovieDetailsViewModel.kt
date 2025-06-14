package com.example.mobile_application.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.Cinema
import com.example.mobile_application.model.CinemaHall
import com.example.mobile_application.model.HallType
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.MovieCrew
import com.example.mobile_application.model.MovieShowing
import com.example.mobile_application.repository.CinemaRepository
import com.example.mobile_application.repository.MovieRepository
import com.example.mobile_application.repository.MovieShowingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MovieDetailsViewModel(application: Application): AndroidViewModel(application){
    private val movieRepository = MovieRepository(application)
    private val cinemaRepository = CinemaRepository(application)
    private val movieShowingRepository = MovieShowingRepository(application)

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie

    private val _crew = MutableStateFlow<MovieCrew?>(null)
    val crew: StateFlow<MovieCrew?> = _crew

    private val _cinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val cinemas: StateFlow<List<Cinema>> = _cinemas

    private val _hallTypes = MutableStateFlow<List<HallType>>(emptyList())
    val hallTypes: StateFlow<List<HallType>> = _hallTypes

    private val _cinemaHalls = MutableStateFlow<List<CinemaHall>>(emptyList())
    val cinemaHalls: StateFlow<List<CinemaHall>> = _cinemaHalls

    private val _showingsByDate = MutableStateFlow<Map<String, List<MovieShowing>>>(emptyMap())
    val showingsByDate: StateFlow<Map<String, List<MovieShowing>>> = _showingsByDate

    private val _loadingShowtimes = MutableStateFlow(false)
    val loadingShowtimes: StateFlow<Boolean> = _loadingShowtimes

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _movie.value = movieRepository.fetchMovieById(movieId)
            _crew.value = movieRepository.fetchMovieCrewById(movieId)
            _cinemas.value = cinemaRepository.fetchCinemas() ?: emptyList()
            _hallTypes.value = cinemaRepository.fetchHallTypes() ?: emptyList()
            _cinemaHalls.value = cinemaRepository.fetchCinemaHalls(null) ?: emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadShowingsForDate(movieId: Int, date: LocalDate, cinemaId: Int?=null) {
        val dateStr = date.toString()

        // Resetowanie danych przy zmianie kina lub wymuszenie załadowania nowych danych
        if (cinemaId != null) {
            // Możesz usunąć dane dla danego kina, żeby wymusić przeładowanie
            _showingsByDate.value = _showingsByDate.value.filterNot { it.key.startsWith(dateStr) }
        }

        if (_showingsByDate.value.containsKey(dateStr)) return

        viewModelScope.launch {
            _loadingShowtimes.value = true
            val before = dateStr
            val after = date.minusDays(1).toString()
            val rawList = movieShowingRepository.fetchMovieShowings(
                page = 1,
                movieId = movieId,
                dateBefore = before,
                dateAfter = before,
                cinemaId = cinemaId
            )
            val cleaned = mapShowings(rawList)
            _showingsByDate.update { it + (dateStr to cleaned) }
            _loadingShowtimes.value = false
            Log.d("SHOWINGS", cleaned.toString())
        }
    }

    private fun mapShowings(list: List<MovieShowing>?): List<MovieShowing> {
        if (list.isNullOrEmpty()) return emptyList()
        // tutaj dowolne dodatkowe mapowanie / formatowanie daty-czasu
        return list
    }
}