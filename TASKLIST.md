# Project Task List

## Backend
- **Done:** Auth & JWT — `/api/auth/register`, `/api/auth/login` implemented
- **Done:** Datasource & properties — MySQL/XAMPP config in `application.properties`
- **Done:** CORS & SecurityConfig — JWT filter wired, CORS allowed for frontend
- **Done:** Validation & Error Handling — DTO validation and global handler
- **Done:** UserIngredient & UserRecipe entities — JPA models with repositories, services, controllers
- **Done:** Spoonacular API integration — ingredient autocomplete, recipe search, find by ingredients
- **Done:** Database migration SQL — `user_ingredients` and `user_recipes` tables
- **To Do:** Create DB tables — run `docs/database-migration.sql` in MySQL
- **To Do:** Unit/Integration tests — add Spring Boot tests
- **To Do:** Dockerfile & Compose — containerize backend + MySQL

## Web (React)
- **Done:** Routing & Pages — Login, Register, Landing, Ingredients, Recipes, Favorites
- **Done:** Auth integration — frontend registers/logins and stores JWT
- **Done:** UI Theme & Navbar — green health theme, sidebar navigation
- **Done:** Landing page — hero section with call-to-action, features section
- **Done:** Ingredients page — category-based selection, ingredient autocomplete
- **Done:** Recipes page — search recipes by name, display suggestions from ingredients
- **Done:** Favorites page — saved recipes with remove functionality
- **Done:** Authorization header — automatically attached via api.js wrapper
- **Done:** Logout functionality — clears session and redirects to landing page
- **Done:** Launcher scripts — `run-all.bat` with PowerShell helpers

## Mobile
- **To Do:** Scaffold client — basic screens (Login, Ingredients, Recipes, Favorites)
- **To Do:** Auth flow — register/login and persist token
- **To Do:** Ingredient selection — consume backend APIs
- **To Do:** Recipe browsing — display and save recipes

---
Notes:
- **Database Setup Required:** Run `docs/database-migration.sql` to create `user_ingredients` and `user_recipes` tables
- **Spoonacular API:** Free tier has 150 requests/day limit. Consider upgrading or implementing caching for production.
- **Workflow:** Ingredients tab → select ingredients → Find Recipes → redirects to Recipes tab with suggestions
- **Tech Stack:** Spring Boot 3.2.6, React + Vite, Spoonacular API, MySQL, JWT authentication