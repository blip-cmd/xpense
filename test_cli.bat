@echo off
echo ╔══════════════════════════════════════════════════════════╗
echo ║                 🔧 CLI TESTING LAUNCHER                  ║
echo ╚══════════════════════════════════════════════════════════╝
echo.
echo Starting CLI Menu Testing Suite...
echo.

cd /d "c:\Users\HP\OneDrive - University of Ghana\RB\mySakai\lvl300 2nd sem\DCIT308 DSA II with Prof Sarpong\grp\xpense"

call src\test\cli\scripts\cli_tester.bat

echo.
echo CLI Testing Suite completed.
pause
