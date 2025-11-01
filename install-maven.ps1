# Quick Maven Installer for Windows
# Run this script in PowerShell as Administrator

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Maven Quick Installer for Windows" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is already installed
$existingMaven = Get-Command mvn -ErrorAction SilentlyContinue
if ($existingMaven) {
    Write-Host "Maven is already installed!" -ForegroundColor Green
    mvn -version
    exit 0
}

# Check Java installation
Write-Host "Checking Java installation..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Java is not installed!" -ForegroundColor Red
    Write-Host "Please install Java first from: https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Red
    exit 1
}
Write-Host "Java is installed: $($javaVersion[0])" -ForegroundColor Green

# Maven version to install
$mavenVersion = "3.9.9"
$mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$tempZip = "$env:TEMP\apache-maven-$mavenVersion-bin.zip"
$extractPath = "C:\Program Files\Apache"
$mavenHome = "$extractPath\maven"

Write-Host ""
Write-Host "Installing Maven $mavenVersion..." -ForegroundColor Yellow

# Create Apache directory if it doesn't exist
if (!(Test-Path $extractPath)) {
    Write-Host "Creating directory: $extractPath" -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $extractPath -Force | Out-Null
}

# Download Maven
Write-Host "Downloading Maven..." -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri $mavenUrl -OutFile $tempZip -UseBasicParsing
    Write-Host "Download complete!" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to download Maven!" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

# Extract Maven
Write-Host "Extracting Maven..." -ForegroundColor Yellow
try {
    Expand-Archive -Path $tempZip -DestinationPath $extractPath -Force
    
    # Rename to 'maven' if needed
    $extractedFolder = "$extractPath\apache-maven-$mavenVersion"
    if (Test-Path $extractedFolder) {
        if (Test-Path $mavenHome) {
            Remove-Item $mavenHome -Recurse -Force
        }
        Rename-Item -Path $extractedFolder -NewName "maven"
    }
    
    Write-Host "Extraction complete!" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to extract Maven!" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

# Set environment variables
Write-Host "Setting environment variables..." -ForegroundColor Yellow
try {
    # Set MAVEN_HOME
    [Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "Machine")
    
    # Add to PATH
    $currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
    if ($currentPath -notlike "*$mavenHome\bin*") {
        $newPath = "$currentPath;$mavenHome\bin"
        [Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
    }
    
    # Update current session
    $env:MAVEN_HOME = $mavenHome
    $env:Path = "$env:Path;$mavenHome\bin"
    
    Write-Host "Environment variables set!" -ForegroundColor Green
} catch {
    Write-Host "WARNING: Failed to set environment variables!" -ForegroundColor Yellow
    Write-Host "You may need to set them manually." -ForegroundColor Yellow
}

# Clean up
Remove-Item $tempZip -Force -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host "Maven Installation Complete!" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Green
Write-Host ""
Write-Host "Maven installed at: $mavenHome" -ForegroundColor Cyan
Write-Host ""
Write-Host "IMPORTANT: Close this PowerShell window and open a NEW one" -ForegroundColor Yellow
Write-Host "Then verify installation by running: mvn -version" -ForegroundColor Yellow
Write-Host ""
Write-Host "If 'mvn' is still not recognized, try:" -ForegroundColor Yellow
Write-Host "1. Restart your computer" -ForegroundColor Yellow
Write-Host "2. Or run in a NEW PowerShell window: `$env:Path = [Environment]::GetEnvironmentVariable('Path','Machine')" -ForegroundColor Yellow
