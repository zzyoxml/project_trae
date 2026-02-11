@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ========================================
echo   Starting All Services
echo ========================================
echo.

echo [1/5] Starting Redis (Port 6379)...
start "Redis" cmd /k "E:\redis-64.3.0.503\redis-server.exe E:\redis-64.3.0.503\redis.windows.conf"
timeout /t 3 /nobreak >nul

echo [2/5] Starting Mock API (Port 3002)...
start "MockAPI" cmd /k "cd /d "%~dp0frontend" && npx json-server db.json --port 3002"

echo [3/5] Starting Frontend - LinguaLearn (Port 3000)...
start "LinguaLearn" cmd /k "cd /d "%~dp0frontend" && npm run dev"

echo [4/5] Starting Backend (Port 6666)...
start "Backend" cmd /k "cd /d "%~dp0" && java -jar ruoyi-admin\target\ruoyi-admin.jar"

echo [5/5] Starting RuoYi Admin Frontend (Port 66)...
start "RuoYiAdmin" cmd /k "cd /d "%~dp0ruoyi-ui" && set NODE_OPTIONS=--openssl-legacy-provider && npm run dev"

echo.
echo ========================================
echo   All Services Started
echo ========================================
echo   Redis:         http://localhost:6379
echo   Mock API:      http://localhost:3002
echo   LinguaLearn:   http://localhost:3000
echo   Backend:       http://localhost:6666
echo   RuoYi Admin:   http://localhost:66
echo ========================================
echo.
echo Press any key to close this window...
pause >nul
