package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.Order
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository {
    suspend fun fetchOrders(page: Int?, user: Int?, status: String?): List<Order>? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.orderService.getOrders(
                page=page,
                userId=user,
                status=status
            )
            if (response.isSuccessful) {
                return@withContext response.body()?.results
            } else {
                Log.e("OrderRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun fetchOrderById(orderId: Int): Order? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.orderService.getOrderById(orderId)
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                Log.e("OrderRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }
}