package com.example.mobile_application.model

data class MovieCrewResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<MovieCrew>
)
