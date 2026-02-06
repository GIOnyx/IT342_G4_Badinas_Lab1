package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.repository.UserRepository;
import com.example.backend.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

// Temporary debug endpoints — remove in production
@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/check-password")
    public ResponseEntity<Map<String, Object>> checkPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "email and password required"));
        }
        User u = userRepository.findByEmail(email).orElse(null);
        if (u == null) {
            return ResponseEntity.ok(Map.of("exists", false));
        }
        boolean matches = passwordEncoder.matches(password, u.getPassword());
        return ResponseEntity.ok(Map.of("exists", true, "matches", matches));
    }
}
