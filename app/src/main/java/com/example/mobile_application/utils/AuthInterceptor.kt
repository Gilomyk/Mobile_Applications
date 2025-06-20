package com.example.mobile_application.utils

import android.content.Context
import com.example.mobile_application.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

// Interceptor do dodawania Api-Key do wszystkich endpointów, z wyjątkiem getProfile
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = AuthManager.getToken(context)

        val urlString = originalRequest.url.toString()
        val useJwt = urlString.contains("/profile") || (urlString.contains("/tickets/") || urlString.contains("/user_device") && originalRequest.method == "POST")

        val modifiedRequest = if (useJwt && token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Api-Key ${BuildConfig.API_KEY}")
                .build()
        }

        return chain.proceed(modifiedRequest)
    }
}
