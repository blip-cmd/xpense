@echo off
setlocal enabledelayedexpansion
color 0B

echo ╔══════════════════════════════════════════════════════════╗
echo ║                 CLI MENU TESTING SUITE                  ║
echo ╚══════════════════════════════════════════════════════════╝
echo.

cd /d "c:\Users\HP\OneDrive - University of Ghana\RB\mySakai\lvl300 2nd sem\DCIT308 DSA II with Prof Sarpong\grp\xpense"

:main_menu
echo ┌─ TESTING OPTIONS ─────────────────────────────────────────┐
echo │ 1. Compile and Test Your CLI Application                  │
echo │ 2. Run Interactive Menu Tester                           │
echo │ 3. Run Simple CLI Menu Guide                             │
echo │ 4. Test with Basic Navigation Input                      │
echo │ 5. Test with Invalid Inputs                              │
echo │ 6. Test with Complex Navigation                          │
echo │ 7. Show All Test Files                                   │
echo │ 8. Clean up compiled files                               │
echo │ 9. Exit                                                  │
echo └───────────────────────────────────────────────────────────┘
echo.
set /p choice="Select option (1-9): "

if "%choice%"=="1" goto test_cli
if "%choice%"=="2" goto interactive_tester
if "%choice%"=="3" goto simple_guide
if "%choice%"=="4" goto basic_test
if "%choice%"=="5" goto invalid_test
if "%choice%"=="6" goto complex_test
if "%choice%"=="7" goto show_files
if "%choice%"=="8" goto cleanup
if "%choice%"=="9" goto end
goto main_menu

:test_cli
echo.
echo ┌─ COMPILING YOUR CLI APPLICATION ──────────────────────────┐
echo │ Compiling Main.java...                                    │
javac -cp src src/app/Main.java
if %errorlevel% equ 0 (
    echo │ ✓ Compilation successful!                                  │
    echo └────────────────────────────────────────────────────────────┘
    echo.
    echo ┌─ RUNNING YOUR CLI APPLICATION ─────────────────────────────┐
    echo │ Starting CLI... (Use Ctrl+C to exit)                      │
    echo └────────────────────────────────────────────────────────────┘
    java -cp src app.Main
) else (
    echo │ ✗ Compilation failed! Check for errors.                   │
    echo └────────────────────────────────────────────────────────────┘
)
pause
goto main_menu

:interactive_tester
echo.
echo ┌─ COMPILING INTERACTIVE TESTER ────────────────────────────┐
javac -cp src src/test/cli/InteractiveCLIMenuTester.java
if %errorlevel% equ 0 (
    echo │ ✓ Compilation successful!                                  │
    echo └────────────────────────────────────────────────────────────┘
    echo.
    java -cp src test.cli.InteractiveCLIMenuTester
) else (
    echo │ ✗ Compilation failed!                                     │
    echo └────────────────────────────────────────────────────────────┘
)
pause
goto main_menu

:simple_guide
echo.
echo ┌─ COMPILING SIMPLE GUIDE ───────────────────────────────────┐
javac -cp src src/test/cli/SimpleCLIMenuTester.java
if %errorlevel% equ 0 (
    echo │ ✓ Compilation successful!                                  │
    echo └────────────────────────────────────────────────────────────┘
    echo.
    java -cp src test.cli.SimpleCLIMenuTester
) else (
    echo │ ✗ Compilation failed!                                     │
    echo └────────────────────────────────────────────────────────────┘
)
pause
goto main_menu

:basic_test
echo.
echo ┌─ TESTING WITH BASIC NAVIGATION INPUT ─────────────────────┐
echo │ This will test: Expenditure→View→Back→Help→Alerts→Back→Exit│
echo └────────────────────────────────────────────────────────────┘
javac -cp src src/app/Main.java
if %errorlevel% equ 0 (
    java -cp src app.Main < src/test/cli/inputs/test_basic_navigation.txt
) else (
    echo Compilation failed!
)
pause
goto main_menu

:invalid_test
echo.
echo ┌─ TESTING WITH INVALID INPUTS ──────────────────────────────┐
echo │ This will test error handling with invalid menu choices    │
echo └────────────────────────────────────────────────────────────┘
javac -cp src src/app/Main.java
if %errorlevel% equ 0 (
    java -cp src app.Main < src/test/cli/inputs/test_invalid_inputs.txt
) else (
    echo Compilation failed!
)
pause
goto main_menu

:complex_test
echo.
echo ┌─ TESTING WITH COMPLEX NAVIGATION ──────────────────────────┐
echo │ This will test multiple menu levels and paths              │
echo └────────────────────────────────────────────────────────────┘
javac -cp src src/app/Main.java
if %errorlevel% equ 0 (
    java -cp src app.Main < src/test/cli/inputs/test_complex_navigation.txt
) else (
    echo Compilation failed!
)
pause
goto main_menu

:show_files
echo.
echo ┌─ AVAILABLE TEST FILES ─────────────────────────────────────┐
echo │ Basic Navigation Test:     src/test/cli/inputs/test_basic_navigation.txt       │
echo │ Invalid Input Test:        src/test/cli/inputs/test_invalid_inputs.txt         │
echo │ Complex Navigation Test:   src/test/cli/inputs/test_complex_navigation.txt     │
echo │ Interactive Tester:        src/test/cli/InteractiveCLIMenuTester.java │
echo │ Simple Guide:              src/test/cli/SimpleCLIMenuTester.java     │
echo └────────────────────────────────────────────────────────────┘
echo.
echo You can also create your own test input files and run:
echo java -cp src app.Main ^< your_test_file.txt
pause
goto main_menu

:cleanup
echo.
echo ┌─ CLEANING UP COMPILED FILES ───────────────────────────────┐
del /q /s src\*.class 2>nul
echo │ ✓ All .class files removed                                 │
echo └────────────────────────────────────────────────────────────┘
pause
goto main_menu

:end
echo.
echo ┌─ TESTING SUITE COMPLETED ──────────────────────────────────┐
echo │ Thank you for using the CLI Menu Testing Suite!           │
echo │ Remember to test all menu paths and edge cases.           │
echo └────────────────────────────────────────────────────────────┘
pause
