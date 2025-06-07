package com.example.mobile_application.model

data class MovieResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Movie>
)
