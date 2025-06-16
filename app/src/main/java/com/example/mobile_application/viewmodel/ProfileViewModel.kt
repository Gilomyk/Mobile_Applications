package com.example.mobile_application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.MovieShowing
import com.example.mobile_application.model.Ticket
import com.example.mobile_application.model.User
import com.example.mobile_application.repository.MovieRepository
import com.example.mobile_application.repository.MovieShowingRepository
import com.example.mobile_application.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TicketWithDetails(
    val ticket: Ticket,
    val showing: MovieShowing,
    val movie: Movie
)

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val tickets: List<TicketWithDetails> = emptyList(),
    val error: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val showingRepository = MovieShowingRepository(application)
    private val movieRepository = MovieRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            try {
                val user = requireNotNull(userRepository.fetchProfile()) { "User is null!" }
                val tickets = user.tickets

                val enrichedTickets = tickets.map { ticket ->
                    val showing = requireNotNull(showingRepository.fetchShowingById(ticket.showing)) {
                        "Brak danych o seansie o ID ${ticket.showing}"
                    }

                    val movie = requireNotNull(movieRepository.fetchMovieById(showing.movie)) {
                        "Brak danych o filmie o ID ${showing.movie}"
                    }
                    TicketWithDetails(ticket, showing, movie)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        tickets = enrichedTickets
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }
}