@echo off
cd /d "%~dp0"

echo [1/4] Starting Redis...
start "Redis" cmd /k "cd /d E:\redis-64.3.0.503 && redis-server.exe redis.windows.conf"

echo [2/4] Waiting for Redis...
timeout /t 3 /nobreak >nul

echo [3/4] Starting Backend...
start "Backend" cmd /k "java -jar ruoyi-admin\target\ruoyi-admin.jar"

echo [4/4] Starting Frontend...
cd ruoyi-ui
set NODE_OPTIONS=--openssl-legacy-provider
start "Frontend" cmd /k "npm run dev"
cd ..

echo.
echo ===============================
echo   Frontend: http://localhost:3000
echo   Backend:  http://localhost:3001
echo ===============================
pause
