package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.Ticket
import com.example.mobile_application.model.TicketDiscount
import com.example.mobile_application.model.TicketPayload
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketRepository {
    suspend fun fetchTickets(
        page: Int? = null,
        user: Int? = null,
        ordering: String = "showing_date",
        showing: Int?
    ): List<Ticket>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.ticketService.getTickets(
                page=page,
                userId=user,
                ordering=ordering,
                showing=showing)
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

    suspend fun createTicket(payload: TicketPayload): Ticket? = withContext(Dispatchers.IO) {
        try {
            val res = ApiClient.ticketService.createTicket(payload)
            if (res.isSuccessful) return@withContext res.body()
            else Log.e(TAG, "createTicket API error: ${res.code()}")
        } catch (e: Exception) {
            Log.e(TAG, "createTicket exception: ${e.localizedMessage}")
        }
        null
    }

    suspend fun fetchActiveDiscounts(): List<TicketDiscount>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.ticketService.getActiveDiscounts()
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                Log.e(TAG, "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching discounts: ${e.localizedMessage}")
        }
        return@withContext null
    }

    companion object {
        private const val TAG = "TicketRepository"
    }
}