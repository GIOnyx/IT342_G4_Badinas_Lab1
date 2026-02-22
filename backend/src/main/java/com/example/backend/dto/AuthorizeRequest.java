package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for POST /api/auth/authorize (PKCE step 1).
 * The client sends credentials together with a code_challenge so the server
 * can later verify the code_verifier during the token exchange.
 */
public class AuthorizeRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    /** BASE64URL(SHA-256(code_verifier)) */
    @NotBlank
    private String codeChallenge;

    /** Must be "S256" — plain is not accepted. */
    @NotBlank
    private String codeChallengeMethod;

    public AuthorizeRequest() {}

    public String getEmail()               { return email; }
    public String getPassword()            { return password; }
    public String getCodeChallenge()       { return codeChallenge; }
    public String getCodeChallengeMethod() { return codeChallengeMethod; }

    public void setEmail(String email)                             { this.email = email; }
    public void setPassword(String password)                       { this.password = password; }
    public void setCodeChallenge(String codeChallenge)             { this.codeChallenge = codeChallenge; }
    public void setCodeChallengeMethod(String codeChallengeMethod) { this.codeChallengeMethod = codeChallengeMethod; }
}
