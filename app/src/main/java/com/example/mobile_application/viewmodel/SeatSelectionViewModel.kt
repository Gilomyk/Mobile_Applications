package com.example.mobile_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.Seat
import com.example.mobile_application.repository.CinemaRepository
import com.example.mobile_application.repository.TicketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeatSelectionViewModel() : ViewModel() {

    private val cinemaRepository = CinemaRepository()
    private val ticketRepository = TicketRepository()
    
    private val _seats = MutableStateFlow<List<Seat>>(emptyList())
    val seats: StateFlow<List<Seat>> = _seats

    private val _takenSeatIds = MutableStateFlow<Set<Int>>(emptySet())
    val takenSeatIds: StateFlow<Set<Int>> = _takenSeatIds

    private val _selectedSeatIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedSeatIds: StateFlow<Set<Int>> = _selectedSeatIds

    private val _justTakenSeatIds = MutableStateFlow<Set<Int>>(emptySet())
    val justTakenSeatIds: StateFlow<Set<Int>> = _justTakenSeatIds

    fun loadSeats(cinemaHallId: Int) = viewModelScope.launch {
        val allSeats = cinemaRepository.fetchAllSeats(cinemaHallId) ?: emptyList()
        _seats.value = allSeats.filter { it.hall == cinemaHallId }
    }

    fun loadTakenSeats(showingId: Int) = viewModelScope.launch {
        val allTickets = ticketRepository.fetchTickets(null, null, "", showingId) ?: emptyList()
        _takenSeatIds.value = allTickets.map { it.seat }.toSet()
    }

    fun toggleSelect(seatId: Int) {
        _selectedSeatIds.update { set ->
            if (set.contains(seatId)) set - seatId else set + seatId
        }
    }

    fun markSeatJustTaken(seatId: Int) {
        _justTakenSeatIds.update { it + seatId }
        viewModelScope.launch {
            delay(1000)
            _justTakenSeatIds.update { it - seatId }
        }
    }
}
