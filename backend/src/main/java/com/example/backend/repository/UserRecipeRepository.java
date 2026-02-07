package com.example.backend.repository;

import com.example.backend.model.User;
import com.example.backend.model.UserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRecipeRepository extends JpaRepository<UserRecipe, Long> {
    List<UserRecipe> findByUser(User user);
    List<UserRecipe> findByUserOrderByCreatedAtDesc(User user);
    Optional<UserRecipe> findByUserAndSpoonacularRecipeId(User user, Integer spoonacularRecipeId);
    boolean existsByUserAndSpoonacularRecipeId(User user, Integer spoonacularRecipeId);
}
