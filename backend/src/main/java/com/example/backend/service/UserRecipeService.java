package com.example.backend.service;

import com.example.backend.dto.UserRecipeRequest;
import com.example.backend.model.User;
import com.example.backend.model.UserRecipe;
import com.example.backend.repository.UserRecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRecipeService {
    private final UserRecipeRepository userRecipeRepository;

    public UserRecipeService(UserRecipeRepository userRecipeRepository) {
        this.userRecipeRepository = userRecipeRepository;
    }

    @Transactional(readOnly = true)
    public List<UserRecipe> getUserRecipes(User user) {
        return userRecipeRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public UserRecipe saveRecipe(User user, UserRecipeRequest request) {
        // Check if already saved
        if (userRecipeRepository.existsByUserAndSpoonacularRecipeId(user, request.getSpoonacularRecipeId())) {
            throw new IllegalStateException("Recipe already saved");
        }

        UserRecipe recipe = new UserRecipe(
            user,
            request.getSpoonacularRecipeId(),
            request.getTitle(),
            request.getImage(),
            request.getUsedIngredients(),
            request.getMissedIngredients()
        );
        return userRecipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipe(User user, Long id) {
        UserRecipe recipe = userRecipeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Not authorized to delete this recipe");
        }
        
        userRecipeRepository.delete(recipe);
    }

    @Transactional
    public void deleteBySpoonacularId(User user, Integer spoonacularRecipeId) {
        UserRecipe recipe = userRecipeRepository.findByUserAndSpoonacularRecipeId(user, spoonacularRecipeId)
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        
        userRecipeRepository.delete(recipe);
    }
}
