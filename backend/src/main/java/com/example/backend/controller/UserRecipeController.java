package com.example.backend.controller;

import com.example.backend.dto.UserRecipeRequest;
import com.example.backend.model.User;
import com.example.backend.model.UserRecipe;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import com.example.backend.service.UserRecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-recipes")
public class UserRecipeController {
    private final UserRecipeService userRecipeService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserRecipeController(UserRecipeService userRecipeService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRecipeService = userRecipeService;
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
    public ResponseEntity<List<UserRecipe>> getUserRecipes(@RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        List<UserRecipe> recipes = userRecipeService.getUserRecipes(user);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping
    public ResponseEntity<UserRecipe> saveRecipe(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserRecipeRequest request) {
        User user = getCurrentUser(authHeader);
        UserRecipe saved = userRecipeService.saveRecipe(user, request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        User user = getCurrentUser(authHeader);
        userRecipeService.deleteRecipe(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/spoonacular/{spoonacularRecipeId}")
    public ResponseEntity<Void> deleteBySpoonacularId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer spoonacularRecipeId) {
        User user = getCurrentUser(authHeader);
        userRecipeService.deleteBySpoonacularId(user, spoonacularRecipeId);
        return ResponseEntity.noContent().build();
    }
}
