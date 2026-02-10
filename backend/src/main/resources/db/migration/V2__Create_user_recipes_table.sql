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

-- Add index for better query performance
CREATE INDEX idx_user_recipes_user ON user_recipes(user_id, created_at);
