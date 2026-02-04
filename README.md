

**Contents**
- `backend/` — Spring Boot REST API (Maven)
- `web/` — React + Vite frontend
- `mobile/` — (mobile client folder)

## Project description

InStock is a recipe matching app that is aimed to reduce fodd waste. It allows the user to select ingredients that they have in stock and the system suggests matching recipes.

## Technologies used

- Backend: Spring Boot 3, Spring Web, Spring Data JPA, Spring Security, jjwt, MySQL (XAMPP testing)
- Build: Maven
- Frontend: React, Vite, react-router-dom
- Mobile: (placeholder) — mobile client folder exists
- Dev tools: Lombok, Spring Boot DevTools

## Steps to run backend

1. Ensure Java 17 and Maven are installed.
	 - Check with `java -version` and `mvn -v`.
2. Start MySQL (XAMPP Control Panel) and create the database and dedicated user:

```sql
CREATE DATABASE lab1_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'lab1'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON lab1_db.* TO 'lab1'@'localhost';
FLUSH PRIVILEGES;
```

3. Update `backend/src/main/resources/application.properties` with your MySQL username/password and change `jwt.secret` to a secure value.

4. From the repository root run:

```bash
mvn -f backend clean package
mvn -f backend spring-boot:run
```

The backend will start on port `8080` by default.

## Steps to run web app

1. From the `web/` folder install dependencies and start the dev server:

```bash
# from repository root
cd web
npm install
npm run dev
```

2. Open the frontend in your browser (`http://localhost:5174` by default when Vite runs).

3. Register and login using the forms; the frontend will POST to `http://localhost:8080/api/auth/...` and store a JWT in `localStorage`.

## Steps to run mobile app

- The `mobile/` folder is a placeholder for the mobile client. If you have an existing mobile project, ensure its API base URL points to `http://<host>:8080` and that it attaches an `Authorization: Bearer <token>` header for protected endpoints.

## API endpoints

Authentication
- `POST /api/auth/register` — register a new user
	- Request JSON: `{ "name": "...", "email": "...", "password": "..." }`
	- Response 200: `{ "token": "...", "name": "...", "email": "..." }`
	- Possible errors: 400 (validation), 409 (email exists)

- `POST /api/auth/login` — login existing user
	- Request JSON: `{ "email": "...", "password": "..." }`
	- Response 200: `{ "token": "...", "name": "...", "email": "..." }`
	- Possible errors: 400 (validation), 401 (invalid credentials)

Protected endpoints (require `Authorization: Bearer <token>`)
- `GET /api/items` — list items
- `GET /api/items/{id}` — item details
- `POST /api/items` — create item
- `PUT /api/items/{id}` — update item
- `DELETE /api/items/{id}` — delete item

Note: The above item endpoints are scaffolded on the frontend; implement the corresponding controllers/repositories in `backend/` if not already present.

## Dev notes

- For local development `spring.jpa.hibernate.ddl-auto=update` is enabled (creates/updates schema). Switch to migrations (Flyway/Liquibase) for production.
- Change `jwt.secret` in `backend/src/main/resources/application.properties` to a long random value before deploying.
- CORS is configured to allow the frontend origin by default (`http://localhost:5174`).

---

If you want, I can: add a Dockerfile for the backend, wire the item controllers on the backend to match the frontend, or implement logout and attaching Authorization headers for all frontend API calls. Which should I do next?
