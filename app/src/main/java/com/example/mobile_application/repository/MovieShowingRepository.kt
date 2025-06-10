package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.MovieShowing
import com.example.mobile_application.model.MovieShowingResponse
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
        dateAfter: String? = null
    ): List<MovieShowing>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.movieShowingService.getMovieShowings(
                page=page,
                movieId=movieId,
                dateBefore=dateBefore,
                dateAfter=dateAfter
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

    companion object {
        private const val TAG = "MovieShowingRepository"
    }
}
