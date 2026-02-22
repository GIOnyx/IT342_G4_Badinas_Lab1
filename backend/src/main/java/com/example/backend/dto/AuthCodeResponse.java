package com.example.backend.dto;

/**
 * Response body for POST /api/auth/authorize.
 * Contains a short-lived, single-use authorization code.
 */
public class AuthCodeResponse {

    private final String code;

    public AuthCodeResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
