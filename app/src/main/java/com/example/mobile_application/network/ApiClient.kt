package com.example.mobile_application.network

import AuthInterceptor
import android.content.Context
import com.example.mobile_application.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

data class ApiClientInterface(
    val movieService: MovieApiService,
    val ticketService: TicketApiService,
    val cinemaService: CinemaApiService,
    val movieShowingService: MovieShowingApiService,
    val orderService: OrderApiService,
    val userService: UserApiService
)

object ApiClient {
    private const val BASE_URL = "https://cinemaland.pl/"

    fun create(context: Context): ApiClientInterface {
        val logginInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(logginInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return ApiClientInterface(
            retrofit.create(MovieApiService::class.java),
            retrofit.create(TicketApiService::class.java),
            retrofit.create(CinemaApiService::class.java),
            retrofit.create(MovieShowingApiService::class.java),
            retrofit.create(OrderApiService::class.java),
            retrofit.create(UserApiService::class.java),
        )
    }
}