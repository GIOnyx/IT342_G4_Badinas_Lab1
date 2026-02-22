package com.example.backend.service;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PkceStore pkceStore;

    // ── PKCE flow ────────────────────────────────────────────────────────────

    /**
     * PKCE step 1: validate credentials, store the code_challenge, and return a
     * short-lived single-use authorization code.
     */
    public com.example.backend.dto.AuthCodeResponse authorize(
            com.example.backend.dto.AuthorizeRequest req) {
        User u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new com.example.backend.exception.InvalidCredentialsException(
                        "Invalid email or password"));
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new com.example.backend.exception.InvalidCredentialsException(
                    "Invalid email or password");
        }
        if (!"S256".equals(req.getCodeChallengeMethod())) {
            throw new IllegalArgumentException("Only S256 code_challenge_method is supported");
        }
        String code = pkceStore.store(u.getEmail(), req.getCodeChallenge());
        return new com.example.backend.dto.AuthCodeResponse(code);
    }

    /**
     * PKCE step 2: consume the authorization code, verify the code_verifier
     * against the stored code_challenge, and issue a JWT.
     */
    public AuthResponse exchangeToken(
            com.example.backend.dto.TokenRequest req) throws java.security.NoSuchAlgorithmException {
        PkceStore.Entry entry = pkceStore.consume(req.getCode());
        if (entry == null) {
            throw new com.example.backend.exception.InvalidCredentialsException(
                    "Invalid or expired authorization code");
        }
        // RFC 7636 §4.6: verify BASE64URL(SHA-256(ASCII(code_verifier))) == code_challenge
        java.security.MessageDigest digest =
                java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                req.getCodeVerifier().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        String computed = java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(hash);
        if (!computed.equals(entry.codeChallenge)) {
            throw new com.example.backend.exception.InvalidCredentialsException(
                    "PKCE code_verifier mismatch");
        }
        User u = userRepository.findByEmail(entry.email)
                .orElseThrow(() -> new com.example.backend.exception.InvalidCredentialsException(
                        "User not found"));
        String token = jwtUtil.generateToken(u.getEmail());
        return new AuthResponse(token, u.getName(), u.getEmail());
    }

    // ── Standard auth ─────────────────────────────────────────────────────────

    public AuthResponse register(RegisterRequest req) {
        Optional<User> exists = userRepository.findByEmail(req.getEmail());
        if (exists.isPresent()) {
            throw new com.example.backend.exception.UserAlreadyExistsException("A user with that email already exists");
        }
        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(u);
        String token = jwtUtil.generateToken(u.getEmail());
        return new AuthResponse(token, u.getName(), u.getEmail());
    }

    public AuthResponse login(AuthRequest req) {
        User u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new com.example.backend.exception.InvalidCredentialsException("Invalid email or password"));
        boolean matches = passwordEncoder.matches(req.getPassword(), u.getPassword());
        logger.debug("Login attempt for email {}: password matches={}", req.getEmail(), matches);
        if (!matches) {
            throw new com.example.backend.exception.InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(u.getEmail());
        return new AuthResponse(token, u.getName(), u.getEmail());
    }
}
