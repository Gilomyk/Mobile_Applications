package com.example.mobile_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.OrderResponse
import com.example.mobile_application.model.Seat
import com.example.mobile_application.model.TicketDiscount
import com.example.mobile_application.model.TicketPayload
import com.example.mobile_application.repository.CinemaRepository
import com.example.mobile_application.repository.MovieRepository
import com.example.mobile_application.repository.MovieShowingRepository
import com.example.mobile_application.repository.OrderRepository
import com.example.mobile_application.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel
class BookingSummaryViewModel() : ViewModel() {
    private val cinemaRepository = CinemaRepository()
    private val movieRepository = MovieRepository()
    private val movieShowingRepository = MovieShowingRepository()
    private val ticketRepository = TicketRepository()
    private val orderRepository = OrderRepository()

    private val _discounts = MutableStateFlow<List<TicketDiscount>>(emptyList())
    val discounts: StateFlow<List<TicketDiscount>> = _discounts

    private val _movieTitle = MutableStateFlow("")
    val movieTitle: StateFlow<String> = _movieTitle

    private val _cinemaName = MutableStateFlow("")
    val cinemaName: StateFlow<String> = _cinemaName

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date

    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time

    private val _showingPrice = MutableStateFlow(0.0)
    val showingPrice: StateFlow<Double> = _showingPrice

    private val _selectedSeats = MutableStateFlow<List<Seat>>(emptyList())
    val selectedSeats: StateFlow<List<Seat>> = _selectedSeats

    fun loadDiscounts() = viewModelScope.launch {
        val list = ticketRepository.fetchActiveDiscounts() ?: emptyList()
        _discounts.value = list
    }

    suspend fun postTickets(tickets: List<TicketPayload>): List<Int> {
        return tickets.mapNotNull { payload ->
            ticketRepository.createTicket(payload)?.id
        }
    }

    suspend fun postOrder(ticketIds: List<Int>, email: String): OrderResponse? {
        return orderRepository.postOrder(ticketIds, email)
    }

    suspend fun loadContext(showingId: Int, seatIds: List<Int>) {
        // 1) fetch showing → extract date, time, price, hallId
        val showing = movieShowingRepository.fetchShowingById(showingId)!!
        _date.value = showing.date
        _time.value = showing.date
        _showingPrice.value = showing.ticket_price.toDouble()
        // 2) fetch hall → cinemaId
        val hall = cinemaRepository.fetchHallById(showing.hall)!!
        // 3) fetch cinema → name
        val cinema = cinemaRepository.fetchCinema(hall.cinema)!!
        _cinemaName.value = cinema.name
        // 4) fetch movie → title
        val movie = movieRepository.fetchMovieById(showing.movie)!!
        _movieTitle.value = movie.title
        // 5) seats list
        val seats = cinemaRepository.fetchSeatsByIds(seatIds)
        _selectedSeats.value = seats
    }

}
