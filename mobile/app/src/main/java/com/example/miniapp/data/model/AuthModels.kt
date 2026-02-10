package com.example.miniapp.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String?,
    val name: String,
    val email: String
)

data class ValidateResponse(
    val name: String,
    val email: String
)
