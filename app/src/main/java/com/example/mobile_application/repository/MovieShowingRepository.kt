package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.MovieShowing
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieShowingRepository {

    /**
     * Pobiera wszystkie seanse filmowe z paginacją.
     * @param page numer strony (opcjonalny)
     * @return lista seansów filmowych lub null w przypadku błędu
     */
    suspend fun fetchMovieShowings(
        page: Int? = null,
        movieId: Int? = null,
        dateBefore: String? = null,
        dateAfter: String? = null,
        cinemaId: Int? = null
    ): List<MovieShowing>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.movieShowingService.getMovieShowings(
                page=page,
                movieId=movieId,
                dateBefore=dateBefore,
                dateAfter=dateAfter,
                cinemaId=cinemaId
            )
            if (response.isSuccessful) {
                return@withContext response.body()?.results
            } else {
                Log.e(TAG, "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching movie showings: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun fetchShowingById(id: Int): MovieShowing? = withContext(Dispatchers.IO) {
        try {
            val res = ApiClient.movieShowingService.getShowingById(id)
            if (res.isSuccessful) {
                return@withContext res.body()
            } else {
                Log.e(TAG, "fetchShowingById API error: ${res.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchShowingById exception: ${e.localizedMessage}")
        }
        null
    }

    companion object {
        private const val TAG = "MovieShowingRepository"
    }
}
