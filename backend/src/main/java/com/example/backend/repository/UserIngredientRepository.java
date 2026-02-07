package com.example.backend.repository;

import com.example.backend.model.User;
import com.example.backend.model.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {
    List<UserIngredient> findByUser(User user);
    List<UserIngredient> findByUserOrderByCreatedAtDesc(User user);
    Optional<UserIngredient> findByUserAndSpoonacularId(User user, Integer spoonacularId);
    boolean existsByUserAndSpoonacularId(User user, Integer spoonacularId);
}
