package com.example.mobile_application.network

import com.example.mobile_application.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("api/movies/")
    suspend fun getMovies(@Query("title") title: String?): Response<MovieResponse>
}
