package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.Cinema
import com.example.mobile_application.model.CinemaHall
import com.example.mobile_application.model.HallType
import com.example.mobile_application.model.Seat
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CinemaRepository {
    suspend fun fetchCinemas(): List<Cinema>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cinemaService.getCinemas()
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

    suspend fun fetchClosestCinemas(latitude: Double, longitude: Double, amount: Int?=null): List<Cinema>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cinemaService.getClosestCinemas(
                latitude =latitude,
                longitude =longitude,
                amount =amount)
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

    suspend fun fetchCinema(cinemaId: Int): Cinema? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cinemaService.getCinema(cinemaId)
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                Log.e("CinemaRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("CinemaRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }


    suspend fun fetchCinemaHalls(cinemaId: Int?): List<CinemaHall>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cinemaService.getCinemaHalls(page = null, cinemaId = cinemaId)
            if (response.isSuccessful) {
                return@withContext response.body()?.results
            } else {
                Log.e("CinemaRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("CinemaRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun fetchAllCinemaHalls(cinemaId: Int?): List<CinemaHall> = withContext(Dispatchers.IO) {
        val allHalls = mutableListOf<CinemaHall>()
        var currentPage: Int? = 1
        var hasMorePages = true

        while (hasMorePages) {
            try {
                val response = ApiClient.cinemaService.getCinemaHalls(page = currentPage, cinemaId = cinemaId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        allHalls.addAll(body.results)
                        // Sprawdź, czy jest kolejna strona
                        hasMorePages = body.next != null
                        currentPage = if (hasMorePages) currentPage?.plus(1) else null
                    } else {
                        hasMorePages = false
                    }
                } else {
                    Log.e("CinemaRepository", "API error: ${response.code()}")
                    hasMorePages = false
                }
            } catch (e: Exception) {
                Log.e("CinemaRepository", "Exception: ${e.localizedMessage}")
                hasMorePages = false
            }
        }

        return@withContext allHalls
    }

    suspend fun fetchHallTypes(): List<HallType>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cinemaService.getHallTypes()
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

    suspend fun fetchAllSeats(hallId: Int): List<Seat>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cinemaService.getSeats(page = null, hallId = hallId)
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
}