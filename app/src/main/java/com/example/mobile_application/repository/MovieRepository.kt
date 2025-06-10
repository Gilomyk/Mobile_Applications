package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.MovieCrew
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {

    suspend fun fetchMovies(title: String?): List<Movie>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.service.getMovies(title)
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
            val response = ApiClient.service.getMovieById(movieId)
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

//    suspend fun fetchMovieCrews(): List<MovieCrew>? = withContext(Dispatchers.IO) {
//        try {
//            val response = ApiClient.service.getMovieCrews()
//            if (response.isSuccessful) {
//                return@withContext response.body()?.results
//            } else {
//                Log.e("MovieRepository", "API error: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            Log.e("MovieRepository", "Exception: ${e.localizedMessage}")
//        }
//        return@withContext null
//    }

    suspend fun fetchMovieCrewById(movieId: Int): MovieCrew? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.service.getMovieCrewById(movieId)
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
