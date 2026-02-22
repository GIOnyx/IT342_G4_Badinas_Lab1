package com.example.miniapp

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Back arrow — navigates up to LoginActivity
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val nameInput = findViewById<EditText>(R.id.name)
        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val goToLogin = findViewById<android.widget.TextView>(R.id.goToLogin)

        goToLogin.setOnClickListener { finish() }

        registerBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            registerBtn.isEnabled = false
            val req = com.example.miniapp.network.RegisterRequest(name, email, password)
            com.example.miniapp.network.ApiClient.authService.register(req).enqueue(object: Callback<com.example.miniapp.network.AuthResponse> {
                override fun onResponse(call: Call<com.example.miniapp.network.AuthResponse>, response: Response<com.example.miniapp.network.AuthResponse>) {
                    registerBtn.isEnabled = true
                    if (response.isSuccessful) {
                        val cookieHeader = response.headers().get("Set-Cookie")
                        var token: String? = null
                        if (!cookieHeader.isNullOrBlank()) {
                            val kv = cookieHeader.split(';')[0]
                            if (kv.contains("=")) token = kv.substringAfter('=')
                        }
                        if (!token.isNullOrBlank()) {
                            SessionManager.saveSession(this@RegisterActivity, token, name, email)
                        }
                        Toast.makeText(this@RegisterActivity, "Account created! Please sign in.", Toast.LENGTH_SHORT).show()
                        // Go back to login rather than jumping to dashboard
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    } else {
                        val errBody = response.errorBody()?.string()
                        val msg = try {
                            org.json.JSONObject(errBody ?: "").optString("message", "Registration failed")
                        } catch (e: Exception) { "Registration failed (${response.code()})" }
                        Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<com.example.miniapp.network.AuthResponse>, t: Throwable) {
                    registerBtn.isEnabled = true
                    Toast.makeText(this@RegisterActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
