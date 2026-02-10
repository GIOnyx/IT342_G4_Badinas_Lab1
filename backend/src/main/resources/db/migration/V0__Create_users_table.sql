-- Create users table (baseline table for authentication)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Add index for faster email lookups during authentication
CREATE INDEX idx_users_email ON users(email);
