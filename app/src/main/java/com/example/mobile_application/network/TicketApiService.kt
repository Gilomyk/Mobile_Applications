package com.example.mobile_application.network


import com.example.mobile_application.model.TicketDiscount
import com.example.mobile_application.model.TicketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TicketApiService {
    @GET("/api/tickets/")
    suspend fun getTickets(
        @Query("page") page: Int?,
        @Query("ordering") ordering: String?,
        @Query("user") userId: Int?
    ): Response<TicketResponse>

    @POST("/api/tickets")
    suspend fun createTickets()

    @GET("/api/ticket_discounts/active_discounts")
    suspend fun getActiveDiscounts(): Response<List<TicketDiscount>>

}