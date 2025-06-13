package com.example.mobile_application.network

import com.example.mobile_application.model.MovieShowing
import com.example.mobile_application.model.MovieShowingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieShowingApiService {
    @GET("/api/movie_showings")
    suspend fun getMovieShowings(
        @Query("page") page: Int?,
        @Query("movie") movieId: Int?,
        @Query("showing_date_after") dateAfter: String?,
        @Query("showing_date_before") dateBefore: String?,
        @Query("cinema") cinemaId: Int?
    ): Response<MovieShowingResponse>

    @GET("api/movie_showings/{id}/")
    suspend fun getShowingById(
        @Path("id") id: Int
    ): Response<MovieShowing>
}