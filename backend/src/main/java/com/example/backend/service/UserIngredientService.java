package com.example.backend.service;

import com.example.backend.dto.UserIngredientRequest;
import com.example.backend.model.User;
import com.example.backend.model.UserIngredient;
import com.example.backend.repository.UserIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserIngredientService {
    private final UserIngredientRepository userIngredientRepository;

    public UserIngredientService(UserIngredientRepository userIngredientRepository) {
        this.userIngredientRepository = userIngredientRepository;
    }

    @Transactional(readOnly = true)
    public List<UserIngredient> getUserIngredients(User user) {
        return userIngredientRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public UserIngredient saveIngredient(User user, UserIngredientRequest request) {
        // Check if already saved
        if (userIngredientRepository.existsByUserAndSpoonacularId(user, request.getSpoonacularId())) {
            throw new IllegalStateException("Ingredient already saved");
        }

        UserIngredient ingredient = new UserIngredient(
            user,
            request.getSpoonacularId(),
            request.getName(),
            request.getAisle()
        );
        return userIngredientRepository.save(ingredient);
    }

    @Transactional
    public void deleteIngredient(User user, Long id) {
        UserIngredient ingredient = userIngredientRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        
        if (!ingredient.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Not authorized to delete this ingredient");
        }
        
        userIngredientRepository.delete(ingredient);
    }

    @Transactional
    public void deleteBySpoonacularId(User user, Integer spoonacularId) {
        UserIngredient ingredient = userIngredientRepository.findByUserAndSpoonacularId(user, spoonacularId)
            .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        
        userIngredientRepository.delete(ingredient);
    }
}
