# Database Schema Update

## New Tables Added

Two new tables have been created to properly store user favorites:

### 1. `user_ingredients` table
Stores ingredients that users have selected/favorited:
- `id` - Primary key
- `user_id` - Foreign key to users table
- `spoonacular_id` - The ingredient ID from Spoonacular API
- `name` - Ingredient name
- `aisle` - Aisle/category information
- `created_at` - Timestamp when saved

### 2. `user_recipes` table
Stores recipes that users have saved as favorites:
- `id` - Primary key
- `user_id` - Foreign key to users table
- `spoonacular_recipe_id` - The recipe ID from Spoonacular API
- `title` - Recipe title
- `image` - Recipe image URL
- `used_ingredients` - Count of ingredients the user has
- `missed_ingredients` - Count of ingredients the user needs
- `created_at` - Timestamp when saved

## How to Apply the Migration

### Using phpMyAdmin (XAMPP):
1. Open phpMyAdmin (http://localhost/phpmyadmin)
2. Select your database (e.g., `miniapp` or whatever you named it)
3. Click the "SQL" tab
4. Copy the contents of `docs/database-migration.sql`
5. Paste into the SQL query box
6. Click "Go" to execute

### Using MySQL Workbench:
1. Open MySQL Workbench
2. Connect to your local MySQL server
3. Open the SQL script: `docs/database-migration.sql`
4. Execute the script

### Using Command Line:
```bash
mysql -u root -p your_database_name < docs/database-migration.sql
```

## Backend Changes

### New Entities:
- `UserIngredient.java` - JPA entity for user ingredients
- `UserRecipe.java` - JPA entity for user recipes

### New Repositories:
- `UserIngredientRepository.java`
- `UserRecipeRepository.java`

### New Services:
- `UserIngredientService.java`
- `UserRecipeService.java`

### New Controllers:
- `UserIngredientController.java` - REST API at `/api/user-ingredients`
- `UserRecipeController.java` - REST API at `/api/user-recipes`

### New DTOs:
- `UserIngredientRequest.java`
- `UserRecipeRequest.java`

## Frontend Changes

### Updated Pages:
- `Favorites.jsx` - Now displays saved recipes from `/api/user-recipes`
- `Ingredients.jsx` - Added "Save to Favorites" button on recipe cards
- `Recipes.jsx` - Added "Save to Favorites" button on recipe cards

## API Endpoints

### User Ingredients:
- `GET /api/user-ingredients` - Get all saved ingredients for current user
- `POST /api/user-ingredients` - Save an ingredient
- `DELETE /api/user-ingredients/{id}` - Remove by database ID
- `DELETE /api/user-ingredients/spoonacular/{spoonacularId}` - Remove by Spoonacular ID

### User Recipes:
- `GET /api/user-recipes` - Get all saved recipes for current user
- `POST /api/user-recipes` - Save a recipe
- `DELETE /api/user-recipes/{id}` - Remove by database ID
- `DELETE /api/user-recipes/spoonacular/{spoonacularRecipeId}` - Remove by Spoonacular ID

## What's Next

After running the migration:
1. Restart your backend server to load the new entities
2. Refresh the frontend
3. Test saving recipes from Ingredients or Recipes pages
4. Check Favorites page to see saved recipes

## Old `items` Table

The old `items` table is still in your database but is no longer used by the frontend. You can keep it for reference or drop it if you don't need it:

```sql
-- Optional: Drop old items table if not needed
DROP TABLE IF EXISTS items;
```
