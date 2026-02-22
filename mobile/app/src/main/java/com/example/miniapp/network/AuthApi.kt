package com.example.miniapp.network

import retrofit2.Call
import retrofit2.http.*

data class AuthRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class AuthResponse(val token: String?, val name: String?, val email: String?)

interface AuthApi {
    @POST("/api/auth/login")
    fun login(@Body req: AuthRequest): Call<AuthResponse>

    @POST("/api/auth/register")
    fun register(@Body req: RegisterRequest): Call<AuthResponse>

    @GET("/api/auth/validate")
    fun validate(@Header("Cookie") cookie: String): Call<Map<String, String>>

    @POST("/api/auth/logout")
    fun logout(): Call<Void>
}
