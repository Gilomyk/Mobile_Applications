package com.example.mobile_application.network


import com.example.mobile_application.model.Ticket
import com.example.mobile_application.model.TicketDiscount
import com.example.mobile_application.model.TicketPayload
import com.example.mobile_application.model.TicketResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TicketApiService {
    @GET("/api/tickets/")
    suspend fun getTickets(
        @Query("page") page: Int?,
        @Query("ordering") ordering: String?,
        @Query("user") userId: Int?,
        @Query("showing") showing: Int?
    ): Response<TicketResponse>

    @POST("api/tickets/")
    suspend fun createTicket(
        @Body payload: TicketPayload
    ): Response<Ticket>

    @GET("/api/ticket_discounts/active_discounts")
    suspend fun getActiveDiscounts(): Response<List<TicketDiscount>>

}