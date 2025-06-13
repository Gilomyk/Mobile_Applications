package com.example.mobile_application.network

import com.example.mobile_application.model.RegisterRequest
import com.example.mobile_application.model.Token
import com.example.mobile_application.model.TokenRequest
import com.example.mobile_application.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @GET("/profile")
    suspend fun getProfile(): Response<User>

    @POST("/register")
    suspend fun  registerUser(@Body registerRequest: RegisterRequest): Response<Unit>

    @POST("/token")
    suspend fun getUserToken(@Body tokenRequest: TokenRequest): Response<Token>
}