@echo off
echo ================================================
echo   Inventory Management System - Database Setup
echo ================================================
echo.

REM Navigate to project directory
cd /d "%~dp0"

echo [1/3] Cleaning and compiling project...
call mvn clean compile
if errorlevel 1 (
    echo ERROR: Maven compilation failed!
    echo Please ensure Maven is installed and added to PATH
    pause
    exit /b 1
)

echo.
echo [2/3] Installing dependencies...
call mvn install
if errorlevel 1 (
    echo ERROR: Dependency installation failed!
    pause
    exit /b 1
)

echo.
echo [3/3] Starting application...
echo.
echo ================================================
echo   Starting Inventory System (Database Mode)
echo ================================================
echo.

call mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"

pause
