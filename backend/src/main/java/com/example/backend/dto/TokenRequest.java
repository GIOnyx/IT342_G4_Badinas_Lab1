package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for POST /api/auth/token (PKCE step 2).
 * The client sends the one-time authorization code it received from /authorize
 * together with the original code_verifier so the server can confirm integrity.
 */
public class TokenRequest {

    @NotBlank
    private String code;

    /** The random secret the client generated before step 1. */
    @NotBlank
    private String codeVerifier;

    public TokenRequest() {}

    public String getCode()         { return code; }
    public String getCodeVerifier() { return codeVerifier; }

    public void setCode(String code)                 { this.code = code; }
    public void setCodeVerifier(String codeVerifier) { this.codeVerifier = codeVerifier; }
}
