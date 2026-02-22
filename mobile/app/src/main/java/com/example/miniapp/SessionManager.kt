package com.example.miniapp

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Central token/session store — REQ-2.3.
 * The JWT is encrypted at rest using AES-256-GCM backed by the Android Keystore.
 * EncryptedSharedPreferences wraps the standard SharedPreferences API so the rest
 * of the codebase is unaffected.
 */
object SessionManager {
    private const val PREFS     = "miniapp_secure"
    private const val KEY_TOKEN = "token"
    private const val KEY_NAME  = "name"
    private const val KEY_EMAIL = "email"

    /** Returns an EncryptedSharedPreferences instance backed by the Android Keystore. */
    private fun prefs(ctx: Context): SharedPreferences {
        return try {
            buildEncryptedPrefs(ctx)
        } catch (e: Exception) {
            // If the Keystore key is lost (e.g. factory reset with no backup),
            // the existing file is unreadable — delete it and start fresh.
            ctx.deleteSharedPreferences(PREFS)
            buildEncryptedPrefs(ctx)
        }
    }

    private fun buildEncryptedPrefs(ctx: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(ctx)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            ctx,
            PREFS,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

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
