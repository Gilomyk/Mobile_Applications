package com.example.mobile_application.model

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
