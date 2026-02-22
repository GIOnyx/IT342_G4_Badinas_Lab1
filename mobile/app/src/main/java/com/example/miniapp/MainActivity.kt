package com.example.miniapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miniapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button?>(R.id.openLogin)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        findViewById<Button?>(R.id.openRegister)?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Auto-login: if a valid token is stored, skip this screen
        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            ApiClient.authService.validate("token=$token")
                .enqueue(object : Callback<Map<String, String>> {
                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        if (response.isSuccessful) {
                            startActivity(
                                Intent(this@MainActivity, DashboardActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            )
                        } else {
                            SessionManager.clearSession(this@MainActivity)
                        }
                    }

                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        // Network unavailable — stay on this screen
                    }
                })
        }
    }
}