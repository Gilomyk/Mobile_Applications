package com.example.mobile_application.model

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
