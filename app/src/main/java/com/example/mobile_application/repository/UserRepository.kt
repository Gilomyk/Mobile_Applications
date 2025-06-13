package com.example.mobile_application.repository

import android.util.Log
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.User
import com.example.mobile_application.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    suspend fun fetchProfile(): User? = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.userService.getProfile()
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
}