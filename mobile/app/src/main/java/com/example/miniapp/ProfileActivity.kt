package com.example.miniapp

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar

class ProfileActivity : AuthBaseActivity() {
    private lateinit var nameView: TextView
    private lateinit var emailView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Back arrow — navigates up to DashboardActivity
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nameView  = findViewById(R.id.profileName)
        emailView = findViewById(R.id.profileEmail)

        // Show cached values immediately while validation runs
        nameView.text  = SessionManager.getName(this).ifBlank { "…" }
        emailView.text = SessionManager.getEmail(this).ifBlank { "…" }
    }

    override fun onSessionValidated(profile: Map<String, String>) {
        nameView.text  = profile["name"]  ?: profile["username"] ?: "-"
        emailView.text = profile["email"] ?: "-"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
