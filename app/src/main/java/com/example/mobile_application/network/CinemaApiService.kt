package com.example.mobile_application.network

import com.example.mobile_application.model.Cinema
import com.example.mobile_application.model.CinemaHall
import com.example.mobile_application.model.CinemaHallResponse
import com.example.mobile_application.model.CinemaResponse
import com.example.mobile_application.model.HallTypeResponse
import com.example.mobile_application.model.Seat
import com.example.mobile_application.model.SeatResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CinemaApiService {
    @GET("/api/cinemas")
    suspend fun getCinemas(): Response<CinemaResponse>

    @GET("/api/cinemas/{cinemaId}")
    suspend fun getCinema(@Path("cinemaId") cinemaId: Int): Response <Cinema>

    @GET("/api/cinema_halls")
    suspend fun getCinemaHalls(@Query("page") page: Int?, @Query("cinema") cinemaId: Int?, ): Response<CinemaHallResponse>

    @GET("api/cinema_halls/{id}/")
    suspend fun getHallById(
        @Path("id") id: Int
    ): Response<CinemaHall>

    @GET("/api/hall_types")
    suspend fun getHallTypes(): Response<HallTypeResponse>

    @GET("/api/seats")
    suspend fun getSeats(@Query("page") page: Int?,@Query("hall") hallId: Int?): Response<SeatResponse>

    @GET("api/seats/{id}/")
    suspend fun getSeatById(
        @Path("id") id: Int
    ): Response<Seat>

    @GET("/api/cinemas/get_closest")
    suspend fun getClosestCinemas(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("amount") amount: Int?
    ): Response<List<Cinema>>
}