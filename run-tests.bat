@echo off
echo === Compiling Tests ===

setlocal enabledelayedexpansion
set "TEST_FILES="

for /r tests %%f in (*.java) do (
    set "TEST_FILES=!TEST_FILES! %%f"
)

javac -d bin !TEST_FILES!

echo === Running Tests ===
java -cp bin tests.ModelsTest
