# Maven Installer for Current User (No Admin Required)
# Run this script in PowerShell

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Maven Installer (User Directory)" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# Maven version
$mavenVersion = "3.9.9"
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$tempZip = "$env:TEMP\maven.zip"
$installPath = "$env:USERPROFILE\maven"

Write-Host "Installing Maven $mavenVersion to: $installPath" -ForegroundColor Yellow
Write-Host ""

# Download Maven
Write-Host "[1/4] Downloading Maven..." -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri $mavenUrl -OutFile $tempZip -UseBasicParsing
    Write-Host "      Download complete!" -ForegroundColor Green
} catch {
    Write-Host "      ERROR: Download failed!" -ForegroundColor Red
    Write-Host "      $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Remove old installation
if (Test-Path $installPath) {
    Write-Host "[2/4] Removing old installation..." -ForegroundColor Yellow
    Remove-Item $installPath -Recurse -Force
}

# Extract Maven
Write-Host "[2/4] Extracting Maven..." -ForegroundColor Yellow
try {
    Expand-Archive -Path $tempZip -DestinationPath $env:USERPROFILE -Force
    $extractedFolder = "$env:USERPROFILE\apache-maven-$mavenVersion"
    Rename-Item -Path $extractedFolder -NewName "maven"
    Write-Host "      Extraction complete!" -ForegroundColor Green
} catch {
    Write-Host "      ERROR: Extraction failed!" -ForegroundColor Red
    exit 1
}

# Set environment variables for current user
Write-Host "[3/4] Setting environment variables..." -ForegroundColor Yellow
try {
    [Environment]::SetEnvironmentVariable("MAVEN_HOME", $installPath, "User")
    
    $currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
    if ($currentPath -notlike "*maven\bin*") {
        $newPath = "$installPath\bin;$currentPath"
        [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    }
    
    Write-Host "      Environment variables set!" -ForegroundColor Green
} catch {
    Write-Host "      WARNING: Failed to set environment variables" -ForegroundColor Yellow
}

# Clean up
Write-Host "[4/4] Cleaning up..." -ForegroundColor Yellow
Remove-Item $tempZip -Force -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host "  Installation Complete!" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Green
Write-Host ""
Write-Host "Maven installed at: $installPath" -ForegroundColor Cyan
Write-Host ""
Write-Host "NEXT STEPS:" -ForegroundColor Yellow
Write-Host "1. Close this PowerShell window" -ForegroundColor White
Write-Host "2. Open a NEW PowerShell window" -ForegroundColor White
Write-Host "3. Run: mvn -version" -ForegroundColor White
Write-Host ""
Write-Host "Or use Maven immediately in this window:" -ForegroundColor Yellow
Write-Host "Run: " -NoNewline -ForegroundColor White
Write-Host "`$env:Path = '$installPath\bin;' + `$env:Path" -ForegroundColor Cyan
