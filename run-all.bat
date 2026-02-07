@echo off
REM run-all.bat - simple launcher that opens two PowerShell windows
REM It delegates detailed environment loading to the PS1 scripts to avoid quoting/bracing issues.

SET ROOT_DIR=%~dp0

echo Starting Backend and Frontend in separate PowerShell windows...

REM Start backend PS1 script
start "Backend" powershell -NoExit -ExecutionPolicy Bypass -File "%ROOT_DIR%scripts\start-backend.ps1"

REM Start frontend PS1 script
start "Frontend" powershell -NoExit -ExecutionPolicy Bypass -File "%ROOT_DIR%scripts\start-frontend.ps1"

echo Launched. Close the two windows to stop the servers.
