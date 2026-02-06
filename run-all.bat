@echo off
REM Launcher to start backend and frontend in separate PowerShell windows
SETLOCAL
echo Starting backend and frontend...
start "Backend" powershell -NoExit -Command "Set-Location -Path '%~dp0backend'; .\mvnw.cmd spring-boot:run"
start "Frontend" powershell -NoExit -Command "Set-Location -Path '%~dp0web'; npm run dev"
echo Launched. Two new windows should open for backend and frontend.
ENDLOCAL
