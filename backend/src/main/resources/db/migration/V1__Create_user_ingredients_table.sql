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

-- Add index for better query performance
CREATE INDEX idx_user_ingredients_user ON user_ingredients(user_id, created_at);
