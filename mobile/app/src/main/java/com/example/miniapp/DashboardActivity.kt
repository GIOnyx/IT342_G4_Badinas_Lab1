package com.example.miniapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DashboardActivity : AuthBaseActivity() {
    private lateinit var welcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        welcome = findViewById(R.id.welcome)
        val name = intent.getStringExtra("name")
            ?: SessionManager.getName(this).ifBlank { "guest" }
        welcome.text = "Welcome $name — this is your dashboard."

        findViewById<Button>(R.id.logoutBtn).setOnClickListener { logout() }
        findViewById<Button>(R.id.profileBtn).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onSessionValidated(profile: Map<String, String>) {
        val serverName = profile["name"] ?: profile["username"] ?: return
        welcome.text = "Welcome $serverName — this is your dashboard."
        SessionManager.saveSession(
            this,
            SessionManager.getToken(this) ?: "",
            serverName,
            profile["email"]
        )
    }
}
