# Inventory Management System - MySQL Integration

This project has been integrated with MySQL database to store all inventory data and transaction logs persistently.

## 🗄️ Database Structure

### Tables

1. **inventory** - Stores all inventory items
   - `sku` (VARCHAR(50), PRIMARY KEY)
   - `name` (VARCHAR(255))
   - `quantity` (INT)
   - `reorder_threshold` (INT)
   - `shelf_life_days` (INT)
   - `created_at` (TIMESTAMP)
   - `updated_at` (TIMESTAMP)

2. **transaction_log** - Stores all transactions
   - `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
   - `date` (VARCHAR(20))
   - `time` (VARCHAR(20))
   - `action` (VARCHAR(50))
   - `sku` (VARCHAR(50))
   - `quantity` (INT)
   - `item_name` (VARCHAR(255))
   - `created_at` (TIMESTAMP)

## 📋 Prerequisites

1. **MySQL Server** (Version 8.0 or higher)
   - Download from: https://dev.mysql.com/downloads/mysql/
   
2. **Java 22** (Already configured in your project)

3. **Maven** (For dependency management)

## 🚀 Setup Instructions

### Step 1: Install MySQL

1. Download and install MySQL Server
2. During installation, set a root password (remember this!)
3. Start MySQL service:
   ```powershell
   # On Windows, MySQL should start automatically
   # Or use Services app to start MySQL80 service
   ```

### Step 2: Configure Database Connection

1. Open `src/main/resources/database.properties`
2. Update the following properties:
   ```properties
   db.username=root
   db.password=YOUR_MYSQL_PASSWORD_HERE
   ```
3. The database `inventory_db` will be created automatically on first run

### Step 3: Install Maven Dependencies

Run the following command in the project directory:
```powershell
mvn clean install
```

This will download:
- MySQL Connector/J (JDBC driver)
- HikariCP (Connection pooling library)

### Step 4: Run the Application

You have two options:

#### Option A: Using Database (Recommended)
```powershell
mvn compile exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"
```

#### Option B: Using Legacy File System
```powershell
mvn compile exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLI"
```

## 📁 Project Structure

```
src/main/java/com/Daryappa/Inventory/
├── cli/
│   ├── InventoryCLI.java          # Legacy file-based CLI
│   └── InventoryCLIDB.java        # New database-enabled CLI
├── db/
│   ├── DatabaseConfig.java         # Database connection configuration
│   ├── DatabaseInitializer.java   # Creates database tables
│   ├── InventoryDAO.java          # Inventory data access object
│   └── TransactionLogDAO.java     # Transaction log data access object
├── service/
│   ├── InventoryManager.java      # Legacy in-memory manager
│   └── InventoryManagerDB.java    # New database-backed manager
└── utils/
    ├── TransactionLoggerDB.java   # Database transaction logger
    └── LogReaderDB.java           # Database log reader
```

## 🔧 Features

### Database Features
- ✅ Persistent storage of inventory items
- ✅ Transaction logging to database
- ✅ Connection pooling for better performance
- ✅ Automatic table creation
- ✅ Data migration from CSV files
- ✅ Backward compatibility with file-based system

### Application Features
- Add inventory items
- Sell items (with stock validation)
- Receive stock (restock items)
- List low stock items
- View all inventory
- Suggest items for restocking (priority-based)
- Suggest expiring items
- Export logs by action type
- Export logs by date
- CSV export functionality

## 🔍 Verifying Database

To verify data is being stored in MySQL:

1. Open MySQL Command Line or MySQL Workbench
2. Connect to MySQL server
3. Run these commands:

```sql
-- Use the database
USE inventory_db;

-- View all inventory items
SELECT * FROM inventory;

-- View all transactions
SELECT * FROM transaction_log ORDER BY id DESC LIMIT 10;

-- View low stock items
SELECT * FROM inventory WHERE quantity < reorder_threshold;

-- View transaction count by action
SELECT action, COUNT(*) as count 
FROM transaction_log 
GROUP BY action;
```

## 🔄 Data Migration

If you have existing CSV data, the application will ask on startup:
```
Do you want to load legacy CSV data into database? (yes/no):
```

Type `yes` to migrate your existing `inventory.csv` data into the MySQL database.

## 📊 Connection Pool Configuration

The application uses HikariCP for connection pooling. You can adjust settings in `database.properties`:

```properties
db.pool.maximumPoolSize=10      # Maximum connections
db.pool.minimumIdle=5           # Minimum idle connections
db.pool.connectionTimeout=30000 # Connection timeout in ms
```

## 🛠️ Troubleshooting

### Issue: "Access denied for user"
**Solution:** Check your MySQL username and password in `database.properties`

### Issue: "Communications link failure"
**Solution:** Make sure MySQL service is running:
```powershell
# Check if MySQL is running
Get-Service MySQL80
# Start MySQL if stopped
Start-Service MySQL80
```

### Issue: "Could not create connection to database server"
**Solution:** Verify MySQL is installed and the port 3306 is not blocked by firewall

### Issue: "Table doesn't exist"
**Solution:** The tables should be created automatically. If not, run:
```sql
CREATE DATABASE inventory_db;
```
Then restart the application.

## 🔐 Security Notes

1. **DO NOT commit `database.properties` with real passwords to version control**
2. Consider using environment variables for sensitive data in production
3. Create a MySQL user with limited privileges instead of using root:

```sql
CREATE USER 'inventory_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON inventory_db.* TO 'inventory_user'@'localhost';
FLUSH PRIVILEGES;
```

Then update `database.properties`:
```properties
db.username=inventory_user
db.password=secure_password
```

## 📚 Additional Commands

### Reset Database (Clean start)
To reset the database and start fresh, you can manually run:
```sql
DROP DATABASE inventory_db;
CREATE DATABASE inventory_db;
```
Then restart the application.

### Backup Database
```powershell
mysqldump -u root -p inventory_db > backup.sql
```

### Restore Database
```powershell
mysql -u root -p inventory_db < backup.sql
```

## 💡 Tips

1. **Performance**: The connection pool is configured for optimal performance with 10 max connections
2. **Data Integrity**: All operations use transactions to ensure data consistency
3. **Backward Compatibility**: The old file-based system still works if needed
4. **Scalability**: MySQL can handle much larger datasets than CSV files

## 📞 Support

If you encounter any issues:
1. Check the console for error messages
2. Verify MySQL is running
3. Check database connection settings
4. Review the troubleshooting section above

---

**Happy Inventory Managing! 📦**
