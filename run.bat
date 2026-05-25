@echo off
chcp 65001 >nul
echo ========================================
echo   Drone Rental Admin - Startup Script
echo ========================================
echo.

echo [1/3] Cleaning project...
call mvn clean
if errorlevel 1 (
    echo Clean failed!
    pause
    exit /b 1
)
echo.

echo [2/3] Compiling project...
call mvn compile
if errorlevel 1 (
    echo Compile failed!
    pause
    exit /b 1
)
echo.

echo [3/3] Starting application...
echo.
call mvn spring-boot:run -Dspring-boot.run.main-class=com.xxq.dronerent.DroneRentApplication

pause
