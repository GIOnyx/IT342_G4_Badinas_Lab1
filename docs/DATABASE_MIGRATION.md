# Database Schema Management

## Overview

This project uses **Flyway** for automated database migrations, ensuring consistent schema updates across all environments. Migrations run automatically on application startup.

## Migration Strategy

### Automated with Flyway
All database schema changes are managed through versioned SQL migration scripts located in:
```
backend/src/main/resources/db/migration/
```

Flyway automatically executes pending migrations when the application starts, eliminating the need for manual SQL execution.

## Current Schema Tables

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

## How Migrations Work

### Automatic Migration (Recommended)
1. Ensure your database connection is configured in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=root
   spring.datasource.password=
   ```
2. Start the Spring Boot application
3. Flyway automatically detects and runs pending migrations
4. Check logs for migration status:
   ```
   Flyway: Successfully applied 2 migrations
   ```

### Migration Files
- **V1__Create_user_ingredients_table.sql** - Creates user_ingredients table
- **V2__Create_user_recipes_table.sql** - Creates user_recipes table

### Manual Migration (Legacy - Not Recommended)
If you prefer manual execution, the migration SQL is also available in:
```
docs/database-migration.sql
```

However, this approach is deprecated in favor of automated Flyway migrations for better:
- Version control
- Repeatability
- Error handling
- Production deployment

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
- `DELsetting up Flyway:
1. Configure your database connection in `application.properties`
2. Start the backend server - migrations run automatically
3. Verify migration success in application logs
4. Test saving recipes from Ingredients or Recipes pages
5. Check Favorites page to see saved recipes

## Configuration

Flyway is configured in `application.properties`:
```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

### JPA Configuration
```properties
spring.jpa.hibernate.ddl-auto=validate
```
Set to `validate` to ensure Flyway manages all schema changes.

## Migration Best Practices

1. **Never modify existing migrations** - Create new versioned migrations instead
2. **Test migrations locally** before deploying to production
3. **Use meaningful version numbers** - V1, V2, V3... for sequential changes
4. **Include rollback plans** - Document how to reverse changes if needed
5. **Keep migrations idempotent** - Use `IF NOT EXISTS` where appropriatecipes for current user
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
