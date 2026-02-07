package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_recipes")
public class UserRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "spoonacular_recipe_id", nullable = false)
    private Integer spoonacularRecipeId;

    @Column(nullable = false)
    private String title;

    private String image;

    @Column(name = "used_ingredients")
    private Integer usedIngredients;

    @Column(name = "missed_ingredients")
    private Integer missedIngredients;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public UserRecipe() {}

    public UserRecipe(User user, Integer spoonacularRecipeId, String title, String image, Integer usedIngredients, Integer missedIngredients) {
        this.user = user;
        this.spoonacularRecipeId = spoonacularRecipeId;
        this.title = title;
        this.image = image;
        this.usedIngredients = usedIngredients;
        this.missedIngredients = missedIngredients;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getSpoonacularRecipeId() {
        return spoonacularRecipeId;
    }

    public void setSpoonacularRecipeId(Integer spoonacularRecipeId) {
        this.spoonacularRecipeId = spoonacularRecipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(Integer usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public Integer getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(Integer missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
