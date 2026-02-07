package com.example.backend.controller;

import com.example.backend.dto.UserIngredientRequest;
import com.example.backend.model.User;
import com.example.backend.model.UserIngredient;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import com.example.backend.service.UserIngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-ingredients")
public class UserIngredientController {
    private final UserIngredientService userIngredientService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserIngredientController(UserIngredientService userIngredientService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.userIngredientService = userIngredientService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private User getCurrentUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No valid token provided");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<UserIngredient>> getUserIngredients(@RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        List<UserIngredient> ingredients = userIngredientService.getUserIngredients(user);
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping
    public ResponseEntity<UserIngredient> saveIngredient(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserIngredientRequest request) {
        User user = getCurrentUser(authHeader);
        UserIngredient saved = userIngredientService.saveIngredient(user, request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        User user = getCurrentUser(authHeader);
        userIngredientService.deleteIngredient(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/spoonacular/{spoonacularId}")
    public ResponseEntity<Void> deleteBySpoonacularId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer spoonacularId) {
        User user = getCurrentUser(authHeader);
        userIngredientService.deleteBySpoonacularId(user, spoonacularId);
        return ResponseEntity.noContent().build();
    }
}
