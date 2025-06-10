package com.example.mobile_application.network

import com.example.mobile_application.model.Genre
import com.example.mobile_application.model.GenreResponse
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.MovieCrew
import com.example.mobile_application.model.MovieCrewResponse
import com.example.mobile_application.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("api/movies/")
    suspend fun getMovies(@Query("title") title: String?): Response<MovieResponse>

    @GET("api/movies/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") movieId: Int): Response<Movie>

    @GET("api/movie_crews/")
    suspend fun getMovieCrews(@Query("page") page: Int?): Response<MovieCrewResponse>

    @GET("api/movie_crews/{movie_id}")
    suspend fun getMovieCrewById(@Path("movie_id") movieId: Int): Response<MovieCrew>

    @GET("api/genres")
    suspend fun getGenres(@Query("page") page: Int?): Response<GenreResponse>
}
