package com.example.mobile_application.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)

data class TokenRequest(
    val username: String,
    val password: String
)

data class DeviceRequest(
    val fcm_token: String
)