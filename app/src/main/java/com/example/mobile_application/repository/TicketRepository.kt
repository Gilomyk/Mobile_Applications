package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.Ticket
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketRepository {
    suspend fun fetchTickets(
        page: Int? = null,
        user: Int? = null,
        ordering: String = "showing_date",
    ): List<Ticket>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.ticketService.getTickets(
                page=page,
                userId=user,
                ordering=ordering)
            if (response.isSuccessful) {
                return@withContext response.body()?.results
            } else {
                Log.e(TAG, "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching tickets: ${e.localizedMessage}")
        }
        return@withContext null
    }

    companion object {
        private const val TAG = "TicketRepository"
    }
}