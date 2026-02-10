-- Database migration script for UserIngredient and UserRecipe tables
-- Run this in your MySQL database (XAMPP phpMyAdmin or MySQL Workbench)

-- Create user_ingredients table
CREATE TABLE IF NOT EXISTS user_ingredients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    spoonacular_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    aisle VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_ingredient (user_id, spoonacular_id)
);

-- Create user_recipes table
CREATE TABLE IF NOT EXISTS user_recipes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    spoonacular_recipe_id INT NOT NULL,
    title VARCHAR(500) NOT NULL,
    image VARCHAR(500),
    used_ingredients INT,
    missed_ingredients INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_recipe (user_id, spoonacular_recipe_id)
);

-- Add indexes for better query performance
CREATE INDEX idx_user_ingredients_user ON user_ingredients(user_id, created_at);
CREATE INDEX idx_user_recipes_user ON user_recipes(user_id, created_at);
