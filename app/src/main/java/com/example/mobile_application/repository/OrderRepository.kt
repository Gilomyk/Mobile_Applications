package com.example.mobile_application.repository

import android.content.Context
import android.util.Log
import com.example.mobile_application.model.CreateOrderResponse
import com.example.mobile_application.model.Order
import com.example.mobile_application.model.OrderPayload
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(private val context: Context) {
    private val api = ApiClient.create(context).orderService

    suspend fun fetchOrders(page: Int?, user: Int?, status: String?): List<Order>? = withContext(Dispatchers.IO) {
        try {
            val response = api.getOrders(
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

    suspend fun postOrder(ticketIds: List<Int>, email: String): CreateOrderResponse? = withContext(Dispatchers.IO) {
        try {
            val payload = OrderPayload(ticketIds, email)
            val response = api.createOrder(payload)
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                Log.e(TAG, "postOrder API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "postOrder exception: ${e.localizedMessage}")
        }
        null
    }

    suspend fun fetchOrderById(orderId: Int): Order? = withContext(Dispatchers.IO) {
        try {
            val response = api.getOrderById(orderId)
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

    companion object { private const val TAG = "OrderRepository" }
}