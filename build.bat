@echo off
echo === Building Project ===

REM Create bin directory if missing
if not exist bin mkdir bin

REM Compile all Java files in src/
javac -cp "libs\sqlite-jdbc.jar" -d bin src\Main.java src\**\*.java

echo === Build Complete ===
