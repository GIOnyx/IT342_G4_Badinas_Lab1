package com.example.miniapp.data.repository

import com.example.miniapp.data.api.RetrofitClient
import com.example.miniapp.data.model.LoginRequest
import com.example.miniapp.data.model.RegisterRequest

class AuthRepository(private val tokenManager: TokenManager) {
    private val api = RetrofitClient.instance

    suspend fun register(name: String, email: String, password: String): Result<String> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    // Note: backend returns token=null in body and sets HttpOnly cookie
                    // For mobile, we'll need the backend to return the token in the body
                    // or implement a different auth strategy
                    Result.success(authResponse.name)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    // Note: Same as register - backend uses HttpOnly cookie
                    // For mobile you may want to modify backend to return token in body
                    Result.success(authResponse.name)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun validate(): Result<String> {
        return try {
            val response = api.validate()
            if (response.isSuccessful) {
                response.body()?.let { validateResponse ->
                    Result.success(validateResponse.name)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Validation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            val response = api.logout()
            if (response.isSuccessful) {
                tokenManager.clearToken()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Logout failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
