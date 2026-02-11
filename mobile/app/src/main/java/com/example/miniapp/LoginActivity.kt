package com.example.miniapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.email)
        val signInBtn = findViewById<Button>(R.id.signInBtn)

        signInBtn.setOnClickListener {
            val name = emailInput.text.toString().split('@').firstOrNull() ?: "User"
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}
