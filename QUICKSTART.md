# Quick Start Guide - MySQL Integration

## ⚡ Fast Setup (5 Minutes)

### 1. Install MySQL
- **Windows**: Download from https://dev.mysql.com/downloads/installer/
- Choose "MySQL Installer for Windows"
- Install MySQL Server (remember your root password!)

### 2. Verify MySQL is Running
```powershell
# Check MySQL service status
Get-Service MySQL80

# If not running, start it
Start-Service MySQL80
```

### 3. Configure Database Password
Edit: `src/main/resources/database.properties`
```properties
db.password=YOUR_MYSQL_ROOT_PASSWORD
```

### 4. Install Dependencies
```powershell
cd "c:\Users\Daryappa mane\IdeaProjects\Inventory Maneger"
mvn clean install
```

### 5. Run the Application
```powershell
mvn compile exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"
```

## 🎯 First Time Setup

When you run the application for the first time:

1. It will automatically create the database `inventory_db`
2. It will create the tables `inventory` and `transaction_log`
3. You'll be asked: "Do you want to load legacy CSV data into database?"
   - Type **yes** if you have existing data in `inventory.csv`
   - Type **no** to start fresh

## 📝 Testing the Setup

After starting the application, try these commands:

```
>> add
Enter SKU: TEST001
Enter item name: Test Item
Enter quantity: 100
Enter reorder threshold: 20
Enter shelf life in days: 365

>> view all
(Should show your test item)

>> exit
```

## 🔍 Verify in MySQL

Open MySQL Command Line Client:
```powershell
mysql -u root -p
```

Then run:
```sql
USE inventory_db;
SELECT * FROM inventory;
SELECT * FROM transaction_log;
```

## ❌ Common Issues

### "Access Denied"
- Check password in `database.properties`
- Make sure you're using the correct MySQL root password

### "Cannot connect to database"
- MySQL service not running: `Start-Service MySQL80`
- Firewall blocking port 3306

### "Could not find database.properties"
- Run `mvn clean compile` first
- Check file exists in `src/main/resources/`

## 🎊 Success!

If you see this, you're all set:
```
🔧 Initializing database...
✅ Database tables initialized successfully.
📦 Welcome to Inventory Management System (Database Mode)
```

## 🔄 Switching Between Modes

**Database Mode** (Persistent):
```powershell
mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"
```

**File Mode** (Legacy):
```powershell
mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLI"
```

## 📚 Next Steps

- Read `DATABASE_SETUP.md` for detailed documentation
- Review `database_setup.sql` for manual database setup
- Check the application features in the main menu

---

Need help? Check the troubleshooting section in `DATABASE_SETUP.md`
