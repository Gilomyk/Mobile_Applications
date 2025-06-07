package com.example.mobile_application.network

import com.example.mobile_application.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    /*TODO*/
//    Ogarnąć zdjęcia na serwerze
    private const val BASE_URL = "https://cinemaland.pl/"
//    private const val BASE_URL = "http://10.0.2.2:8000/"



    private const val apiKey = BuildConfig.API_KEY

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Api-Key $apiKey")
                .build()

            println("➡️ Dodaję nagłówek: Api-Key $apiKey") // 🔍 DEBUG
            chain.proceed(modifiedRequest)
        }
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val service: MovieApiService = retrofit.create(MovieApiService::class.java)
}