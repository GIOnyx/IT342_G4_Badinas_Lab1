package com.example.miniapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val name = intent.getStringExtra("name") ?: "guest"
        val welcome = findViewById<TextView>(R.id.welcome)
        welcome.text = "Welcome $name — this is a simple dashboard."

        val logout = findViewById<Button>(R.id.logoutBtn)
        logout.setOnClickListener {
            finish()
        }
    }
}
