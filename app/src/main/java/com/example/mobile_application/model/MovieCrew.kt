package com.example.mobile_application.model

data class MovieCrew(
    val id: Int,
    val director: List<Director>,
    val mainLead: List<MainLead>,
)

data class Director(
    val id: Int,
    val name: String
)

data class MainLead(
    val id: Int,
    val name: String
)
