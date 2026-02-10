package com.example.backend.controller;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private com.example.backend.security.JwtUtil jwtUtil;

    @Autowired
    private com.example.backend.repository.UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@jakarta.validation.Valid @RequestBody RegisterRequest req) {
        AuthResponse resp = authService.register(req);
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", resp.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtUtil.getExpirationMs() / 1000));
        cookie.setSecure(false);
        com.example.backend.dto.AuthResponse body = new com.example.backend.dto.AuthResponse(null, resp.getName(), resp.getEmail());
        return ResponseEntity.ok().header("Set-Cookie", cookieToHeader(cookie)).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@jakarta.validation.Valid @RequestBody AuthRequest req) {
        AuthResponse resp = authService.login(req);
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", resp.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtUtil.getExpirationMs() / 1000));
        cookie.setSecure(false);
        com.example.backend.dto.AuthResponse body = new com.example.backend.dto.AuthResponse(null, resp.getName(), resp.getEmail());
        return ResponseEntity.ok().header("Set-Cookie", cookieToHeader(cookie)).body(body);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@org.springframework.web.bind.annotation.CookieValue(value = "token", required = false) String token, Authentication authentication) {
        // First, try token from cookie (works when cookie present)
        if (token != null && !token.isBlank()) {
            try {
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    return userRepository.findByEmail(username)
                            .map(u -> ResponseEntity.ok(java.util.Map.of("name", u.getName(), "email", u.getEmail())))
                            .orElseGet(() -> ResponseEntity.ok(java.util.Map.of("username", username)));
                }
                return ResponseEntity.status(401).body(null);
            } catch (Exception ex) {
                return ResponseEntity.status(401).body(null);
            }
        }
        // Fallback to Authentication if cookie missing
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }
        String username = authentication.getName();
        if (username != null) {
            return userRepository.findByEmail(username)
                    .map(u -> ResponseEntity.ok(java.util.Map.of("name", u.getName(), "email", u.getEmail())))
                    .orElseGet(() -> ResponseEntity.ok(java.util.Map.of("username", username)));
        }
        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(false);
        return ResponseEntity.ok().header("Set-Cookie", cookieToHeader(cookie)).body(null);
    }

    private String cookieToHeader(jakarta.servlet.http.Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.getName()).append("=").append(cookie.getValue());
        sb.append("; Path=").append(cookie.getPath() != null ? cookie.getPath() : "/");
        if (cookie.getMaxAge() >= 0) sb.append("; Max-Age=").append(cookie.getMaxAge());
        if (cookie.isHttpOnly()) sb.append("; HttpOnly");
        if (cookie.getSecure()) sb.append("; Secure");
        // Only append SameSite=None when cookie is Secure. Modern browsers
        // reject cookies that declare SameSite=None without Secure set.
        if (cookie.getSecure()) {
            sb.append("; SameSite=None");
        }
        return sb.toString();
    }
}
