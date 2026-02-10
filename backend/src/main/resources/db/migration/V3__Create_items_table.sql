-- Create items table (legacy table, kept for reference)
CREATE TABLE IF NOT EXISTS items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    subtitle VARCHAR(255),
    content TEXT
);
