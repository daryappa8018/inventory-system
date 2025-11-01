@echo off
echo ================================================
echo   Inventory Management System - Legacy Mode
echo ================================================
echo.

REM Navigate to project directory
cd /d "%~dp0"

echo [1/2] Compiling project...
call mvn clean compile
if errorlevel 1 (
    echo ERROR: Maven compilation failed!
    pause
    exit /b 1
)

echo.
echo [2/2] Starting application...
echo.
echo ================================================
echo   Starting Inventory System (File Mode)
echo ================================================
echo.

call mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLI"

pause
