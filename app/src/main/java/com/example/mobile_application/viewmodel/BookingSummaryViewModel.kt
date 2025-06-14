package com.example.mobile_application.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.model.CreateOrderResponse
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
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

// ViewModel
class BookingSummaryViewModel(application: Application): AndroidViewModel(application){
    private val cinemaRepository = CinemaRepository(application)
    private val movieRepository = MovieRepository(application)
    private val movieShowingRepository = MovieShowingRepository(application)
    private val ticketRepository = TicketRepository(application)
    private val orderRepository = OrderRepository(application)

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

    suspend fun postOrder(ticketIds: List<Int>, email: String): CreateOrderResponse? {
        return orderRepository.postOrder(ticketIds, email)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadContext(showingId: Int, seatIds: List<Int>) {
        // 1) fetch showing → extract date, time, price, hallId
        val showing = movieShowingRepository.fetchShowingById(showingId)!!
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val outputDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val parsedDateTime = OffsetDateTime.parse(showing.date, inputFormatter)

        _date.value = parsedDateTime.format(outputDateFormatter)
        _time.value = parsedDateTime.format(outputTimeFormatter)
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
