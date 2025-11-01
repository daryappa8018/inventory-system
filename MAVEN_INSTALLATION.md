# How to Install Apache Maven on Windows

## Step-by-Step Maven Installation Guide

### Method 1: Manual Installation (Recommended)

#### Step 1: Download Maven

1. Go to the official Apache Maven website: https://maven.apache.org/download.cgi
2. Under "Files", find the **Binary zip archive** link
3. Download the latest version (e.g., `apache-maven-3.9.9-bin.zip`)
4. The file will be downloaded to your Downloads folder

#### Step 2: Extract Maven

1. Navigate to your Downloads folder
2. Right-click on `apache-maven-3.9.9-bin.zip`
3. Select "Extract All..."
4. Extract to: `C:\Program Files\Apache\maven`
   - You may need to create the `Apache` folder first
   - Or extract to any location you prefer (e.g., `C:\Maven`)

The folder structure should look like:
```
C:\Program Files\Apache\maven\
├── bin\
├── boot\
├── conf\
├── lib\
└── LICENSE
```

#### Step 3: Set Environment Variables

**3a. Set MAVEN_HOME:**

1. Right-click on "This PC" or "My Computer" → Select "Properties"
2. Click "Advanced system settings" (on the left)
3. Click "Environment Variables" button at the bottom
4. Under "System variables" section, click "New"
5. Enter:
   - **Variable name**: `MAVEN_HOME`
   - **Variable value**: `C:\Program Files\Apache\maven`
6. Click "OK"

**3b. Add Maven to PATH:**

1. Still in "Environment Variables" window
2. Under "System variables", find and select the "Path" variable
3. Click "Edit"
4. Click "New"
5. Add: `%MAVEN_HOME%\bin`
6. Click "OK" on all windows

#### Step 4: Verify Java Installation

Maven requires Java. Verify Java is installed:

1. Open PowerShell or Command Prompt
2. Run:
   ```powershell
   java -version
   ```
3. You should see Java version information
4. If not, install Java first from: https://www.oracle.com/java/technologies/downloads/

#### Step 5: Verify Maven Installation

1. **Close all open PowerShell/CMD windows** (important!)
2. Open a **NEW** PowerShell or Command Prompt
3. Run:
   ```powershell
   mvn -version
   ```

You should see output like:
```
Apache Maven 3.9.9
Maven home: C:\Program Files\Apache\maven
Java version: 22.0.x, vendor: Oracle Corporation
```

---

### Method 2: Using Chocolatey (Package Manager)

If you have Chocolatey installed:

1. Open PowerShell as Administrator
2. Run:
   ```powershell
   choco install maven
   ```
3. Verify:
   ```powershell
   mvn -version
   ```

---

### Method 3: Using Scoop (Package Manager)

If you have Scoop installed:

1. Open PowerShell
2. Run:
   ```powershell
   scoop install maven
   ```
3. Verify:
   ```powershell
   mvn -version
   ```

---

## Troubleshooting

### Issue: "mvn is not recognized"

**Solutions:**

1. **Restart your terminal** - Environment variables only load in new sessions
2. **Check PATH** - Verify `%MAVEN_HOME%\bin` is in System PATH
3. **Check MAVEN_HOME** - Verify it points to correct folder
4. **Use full path** - Try running:
   ```powershell
   & "C:\Program Files\Apache\maven\bin\mvn.cmd" -version
   ```

### Issue: "JAVA_HOME not found"

Maven needs JAVA_HOME environment variable:

1. Find your Java installation path (e.g., `C:\Program Files\Java\jdk-22`)
2. Set JAVA_HOME:
   - Environment Variables → System variables → New
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-22` (your Java path)
3. Restart terminal and try again

### Issue: Maven installed but still not working

1. **Check the bin folder exists**:
   ```powershell
   Test-Path "C:\Program Files\Apache\maven\bin\mvn.cmd"
   ```
   Should return `True`

2. **Check PATH variable**:
   ```powershell
   $env:Path -split ';' | Select-String maven
   ```
   Should show the Maven bin path

3. **Manually run Maven**:
   ```powershell
   & "C:\Program Files\Apache\maven\bin\mvn.cmd" -version
   ```

---

## Quick Verification Checklist

After installation, verify everything is working:

- [ ] Java is installed: `java -version`
- [ ] JAVA_HOME is set: `echo $env:JAVA_HOME`
- [ ] Maven is installed: `mvn -version`
- [ ] Maven shows correct Java version
- [ ] Can run Maven commands from any directory

---

## For Your Inventory Project

Once Maven is installed, navigate to your project and run:

```powershell
# Navigate to project
cd "c:\Users\Daryappa mane\IdeaProjects\Inventory Maneger"

# Install dependencies
mvn clean install

# Run the database version
mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"
```

---

## Alternative: Use IntelliJ IDEA's Bundled Maven

If you're using IntelliJ IDEA, it comes with Maven built-in:

1. Open your project in IntelliJ IDEA
2. Go to: File → Settings → Build, Execution, Deployment → Build Tools → Maven
3. IntelliJ will use its bundled Maven automatically
4. You can build and run directly from IntelliJ without installing Maven separately

To use IntelliJ's Maven:
- Right-click on `pom.xml` → Maven → Reload Project
- Right-click on `pom.xml` → Maven → Install

---

## Quick Installation Script (PowerShell)

If you want to automate the installation:

```powershell
# Download Maven (adjust version as needed)
$mavenVersion = "3.9.9"
$downloadUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$downloadPath = "$env:TEMP\maven.zip"
$installPath = "C:\Program Files\Apache\maven"

# Download
Invoke-WebRequest -Uri $downloadUrl -OutFile $downloadPath

# Extract
Expand-Archive -Path $downloadPath -DestinationPath "C:\Program Files\Apache" -Force

# Rename folder
Rename-Item -Path "C:\Program Files\Apache\apache-maven-$mavenVersion" -NewName "maven"

# Set environment variables (requires admin)
[Environment]::SetEnvironmentVariable("MAVEN_HOME", $installPath, "Machine")
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
[Environment]::SetEnvironmentVariable("Path", "$currentPath;$installPath\bin", "Machine")

Write-Host "Maven installed! Please restart your terminal and run: mvn -version"
```

---

## Next Steps

After Maven is installed:

1. ✅ Verify installation: `mvn -version`
2. ✅ Navigate to project directory
3. ✅ Install dependencies: `mvn clean install`
4. ✅ Configure MySQL in `database.properties`
5. ✅ Run application: `mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"`

---

**Need help?** Check the main INSTALLATION.md for complete project setup instructions.
