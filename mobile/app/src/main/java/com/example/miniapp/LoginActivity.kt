package com.example.miniapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.miniapp.network.ApiClient
import com.example.miniapp.network.AuthCodeResponse
import com.example.miniapp.network.AuthorizeRequest
import com.example.miniapp.network.AuthResponse
import com.example.miniapp.network.TokenRequest
import com.example.miniapp.security.PkceHelper
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Back arrow — navigates up to MainActivity
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val emailInput    = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val signInBtn     = findViewById<Button>(R.id.signInBtn)
        val goToRegister  = findViewById<android.widget.TextView>(R.id.goToRegister)

        goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        signInBtn.setOnClickListener {
            val email    = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signInBtn.isEnabled = false

            // PKCE: generate verifier + challenge before any network call
            val codeVerifier  = PkceHelper.generateCodeVerifier()
            val codeChallenge = PkceHelper.generateCodeChallenge(codeVerifier)

            // ── Step 1: /authorize ────────────────────────────────────────────
            // Credentials + code_challenge  →  one-time authorization code
            ApiClient.authService
                .authorize(AuthorizeRequest(email, password, codeChallenge))
                .enqueue(object : Callback<AuthCodeResponse> {
                    override fun onResponse(
                        call: Call<AuthCodeResponse>,
                        response: Response<AuthCodeResponse>
                    ) {
                        val authCode = response.body()?.code
                        if (!response.isSuccessful || authCode.isNullOrBlank()) {
                            signInBtn.isEnabled = true
                            Toast.makeText(
                                this@LoginActivity,
                                parseError(response.errorBody()?.string(), response.code()),
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }

                        // ── Step 2: /token ────────────────────────────────────
                        // authorization code + code_verifier  →  JWT (Set-Cookie)
                        ApiClient.authService
                            .token(TokenRequest(authCode, codeVerifier))
                            .enqueue(object : Callback<AuthResponse> {
                                override fun onResponse(
                                    call: Call<AuthResponse>,
                                    response: Response<AuthResponse>
                                ) {
                                    signInBtn.isEnabled = true
                                    if (response.isSuccessful) {
                                        val cookieHeader = response.headers().get("Set-Cookie")
                                        var token: String? = null
                                        if (!cookieHeader.isNullOrBlank()) {
                                            val kv = cookieHeader.split(';')[0]
                                            if (kv.contains("=")) token = kv.substringAfter('=')
                                        }
                                        val body = response.body()
                                        val name  = body?.name ?: email.substringBefore('@')
                                        if (!token.isNullOrBlank()) {
                                            SessionManager.saveSession(
                                                this@LoginActivity, token, name, body?.email
                                            )
                                        }
                                        startActivity(
                                            Intent(this@LoginActivity, DashboardActivity::class.java)
                                                .putExtra("name", name)
                                        )
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            parseError(response.errorBody()?.string(), response.code()),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                    signInBtn.isEnabled = true
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Network error: ${t.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                    }

                    override fun onFailure(call: Call<AuthCodeResponse>, t: Throwable) {
                        signInBtn.isEnabled = true
                        Toast.makeText(
                            this@LoginActivity,
                            "Network error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    private fun parseError(body: String?, code: Int): String = try {
        org.json.JSONObject(body ?: "").optString("message", "Login failed")
    } catch (e: Exception) {
        "Login failed ($code)"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
