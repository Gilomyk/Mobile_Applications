package com.example.mobile_application.network

import com.example.mobile_application.model.CreateOrderResponse
import com.example.mobile_application.model.Order
import com.example.mobile_application.model.OrderPayload
import com.example.mobile_application.model.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {
    @GET("/api/orders")
    suspend fun getOrders(
        @Query("page") page: Int?,
        @Query("user") userId: Int?,
        @Query("status") status: String?
    ): Response<OrderResponse>

    @POST("api/orders/")
    suspend fun createOrder(
        @Body payload: OrderPayload
    ): Response<CreateOrderResponse>

    @GET("/api/orders/{order_id}")
    suspend fun getOrderById(
        @Path("order_id") orderId: Int
    ): Response<Order>
}