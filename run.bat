Copy

@echo off
title Study Group Organizer
java -jar StudyGroupOrganizer.jar
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Could not launch the app.
    echo Make sure Java is installed.
    echo Download from: https://adoptium.net
    pause
)
 