

**Contents**
- `backend/` — Spring Boot REST API (Maven)
- `web/` — React + Vite frontend
# MiniApp — Login/Register Demo

This repository is a small demo focused on user authentication (register, login, validate, logout). It is intentionally minimal — the purpose is to demonstrate the authentication flow, not to be a full product.

Contents
- `backend/` — Spring Boot REST API (Maven)
- `web/` — React + Vite frontend

Quick notes about auth in this demo
- The backend sets an HttpOnly cookie named `token` on successful login/register.
- The frontend uses `fetch(..., { credentials: 'include' })` and the Vite dev proxy so browser requests include the cookie in development.
- For production you must use HTTPS and set the cookie `Secure` and rotate `jwt.secret`.

# MiniApp — Login/Register Demo

This repository is a small demo focused on user authentication: register, login, validate, and logout. It is intentionally minimal and intended for learning and experimentation.

**Contents**
- `backend/` — Spring Boot REST API (Maven)
- `web/` — React + Vite frontend
- `mobile/` — Android app (Gradle)

**Project description**
- A tiny full-stack demo showing email/password registration and JWT-based authentication using an HttpOnly cookie named `token`.
- Frontend communicates with the backend using `/api/*` endpoints; the backend issues a cookie on login/register and validates it on protected routes.

**Tech stack**
- Backend: Spring Boot, Spring Data JPA, Spring Security, MySQL
- Frontend: React + Vite
- Mobile: Android (Gradle)

Prerequisites
- Java 17 or later
- Maven (the project includes the Maven wrapper `mvnw`)
- Node.js + npm
- MySQL server (local or remote)
- Android Studio or Gradle (for mobile)

-------------------------
How to run the backend (development)

1. From repository root:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

2. The backend defaults to port `8080`.

Notes
- The backend will attempt to connect to MySQL using the settings in `backend/src/main/resources/application.properties`.
- If `spring.jpa.hibernate.ddl-auto=update` (default here), Hibernate will create/update the `users` table automatically.

How to run the web frontend (development)

1. From repository root:

```bash
cd web
npm install
npm run dev
```

2. Vite will print a local URL (typically `http://localhost:5173`). The frontend uses a Vite dev proxy so requests to `/api` are forwarded to the backend in development. The frontend uses `fetch(..., { credentials: 'include' })` to include the `token` cookie.

How to run the mobile app (development)

Open the `mobile` or `app` module in Android Studio and run on an emulator or device. Or from command line (Windows):

```powershell
cd mobile
.\gradlew.bat assembleDebug
```

You can then install the generated APK or run from Android Studio.

-------------------------
Database setup (MySQL)

The app expects a MySQL database `lab1_db` and a user matching the credentials in `application.properties`.

Quick SQL to create the database and user (run as a MySQL root/admin user):

```sql
CREATE DATABASE IF NOT EXISTS lab1_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'labuser'@'localhost' IDENTIFIED BY 'lab1pass';
GRANT ALL PRIVILEGES ON lab1_db.* TO 'labuser'@'localhost';
FLUSH PRIVILEGES;
```

Because `spring.jpa.hibernate.ddl-auto=update` the `users` table will be created automatically by Hibernate, but if you prefer to create it manually use:

```sql
USE lab1_db;
CREATE TABLE IF NOT EXISTS users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255),
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL
);
```

-------------------------
Environment / configuration notes

- Primary backend config: `backend/src/main/resources/application.properties`
	- `spring.datasource.url` — JDBC URL. Default: `jdbc:mysql://localhost:3306/lab1_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC`
	- `spring.datasource.username` — default `labuser`
	- `spring.datasource.password` — default `lab1pass`
	- `server.port` — backend port (default `8080`)
	- `spring.jpa.hibernate.ddl-auto` — schema strategy (default `update`)

- Frontend API base: the frontend uses relative paths like `/api/...` and relies on Vite dev proxy during development. In production, configure the frontend to point to the backend base URL (for example `https://api.example.com`).

If you need to change DB credentials, update `application.properties` or provide a custom properties file / environment variables when launching Spring Boot.

-------------------------
API endpoints

The backend exposes the following endpoints (base URL: `http://localhost:8080` by default):

- `POST /api/auth/register` — Register a new user.
	- Request JSON: `{ "name": "Full Name", "email": "you@example.com", "password": "secret" }`
	- Response: sets an HttpOnly `token` cookie and returns `{ name, email }`.

- `POST /api/auth/login` — Login with email and password.
	- Request JSON: `{ "email": "you@example.com", "password": "secret" }`
	- Response: sets an HttpOnly `token` cookie and returns `{ name, email }`.

- `GET /api/auth/validate` — Validate current authentication.
	- Reads `token` from cookie (or uses Spring Security authentication as fallback).
	- Response: `200 OK` with `{ name, email }` when valid; `401` when invalid.

- `POST /api/auth/logout` — Log out the current user.
	- Response: clears the `token` cookie.

- `POST /api/debug/check-password` — Debug helper (temporary, remove in production).
	- Request JSON: `{ "email": "...", "password": "..." }`
	- Response: `{ exists: bool, matches: bool }` to check stored password hash.

Note: The `debug` endpoints are intended for development only — remove or secure them before deploying to production.

-------------------------
Troubleshooting

- If the frontend shows `401` when calling `/api/auth/validate`, confirm the frontend uses `credentials: 'include'` and you are running the frontend via Vite's dev server (proxy enabled).
- If the backend cannot connect to MySQL, verify the database server is running and credentials in `application.properties` are correct.

-------------------------
Next steps / suggestions

- Add integration tests for auth endpoints using Spring Boot test slices.
- Containerize backend with Docker and add a `docker-compose` file to run MySQL + backend + frontend.

-------------------------
If you'd like, I can also:
- create a SQL dump file under `backend/db/` for import
- add environment-specific config examples (e.g., `application-dev.properties`)
- add a `docker-compose.yml` to simplify local environment setup

---
Updated to include full run and DB setup instructions. If you want any of the optional extras, tell me which and I'll add them.
