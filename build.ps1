Write-Host "=== Building Project ==="

# Collect all Java files under src/
$javaFiles = Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName }

# Compile into bin/
javac -cp "lib/sqlite-jdbc-3.51.0.0.jar" -d bin $javaFiles

Write-Host "=== Build Complete ==="
