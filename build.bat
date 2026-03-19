@echo off
title Study Group Organizer - Build Script
color 0A

echo ============================================
echo   Study Group Organizer - Build Script
echo   Group 3
echo ============================================
echo.

:: STEP 1: Check Java
echo [1/4] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo ERROR: Java not found. Install from https://adoptium.net
    pause
    exit /b 1
)
echo       Java found!
echo.

:: STEP 2: Check MySQL connector
echo [2/4] Checking MySQL connector...
if not exist mysql-connector-j*.jar (
    color 0C
    echo ERROR: MySQL connector JAR not found!
    echo.
    echo Please download it from:
    echo https://dev.mysql.com/downloads/connector/j/
    echo Choose: Platform Independent ^> Download ZIP
    echo Extract and copy the .jar file into this folder.
    echo.
    pause
    exit /b 1
)
echo       MySQL connector found!
echo.

:: Get connector filename
for %%f in (mysql-connector-j*.jar) do set CONNECTOR=%%f
echo       Using: %CONNECTOR%
echo.

:: STEP 3: Compile
echo [3/4] Compiling source files...
if exist out rmdir /s /q out
mkdir out

javac -encoding UTF-8 -cp .;%CONNECTOR% -d out *.java
if %errorlevel% neq 0 (
    color 0C
    echo ERROR: Compilation failed.
    pause
    exit /b 1
)
echo       Compilation successful!
echo.

:: Copy connector into out folder
copy %CONNECTOR% out\ >nul

:: STEP 4: Create JAR with connector inside
echo [4/4] Creating JAR file...
if exist StudyGroupOrganizer.jar del StudyGroupOrganizer.jar

cd out
jar xf %CONNECTOR%
cd ..
jar cfe StudyGroupOrganizer.jar Main -C out .

if %errorlevel% neq 0 (
    color 0C
    echo ERROR: JAR creation failed.
    pause
    exit /b 1
)

color 0A
echo.
echo ============================================
echo   BUILD SUCCESSFUL!
echo ============================================
echo.
echo   Your app is ready: StudyGroupOrganizer.jar
echo.
echo   Run it by double-clicking run.bat
echo   OR share it with classmates via Google Drive!
echo   They need Java installed to run it.
echo ============================================
echo.
pause