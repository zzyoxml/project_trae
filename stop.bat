@echo off
echo Closing all services...
taskkill /f /im node.exe
taskkill /f /im java.exe
taskkill /f /im redis-server.exe
echo Done!
pause
