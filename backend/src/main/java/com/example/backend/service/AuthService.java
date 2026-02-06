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
