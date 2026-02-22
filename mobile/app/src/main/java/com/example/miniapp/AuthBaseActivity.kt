package com.example.miniapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.miniapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Base activity for every protected screen.
 * Mirrors the web's <RequireAuth> component:
 *   - On every resume, calls /api/auth/validate with the stored JWT.
 *   - If the token is missing or the server returns non-200, clears the session
 *     and redirects the user to LoginActivity (equivalent to web navigate('/login')).
 */
abstract class AuthBaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        validateSession()
    }

    private fun validateSession() {
        val token = SessionManager.getToken(this)
        if (token.isNullOrBlank()) {
            redirectToLogin()
            return
        }
        ApiClient.authService.validate("token=$token")
            .enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    if (!response.isSuccessful) {
                        // Token expired or invalid — clear and redirect, same as web's RequireAuth
                        SessionManager.clearSession(this@AuthBaseActivity)
                        redirectToLogin()
                    } else {
                        // Optionally let subclass update UI with fresh profile data
                        response.body()?.let { onSessionValidated(it) }
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    // Network error — don't log out, just leave the screen as-is
                }
            })
    }

    /** Called when validation succeeds; override to refresh UI with server data. */
    open fun onSessionValidated(profile: Map<String, String>) {}

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    /**
     * Proper logout: mirrors web's handleLogout —
     * calls /api/auth/logout on the backend, clears local session, then redirects to Login.
     */
    fun logout() {
        ApiClient.authService.logout().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                SessionManager.clearSession(this@AuthBaseActivity)
                redirectToLogin()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Even if network fails, clear local session and redirect
                SessionManager.clearSession(this@AuthBaseActivity)
                redirectToLogin()
            }
        })
    }
}
