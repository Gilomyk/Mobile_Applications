package com.example.mobile_application.model

data class Cinema(
    val id: Int,
    val name: String,
    val location_city: String,
    val location_street: String,
    val location_number: Int,
    val longitude: Float,
    val latitude: Float
)

data class HallType(
    val id: Int,
    val hall_type: String,
)

data class CinemaHall(
    val id: Int,
    val hall_number: Int,
    val cinema: Int,
    val types: List<Int>
)

data class Seat(
    val id: Int,
    val row: Int,
    val number: Int,
    val hall: Int,
)

data class Movie(
    val id: Int,
    val title: String,
    val poster: String,
    val duration: Int,
    val description: String,
    val release_date: String,
    val trailer: String,
    val genre: List<Genre>
)

data class Genre(
    val id: Int,
    val genre: String
)

data class MovieCrew(
    val id: Int,
    val director: List<Director>,
    val main_lead: List<MainLead>,
)

data class Director(
    val id: Int,
    val name: String
)

data class MainLead(
    val id: Int,
    val name: String
)

//data class Artist(
//    val id: Int,
//    val name: String
//)

data class MovieShowing(
    val id: Int,
    val date: String,
    val showing_type: Int,
    val movie: Int,
    val hall: Int,
    val ticket_price: Float,
)

data class TicketDiscount(
    val id: Int,
    val name: String,
    val percentage: Float,
    val start_date: String,
    val end_date: String,
)

data class Ticket(
    val id: Int,
    val base_price: Float,
    val seat: Int,
    val purchase_time: String,
    val purchase_price: Float,
    val buyer: Int,
    val discount: Int,
    val cancelled: Boolean,
)

data class PaymentStatus(
    val id: Int,
    val label: String,
)

data class Order(
    val id: Int,
    val tickets: List<Int>,
    val amount: Int,
    val status: Int,
    val user: Int,
    val email: String
)