package com.example.mobile_application.repository

import android.content.Context
import android.util.Log
import com.example.mobile_application.model.RegisterResponse
import com.example.mobile_application.model.TokenRequest
import com.example.mobile_application.model.TokenResponse
import com.example.mobile_application.model.User
import com.example.mobile_application.network.ApiClient
import com.example.mobile_application.utils.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository(private val context: Context) {
    private val api = ApiClient.create(context).userService

    suspend fun fetchProfile(): User? = withContext(Dispatchers.IO) {
        try {
            val response = api.getProfile()
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                Log.d("UserRepository", "API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.localizedMessage}")
        }
        return@withContext null
    }

    suspend fun login(username: String, password: String): Response<TokenResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.getUserToken(TokenRequest(username, password))
            if (response.isSuccessful) {
                val token = response.body()?.access
                token?.let { AuthManager.saveToken(context, it) }
            } else {
                Log.e("UserRepository", "Login failed: ${response.code()} - ${response.message()}")
            }
            return@withContext response
        } catch (e: Exception) {
            Log.e("UserRepository", "Login exception: ${e.localizedMessage}")
            throw e
        }
    }

    suspend fun register(username: String, email: String, password1: String, password2: String): Response<RegisterResponse> {
        val requestBody = mapOf(
            "username" to username,
            "email" to email,
            "password1" to password1,
            "password2" to password2
        )
        return api.register(requestBody)
    }

}