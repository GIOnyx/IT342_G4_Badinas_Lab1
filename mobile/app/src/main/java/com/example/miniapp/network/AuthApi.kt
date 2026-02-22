package com.example.miniapp.network

import retrofit2.Call
import retrofit2.http.*

// ── Direct auth DTOs ──────────────────────────────────────────────
data class AuthRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class AuthResponse(val token: String?, val name: String?, val email: String?)

// ── PKCE (Authorization Code + S256) DTOs ─────────────────────────
/** Step 1 request: credentials + code_challenge → authorization code */
data class AuthorizeRequest(
    val email: String,
    val password: String,
    val codeChallenge: String,
    val codeChallengeMethod: String = "S256"
)

/** Step 1 response: one-time authorization code */
data class AuthCodeResponse(val code: String)

/** Step 2 request: authorization code + code_verifier → JWT */
data class TokenRequest(val code: String, val codeVerifier: String)

interface AuthApi {
    // ── Legacy direct login (kept for reference / web flow) ─────────
    @POST("/api/auth/login")
    fun login(@Body req: AuthRequest): Call<AuthResponse>

    @POST("/api/auth/register")
    fun register(@Body req: RegisterRequest): Call<AuthResponse>

    @GET("/api/auth/validate")
    fun validate(@Header("Cookie") cookie: String): Call<Map<String, String>>

    @POST("/api/auth/logout")
    fun logout(): Call<Void>

    // ── PKCE flow ────────────────────────────────────────────────────
    /** Step 1 — verify credentials, return short-lived authorization code */
    @POST("/api/auth/authorize")
    fun authorize(@Body req: AuthorizeRequest): Call<AuthCodeResponse>

    /** Step 2 — exchange code + code_verifier for JWT (Set-Cookie response) */
    @POST("/api/auth/token")
    fun token(@Body req: TokenRequest): Call<AuthResponse>
}
