

**Contents**
- `backend/` — Spring Boot REST API (Maven)
- `web/` — React + Vite frontend
- `mobile/` — (mobile client folder)

## Project description

InStock is a recipe matching app aimed to reduce food waste. It allows users to:
- **Select ingredients** from categorized lists (dairy, poultry, meats, seafood, vegetables, fruits, etc.)
- **Find matching recipes** based on selected ingredients using the Spoonacular API
- **Search recipes** by name, cuisine, or dish type
- **Save favorites** to build a personal recipe collection
- **Manage ingredient selections** with persistent storage

## Technologies used

- **Backend:** Spring Boot 3.2.6, Spring Web, Spring Data JPA, Spring Security, jjwt, MySQL, RestTemplate
- **External API:** Spoonacular API for ingredient autocomplete and recipe search
- **Build:** Maven
- **Frontend:** React, Vite, React Router v6
- **Mobile:** (placeholder) — mobile client folder exists
- **Dev tools:** Lombok, Spring Boot DevTools
- **Database:** MySQL 8.0+ via MySQL Workbench

## Steps to run backend

1. Ensure Java 17 and Maven are installed.
	 - Check with `java -version` and `mvn -v`.
2. Start MySQL (XAMPP) and create the database and dedicated user:

```sql
CREATE DATABASE instock CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON instock.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

3. Run the database migration to create required tables:

```bash
# Via MySQL command line or phpMyAdmin
mysql -u root -p instock < docs/database-migration.sql
```

Or manually execute the SQL in `docs/database-migration.sql` via phpMyAdmin.

4. Update `backend/src/main/resources/application.properties` with your MySQL username/password, JWT secret, and Spoonacular API key.

5. From the repository root run:

```bash
mvn -f backend clean package
mvn -f backend spring-boot:run
```

The backend will start on port `3060` (configured in `application.properties`).

**Quick Start (Windows):** Use the provided launcher script:
```bash
.\run-all.bat
```
This starts both backend and frontend automatically.

## Steps to run web app

1. From the `web/` folder install dependencies and start the dev server:

```bash
# from repository root
cd web
npm install
npm run dev
```

2. Open the frontend in your browser (`http://localhost:5173` by default when Vite runs).

3. Register and login using the forms; the frontend will POST to `http://localhost:8080/api/auth/...` and store a JWT in `localStorage`.

## Steps to run mobile app

- The `mobile/` folder is a placeholder for the mobile client. If you have an existing mobile project, ensure its API base URL points to `http://<host>:8080` and that it attaches an `Authorization: Bearer <token>` header for protected endpoints.

## API endpoints

### Authentication (Public)
- `POST /api/auth/register` — register a new user
	- Request: `{ "name": "...", "email": "...", "password": "..." }`
	- Response 200: `{ "token": "...", "name": "...", "email": "..." }`
	- Errors: 400 (validation), 409 (email exists)

- `POST /api/auth/login` — login existing user
	- Request: `{ "email": "...", "password": "..." }`
	- Response 200: `{ "token": "...", "name": "...", "email": "..." }`
	- Errors: 400 (validation), 401 (invalid credentials)

### User Ingredients (Protected - requires `Authorization: Bearer <token>`)
- `GET /api/user-ingredients` — list user's saved ingredients
- `POST /api/user-ingredients` — save ingredient
	- Request: `{ "spoonacularId": 123, "name": "...", "aisle": "..." }`
- `DELETE /api/user-ingredients/spoonacular/{spoonacularId}` — remove ingredient by Spoonacular ID

### User Recipes (Protected)
- `GET /api/user-recipes` — list user's favorite recipes
- `POST /api/user-recipes` — save recipe to favorites
	- Request: `{ "spoonacularRecipeId": 456, "title": "...", "image": "...", "usedIngredients": 3, "missedIngredients": 2 }`
- `DELETE /api/user-recipes/spoonacular/{spoonacularRecipeId}` — remove recipe by Spoonacular ID

### Spoonacular Proxy (Protected)
- `GET /api/spoon/ingredients/autocomplete?query={q}&number={n}&metaInformation=true` — ingredient autocomplete
- `GET /api/spoon/recipes-by-ingredients?ingredients={csv}&number={n}` — find recipes by ingredients
- `GET /api/spoon/recipes/search?query={q}&number={n}` — search recipes by name/keyword

## Dev notes

- **Database Schema:** Manual migration via `docs/database-migration.sql`. JPA `ddl-auto=update` is disabled for safety.
- **JWT Secret:** Change `jwt.secret` in `application.properties` to a long random value (min 256 bits) before deploying.
- **CORS:** Configured to allow frontend origin (`http://localhost:5173` for Vite dev server).
- **Spoonacular API:** Free tier has 150 requests/day. API key is in `application.properties` as `spoonacular.api.key`.
- **Frontend Port:** Vite dev server runs on port 5173
- **Backend Port:** Spring Boot runs on port 3060
- **Database Migration:** Must be run manually before first use - see step 3 in "Steps to run backend"

## Project Structure

```
.
├── backend/               # Spring Boot REST API
│   ├── src/main/java/com/example/backend/
│   │   ├── controller/    # REST controllers (Auth, UserIngredient, UserRecipe, Spoonacular)
│   │   ├── service/       # Business logic
│   │   ├── repository/    # JPA repositories
│   │   ├── model/         # JPA entities (User, UserIngredient, UserRecipe)
│   │   ├── dto/           # Request/Response DTOs
│   │   ├── security/      # JWT utilities and filters
│   │   └── config/        # Security and CORS config
│   └── src/main/resources/
│       └── application.properties  # Database, JWT, Spoonacular config
├── web/                   # React + Vite frontend
│   ├── src/
│   │   ├── pages/         # Landing, Login, Register, Ingredients, Recipes, Favorites
│   │   ├── components/    # Navbar, IngredientSearch
│   │   ├── api.js         # API wrapper with JWT header injection
│   │   └── App.jsx        # Router configuration
│   └── public/
├── docs/
│   ├── database-migration.sql      # SQL schema for user_ingredients and user_recipes
│   └── DATABASE_MIGRATION.md       # Migration instructions
├── scripts/
│   ├── start-backend.ps1  # PowerShell script to start backend
│   └── start-frontend.ps1 # PowerShell script to start frontend
├── run-all.bat            # Windows launcher for both servers
├── README.md
└── TASKLIST.md
```

---

**Next Steps:**
- Run the database migration SQL
- Consider implementing caching for Spoonacular API responses
- Add unit tests for services and controllers
- Implement mobile client
- Add Docker support for deployment
