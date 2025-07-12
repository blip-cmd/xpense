@echo off
echo ========================================
echo CLI Menu Testing Tool
echo ========================================

cd /d "c:\Users\HP\OneDrive - University of Ghana\RB\mySakai\lvl300 2nd sem\DCIT308 DSA II with Prof Sarpong\grp\xpense"

echo.
echo Compiling CLI Menu Tester...
javac -cp src src/test/cli/InteractiveCLIMenuTester.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo.
    echo Running CLI Menu Tester...
    echo ----------------------------------------
    java -cp src test.cli.InteractiveCLIMenuTester
) else (
    echo Compilation failed. Please check for errors.
    pause
)

echo.
echo Testing completed.
pause
