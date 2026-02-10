

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

Requirements
- Java 17
- Node.js & npm

Run the backend (development)
1. From repository root:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The backend runs on port `8080` by default.

Run the frontend (development)
1. From repository root:

```bash
cd web
npm install
npm run dev
```

2. Vite will print the local URL (typically `http://localhost:5173` or `5174`). The frontend uses a proxy so API requests to `/api` are forwarded to the backend and cookies behave like same-origin in dev.

Testing the demo (browser)
1. Open the frontend URL in a browser.
2. Register a new user using the Register form.
3. On successful register/login the backend response will include a `Set-Cookie: token=...` header. Confirm in DevTools → Network.
4. Subsequent requests from the frontend call `/api/auth/validate` with `credentials: 'include'`. Validate should return 200 and your user info when the cookie is present.

If you see 403/401 on `/api/auth/validate`:
- Ensure the frontend calls use `credentials: 'include'` (the demo already does this).
- Make sure you run the frontend via Vite (proxy enabled) so requests are same-origin. If you bypass the proxy, cross-site cookies may be blocked.

Production notes (do not use these dev settings in production)
- Use HTTPS and set `cookie.setSecure(true)` so cookies are marked `Secure` and `SameSite=None`.
- Replace `jwt.secret` with a strong secret and consider refresh tokens / revocation.

If you want me to: add a small integration test for the auth endpoints, containerize the backend, or re-enable item endpoints, tell me which option and I will proceed.
## Steps to run mobile app
