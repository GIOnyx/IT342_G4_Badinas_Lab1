# Project Task List

## Backend
- **Done:** Auth & JWT — `/api/auth/register`, `/api/auth/login` implemented
- **Done:** Datasource & properties — MySQL/XAMPP config in `application.properties`
- **Done:** CORS & SecurityConfig — JWT filter wired, CORS allowed for frontend
- **Done:** Validation & Error Handling — DTO validation and global handler
- **To Do:** Item CRUD endpoints — GET/POST/PUT/DELETE for `/api/items`
- **To Do:** Create DB & MySQL user — run SQL in XAMPP
- **To Do:** Unit/Integration tests — add Spring Boot tests
- **To Do:** Dockerfile & Compose — containerize backend + MySQL

## Web (React)
- **Done:** Routing & Pages — Login, Register, Dashboard, Items, ItemDetails
- **Done:** Auth integration — frontend registers/logins and stores JWT
- **Done:** UI Theme & Navbar — light-green theme, full-width sticky navbar
- **To Do:** Attach Authorization header & Logout — automatically add JWT to requests
- **In Progress:** Connect Items pages to backend — call real item endpoints

## Mobile
- **To Do:** Scaffold client — basic screens (Login, Items, ItemDetails)
- **To Do:** Auth flow — register/login and persist token
- **To Do:** Items list & details — consume backend APIs

---
Notes:
- To finish: implement backend Item CRUD, run MySQL setup, and wire frontend item calls.
- I can implement Item endpoints next and then update the web pages to call them. Reply `go` to proceed.