@echo off

echo Compiling test files...

rem Compile ONLY tests (assumes src classes are already in bin)
for /r tests %%f in (*.java) do (
    javac -d bin "%%f"
)

echo Running tests...
java -cp bin tests.ModelsTest
