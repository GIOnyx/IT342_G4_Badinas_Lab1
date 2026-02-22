package com.example.miniapp

import android.content.Context
import android.content.SharedPreferences

/**
 * Central token/session store — mirrors the web's HttpOnly cookie handling.
 * The JWT is stored in private SharedPreferences (never in logs or public storage).
 */
object SessionManager {
    private const val PREFS = "miniapp"
    private const val KEY_TOKEN = "token"
    private const val KEY_NAME  = "name"
    private const val KEY_EMAIL = "email"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun saveSession(ctx: Context, token: String, name: String?, email: String?) {
        prefs(ctx).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NAME,  name  ?: "")
            .putString(KEY_EMAIL, email ?: "")
            .apply()
    }

    fun getToken(ctx: Context): String? = prefs(ctx).getString(KEY_TOKEN, null)
    fun getName (ctx: Context): String  = prefs(ctx).getString(KEY_NAME,  "") ?: ""
    fun getEmail(ctx: Context): String  = prefs(ctx).getString(KEY_EMAIL, "") ?: ""

    fun clearSession(ctx: Context) {
        prefs(ctx).edit().clear().apply()
    }

    fun hasToken(ctx: Context): Boolean = !getToken(ctx).isNullOrBlank()
}
