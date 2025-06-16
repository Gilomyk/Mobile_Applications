package com.example.mobile_application.network

import com.example.mobile_application.model.RegisterResponse
import com.example.mobile_application.model.TokenRequest
import com.example.mobile_application.model.TokenResponse
import com.example.mobile_application.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @GET("/api/profile")
    suspend fun getProfile(): Response<User>

    @POST("/api/register/")
    suspend fun register(@Body request: Map<String, String>): Response<RegisterResponse>

    @POST("/api/token/")
    suspend fun getUserToken(@Body tokenRequest: TokenRequest): Response<TokenResponse>
}