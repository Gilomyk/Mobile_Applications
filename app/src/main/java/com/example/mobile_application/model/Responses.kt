package com.example.mobile_application.model

data class MovieResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Movie>
)

data class MovieCrewResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<MovieCrew>
)

data class CinemaResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Cinema>,
)

data class HallTypeResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<HallType>,
)

data class CinemaHallResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<CinemaHall>,
)

data class SeatResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Seat>,
)

//data class ArtistResponse(
//    val count: Int,
//    val next: String?,
//    val previous: String?,
//    val result: List<Artist>,
//)

data class GenreResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Genre>,
)

data class MovieShowingResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<MovieShowing>,
)

//data class TicketDiscountResponse(
//    val count: Int,
//    val next: String?,
//    val previous: String?,
//    val results: List<TicketDiscount>,
//)

data class TicketResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Ticket>,
)

data class OrderResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Order>
)

//data class UserResponse(
//    val id: Int,
//    val username: String,
//    val email: String,
//    val tickets: List<Ticket>
//)