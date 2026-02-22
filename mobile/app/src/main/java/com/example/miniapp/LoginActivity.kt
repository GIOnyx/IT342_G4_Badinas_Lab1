package com.example.miniapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.widget.Toast
import com.example.miniapp.network.ApiClient
import com.example.miniapp.network.AuthRequest
import com.example.miniapp.network.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val signInBtn = findViewById<Button>(R.id.signInBtn)
        val goToRegister = findViewById<android.widget.TextView>(R.id.goToRegister)

        goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        signInBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
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
            val req = AuthRequest(email, password)
            ApiClient.authService.login(req).enqueue(object: Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    signInBtn.isEnabled = true
                    if (response.isSuccessful) {
                        // Backend sends JWT in Set-Cookie header
                        val cookieHeader = response.headers().get("Set-Cookie")
                        var token: String? = null
                        if (!cookieHeader.isNullOrBlank()) {
                            val kv = cookieHeader.split(';')[0]
                            if (kv.contains("=")) token = kv.substringAfter('=')
                        }
                        val body = response.body()
                        val name = body?.name ?: email.substringBefore('@')
                        if (!token.isNullOrBlank()) {
                            SessionManager.saveSession(this@LoginActivity, token, name, body?.email)
                        }
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("name", name)
                        startActivity(intent)
                    } else {
                        // Parse backend error message from JSON body
                        val errBody = response.errorBody()?.string()
                        val msg = try {
                            org.json.JSONObject(errBody ?: "").optString("message", "Login failed")
                        } catch (e: Exception) { "Login failed (${response.code()})" }
                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    signInBtn.isEnabled = true
                    Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
