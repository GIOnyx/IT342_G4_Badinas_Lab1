package com.example.miniapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameInput = findViewById<EditText>(R.id.name)
        val emailInput = findViewById<EditText>(R.id.email)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {
            val name = nameInput.text.toString().ifEmpty { emailInput.text.toString().split('@').firstOrNull() ?: "User" }
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}
