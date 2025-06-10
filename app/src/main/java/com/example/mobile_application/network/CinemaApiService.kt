package com.example.mobile_application.network

import com.example.mobile_application.model.CinemaHallResponse
import com.example.mobile_application.model.CinemaResponse
import com.example.mobile_application.model.SeatResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CinemaApiService {
    @GET("/api/cinemas")
    suspend fun getCinemas(): Response<CinemaResponse>

    @GET("/api/cinema_halls")
    suspend fun getCinemaHalls(@Query("page") page: Int?, @Query("cinema") cinemaId: Int?): Response<CinemaHallResponse>

    @GET("/api/seats")
    suspend fun getSeats(@Query("page") page: Int?,@Query("hall") hallId: Int?): Response<SeatResponse>
}