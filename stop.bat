@echo off
echo =======================================================================
echo Stopping International Payment Platform on Windows...
echo =======================================================================

for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do taskkill /F /PID %%a 2>nul
echo Application stopped.
