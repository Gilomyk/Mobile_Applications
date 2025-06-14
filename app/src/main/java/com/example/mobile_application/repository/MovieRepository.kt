package com.example.mobile_application.repository

import android.content.Context
import android.util.Log
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.MovieCrew
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val context: Context) {
    private val api = ApiClient.create(context).movieService

    suspend fun fetchMovies(title: String?): List<Movie>? = withContext(Dispatchers.IO) {
        try {
            val response = api.getMovies(title=title)
            if (response.isSuccessful) {
                return@withContext response.body()?.results
            } else {
                Log.e("MovieRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun fetchMoviesByCinema(cinemaId: Int): List<Movie>? = withContext(Dispatchers.IO) {
        try {
            val response = api.getMovies(cinemaId=cinemaId)
            if (response.isSuccessful) {
                return@withContext response.body()?.results
            } else {
                Log.e("MovieRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun fetchMovieById(movieId: Int): Movie? = withContext(Dispatchers.IO) {
        try {
            val response = api.getMovieById(movieId)
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                Log.e("MovieRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun fetchMovieCrews(): List<MovieCrew>? = withContext(Dispatchers.IO) {
        try {
            val response = api.getMovieCrews(page=null)
            if (response.isSuccessful) {
                return@withContext response.body()?.results
            } else {
                Log.e("MovieRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun fetchMovieCrewById(movieId: Int): MovieCrew? = withContext(Dispatchers.IO) {
        try {
            val response = api.getMovieCrewById(movieId)
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                Log.e("MovieRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }
}
