package com.example.miniapp.security

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Proof Key for Code Exchange (PKCE) — RFC 7636.
 *
 * Usage:
 *   val verifier   = PkceHelper.generateCodeVerifier()   // keep secret, send after
 *   val challenge  = PkceHelper.generateCodeChallenge(verifier)  // send upfront
 */
object PkceHelper {

    private val FLAGS = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP

    /**
     * Generate a cryptographically random code_verifier.
     * 32 random bytes → 43-char URL-safe base64 string (no padding).
     */
    fun generateCodeVerifier(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, FLAGS)
    }

    /**
     * Derive the code_challenge from the verifier using method S256:
     *   code_challenge = BASE64URL(SHA-256(ASCII(code_verifier)))
     */
    fun generateCodeChallenge(verifier: String): String {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(verifier.toByteArray(Charsets.US_ASCII))
        return Base64.encodeToString(hash, FLAGS)
    }
}
