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
        // Interceptor do dodawania Api-Key do wszystkich endpointów, z wyjątkiem getProfile
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            // Jeśli zapytanie to getProfile, dodaj nagłówek Bearer
            if (originalRequest.url.toString().contains("/profile")) {
                val token = "giveToken"/*TODO: give token*/
                val modifiedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(modifiedRequest)
            } else {
                // Dla pozostałych endpointów dodaj Api-Key
                val modifiedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Api-Key $apiKey")
                    .build()
                chain.proceed(modifiedRequest)
            }
        }
        .addInterceptor(loggingInterceptor)
        .build()


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val movieService: MovieApiService = retrofit.create(MovieApiService::class.java)
    val ticketService: TicketApiService = retrofit.create(TicketApiService::class.java)
    val cinemaService: CinemaApiService = retrofit.create(CinemaApiService::class.java)
    val movieShowingService: MovieShowingApiService = retrofit.create(MovieShowingApiService::class.java)
    val orderService: OrderApiService = retrofit.create(OrderApiService::class.java)
    val userService: UserApiService = retrofit.create(UserApiService::class.java)

}