# MiniApp Mobile (Android)

This is the Android client for the MiniApp login/register demo. It uses Kotlin, Jetpack Compose, and Retrofit to connect to the backend API.

## Prerequisites

- Android Studio Iguana (2023.2.1) or later
- Android SDK (API 24+)
- Java 17 or later

## How to open in Android Studio

1. **Open Android Studio Iguana**

2. **Open the mobile project:**
   - Click **File** → **Open**
   - Navigate to `IT342_G4_Badinas_Lab1\mobile\` folder
   - Click **OK**

3. **Wait for Gradle sync to complete** (first sync will download dependencies)

4. **Configure backend URL:**
   - Open [RetrofitClient.kt](app/src/main/java/com/example/miniapp/data/api/RetrofitClient.kt)
   - Update `BASE_URL`:
     - For **Android Emulator**: use `http://10.0.2.2:8080/` (points to host machine's localhost)
     - For **Physical Device**: use `http://<YOUR_MACHINE_IP>:8080/` (find your IP with `ipconfig` on Windows)

5. **Run the app:**
   - Click the green **Run** button (or press Shift+F10)
   - Select an emulator or connected device
   - The app will build and launch

## App structure

- `data/model/` — API request/response models
- `data/api/` — Retrofit service and client
- `data/repository/` — Auth repository and token storage (DataStore)
- `ui/login/`, `ui/register/`, `ui/dashboard/` — UI screens and ViewModels
- `navigation/` — Navigation graph
- `ui/theme/` — Material3 theme

## Important notes

**Backend cookie issue**: The web app uses HttpOnly cookies, but Android apps don't support HttpOnly cookies the same way browsers do. This mobile app currently expects the backend to return the token in the response body. You have two options:

1. **Modify the backend** to return the token in the response body for mobile clients (recommended for this demo)
2. **Use a different auth flow** for mobile (e.g., OAuth2, or custom header-based tokens)

For now, the app works but won't persist sessions across app restarts. If you want full token persistence, you'll need to adjust the backend to return tokens in the response body for mobile clients.

## Testing

1. Ensure backend is running on `http://localhost:8080`
2. Launch the app
3. Register a new user
4. Login with credentials
5. You'll be taken to the Dashboard
6. Logout returns to the Login screen

## Build the APK

To build a release APK:

```bash
cd mobile
./gradlew assembleRelease
```

The APK will be in `app/build/outputs/apk/release/`.
