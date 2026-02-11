@echo off
chcp 65001 >nul
echo ========================================
echo   Stopping All Services
echo ========================================
echo.

echo Stopping Node.js processes (Mock API, Frontend, RuoYi)...
taskkill /f /im node.exe 2>nul
if %errorlevel%==0 (
    echo   - Node.js processes stopped
)

echo Stopping Java processes (Backend)...
taskkill /f /im java.exe 2>nul
if %errorlevel%==0 (
    echo   - Java processes stopped
)

echo Stopping Redis...
taskkill /f /im redis-server.exe 2>nul
if %errorlevel%==0 (
    echo   - Redis stopped
)

echo.
echo ========================================
echo   All Services Stopped
echo ========================================
echo.
echo Press any key to close this window...
pause >nul
