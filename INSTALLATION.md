# Installation Guide for MySQL Integration

## Prerequisites Installation

### 1. Install MySQL Server

#### Windows:
1. Download MySQL Installer from: https://dev.mysql.com/downloads/installer/
2. Run the installer (choose mysql-installer-community-8.x.x.msi)
3. Installation steps:
   - Select "Server only" or "Developer Default"
   - Click "Next" and "Execute" to install
   - Configure MySQL Server:
     - Config Type: Development Computer
     - Port: 3306 (default)
     - Set Root Password: **Remember this password!**
     - Windows Service: Leave as default (MySQL80)
     - Click "Execute" to apply configuration
4. Complete the installation

#### Verify MySQL Installation:
```powershell
# Check if MySQL service is running
Get-Service MySQL80

# If not running, start it
Start-Service MySQL80

# Test MySQL connection
mysql -u root -p
# Enter your root password when prompted
# If successful, you'll see: mysql>
# Type: exit
```

### 2. Install Maven (if not already installed)

#### Check if Maven is installed:
```powershell
mvn -version
```

#### If not installed:
1. Download from: https://maven.apache.org/download.cgi
2. Download the Binary zip archive (apache-maven-x.x.x-bin.zip)
3. Extract to: `C:\Program Files\Apache\maven`
4. Add to System PATH:
   - Open System Properties → Environment Variables
   - Under System Variables, find "Path"
   - Click "Edit" → "New"
   - Add: `C:\Program Files\Apache\maven\bin`
   - Click OK
5. Restart your terminal/PowerShell
6. Verify: `mvn -version`

### 3. Verify Java Installation

```powershell
java -version
# Should show Java 22 or compatible version
```

## Project Setup

### Step 1: Configure Database Connection

1. Open `src\main\resources\database.properties`
2. Update with your MySQL root password:
```properties
db.url=jdbc:mysql://localhost:3306/inventory_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_ROOT_PASSWORD_HERE
```

### Step 2: Build Project (Using IntelliJ IDEA)

If you're using IntelliJ IDEA:
1. Open the project in IntelliJ IDEA
2. Right-click on `pom.xml`
3. Select "Maven" → "Reload Project"
4. Wait for dependencies to download
5. The project should now compile without errors

### Step 3: Run the Application

#### Option A: Using Batch Files (Easiest)
Simply double-click on:
- `run-database.bat` - For database mode
- `run-legacy.bat` - For legacy file mode

#### Option B: Using IntelliJ IDEA
1. Open `src\main\java\com\Daryappa\Inventory\cli\InventoryCLIDB.java`
2. Right-click in the editor
3. Select "Run 'InventoryCLIDB.main()'"

#### Option C: Using Maven Command (Terminal)
```powershell
# Navigate to project directory
cd "c:\Users\Daryappa mane\IdeaProjects\Inventory Maneger"

# Build the project
mvn clean install

# Run database version
mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"

# OR run legacy version
mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLI"
```

## Verification

### Test Database Connection

1. Open MySQL Command Line Client (from Start Menu)
2. Enter your root password
3. Run these commands:
```sql
SHOW DATABASES;
-- You should see inventory_db listed

USE inventory_db;
SHOW TABLES;
-- Should show: inventory, transaction_log

-- View table structures
DESCRIBE inventory;
DESCRIBE transaction_log;
```

### Test Application

1. Run the application
2. Try adding an item:
```
>> add
Enter SKU: TEST001
Enter item name: Test Product
Enter quantity: 100
Enter reorder threshold: 20
Enter shelf life in days: 365
```

3. Verify in MySQL:
```sql
USE inventory_db;
SELECT * FROM inventory;
SELECT * FROM transaction_log;
```

## Troubleshooting

### Issue: Maven not recognized
**Solution:** 
- Verify Maven is installed: `mvn -version`
- If not, download and install from maven.apache.org
- Add Maven bin folder to System PATH
- Restart terminal/IDE

### Issue: MySQL connection failed
**Solution:**
1. Check MySQL service: `Get-Service MySQL80`
2. Start if needed: `Start-Service MySQL80`
3. Test connection: `mysql -u root -p`
4. Verify password in `database.properties`

### Issue: Port 3306 already in use
**Solution:**
1. Check what's using port 3306:
```powershell
netstat -ano | findstr :3306
```
2. Either stop that process or change MySQL port in `database.properties`

### Issue: "Access denied for user 'root'"
**Solution:**
1. Verify password is correct
2. Reset root password if needed:
```powershell
# Stop MySQL service
Stop-Service MySQL80

# Follow MySQL password reset procedure
# Or reinstall MySQL with correct password
```

### Issue: Dependencies not downloading
**Solution:**
1. Check internet connection
2. Try clearing Maven cache:
```powershell
# Delete .m2 repository cache
Remove-Item -Path "$env:USERPROFILE\.m2\repository" -Recurse -Force
# Then run: mvn clean install
```

### Issue: IntelliJ IDEA shows errors
**Solution:**
1. File → Invalidate Caches → Invalidate and Restart
2. Right-click pom.xml → Maven → Reload Project
3. Build → Rebuild Project

## Security Recommendations

### 1. Create a Dedicated MySQL User

Instead of using root, create a specific user:

```sql
-- Connect to MySQL as root
mysql -u root -p

-- Run these commands:
CREATE USER 'inventory_user'@'localhost' IDENTIFIED BY 'secure_password_here';
GRANT ALL PRIVILEGES ON inventory_db.* TO 'inventory_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

Then update `database.properties`:
```properties
db.username=inventory_user
db.password=secure_password_here
```

### 2. Protect database.properties

Add to `.gitignore`:
```
src/main/resources/database.properties
```

Create a template file:
```properties
# database.properties.template
db.url=jdbc:mysql://localhost:3306/inventory_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
db.username=root
db.password=CHANGE_THIS_PASSWORD
```

## Next Steps

1. ✅ MySQL installed and running
2. ✅ Maven installed and configured
3. ✅ Project dependencies installed
4. ✅ Database configured
5. ✅ Application running successfully

You're all set! Check out:
- `QUICKSTART.md` for quick commands
- `DATABASE_SETUP.md` for detailed features
- `database_setup.sql` for manual SQL setup

---

**Congratulations! Your inventory system is now powered by MySQL! 🎉**
