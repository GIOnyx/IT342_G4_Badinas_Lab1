# Project Task List

This repository is a small demo focused on user authentication (register, login, validate, logout). The app uses a backend (Spring Boot), a frontend (React + Vite), and a mobile app (Android + Jetpack Compose) for local development and demonstration.

## Backend

- Done: Auth & JWT — `/api/auth/register`, `/api/auth/login` implemented
- Done: Datasource & properties — MySQL/XAMPP config in `application.properties` (dev/test)
- Done: CORS & `SecurityConfig` — JWT filter wired, CORS allowed for local frontend
- Done: Validation & Error Handling — DTO validation and global exception handler

## Web (React)

- Done: Routing & Pages — Login, Register, Dashboard, Items, ItemDetails (UI present)
- Done: Auth integration — frontend calls auth endpoints, uses cookie-based token in dev
- Done: UI Theme & Navbar — light-green theme (Navbar removed if unused)
- Done: Attach Authorization header & Logout — ensure logout clears cookie and redirects

## Mobile

- Done: Scaffold Android app — Kotlin + Jetpack Compose project created
- Done: Auth screens — Login, Register, and Dashboard UI with ViewModels
- Done: API integration — Retrofit service, token storage (DataStore), auth repository
- Done: Navigation — Navigation Compose with protected routes
- To Do: Items list & details — consume backend item APIs (out of demo scope)

---
Notes:
- This repo is maintained as a login/register demo with backend, web, and mobile clients.
- Item management and production deployment are intentionally out-of-scope unless requested.
- For backend/web testing: run the backend and Vite dev server, verify `Set-Cookie` on login.
- For mobile: open `mobile/` in Android Studio Iguana, update backend IP in `RetrofitClient.kt`, and run on emulator or device.

See README files in each folder (backend, web, mobile) for detailed setup instructions.