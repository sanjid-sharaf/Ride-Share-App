Write-Host "=== Building Project ==="

# Collect all Java files under src/
$javaFiles = Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName }

# Compile into bin/
javac -d bin $javaFiles

Write-Host "=== Build Complete ==="
