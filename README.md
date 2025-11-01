# 📦 Inventory Management System with MySQL

A professional-grade inventory management system built with Java and MySQL, featuring real-time stock tracking, transaction logging, and intelligent restock suggestions.

## 🌟 Features

### Core Functionality
- ✅ Add, update, and track inventory items
- ✅ Sell items with automatic stock deduction
- ✅ Receive stock (restocking)
- ✅ Low stock alerts and monitoring
- ✅ Shelf-life tracking for perishable items
- ✅ Transaction logging with timestamps
- ✅ Export capabilities (CSV format)

### Advanced Features
- 🎯 **Priority-based restock suggestions** using custom PriorityQueue
- ⏳ **Expiring soon items detection** 
- 📊 **Filter transactions** by date or action type
- 🗄️ **MySQL database integration** for persistent storage
- ⚡ **Connection pooling** (HikariCP) for optimal performance
- 🔄 **Legacy CSV support** for backward compatibility

### Technical Highlights
- Custom HashTable implementation for efficient lookups
- Custom PriorityQueue for intelligent sorting
- DAO (Data Access Object) pattern for clean architecture
- Prepared statements for SQL injection prevention
- ACID transaction support
- Automatic database initialization

## 🏗️ Technology Stack

- **Language**: Java 22
- **Database**: MySQL 8.0+
- **Build Tool**: Maven
- **Database Driver**: MySQL Connector/J 8.2.0
- **Connection Pooling**: HikariCP 5.1.0
- **Architecture**: MVC with DAO pattern

## 📁 Project Structure

```
Inventory Maneger/
├── src/main/java/com/Daryappa/Inventory/
│   ├── cli/
│   │   ├── InventoryCLI.java          # Legacy file-based interface
│   │   └── InventoryCLIDB.java        # Database-powered interface
│   ├── db/
│   │   ├── DatabaseConfig.java         # Connection management
│   │   ├── DatabaseInitializer.java   # Schema setup
│   │   ├── InventoryDAO.java          # Inventory data access
│   │   └── TransactionLogDAO.java     # Transaction data access
│   ├── ds/
│   │   ├── HashTable.java             # Custom hash table
│   │   └── PriorityQueue.java         # Custom priority queue
│   ├── model/
│   │   ├── InventoryRecord.java       # Item model
│   │   ├── InsufficientStockException.java
│   │   └── ItemNotFoundException.java
│   ├── service/
│   │   ├── IInventoryManager.java     # Interface
│   │   ├── InventoryManager.java      # File-based implementation
│   │   └── InventoryManagerDB.java    # Database implementation
│   └── utils/
│       ├── FileHandler.java           # CSV operations
│       ├── TransactionLogger.java     # File logging
│       ├── TransactionLoggerDB.java   # Database logging
│       ├── LogReader.java             # File log reader
│       └── LogReaderDB.java           # Database log reader
├── src/main/resources/
│   └── database.properties            # Database configuration
├── docs/
│   ├── QUICKSTART.md                  # 5-minute setup guide
│   ├── INSTALLATION.md                # Detailed installation
│   ├── DATABASE_SETUP.md              # Database documentation
│   ├── ARCHITECTURE.md                # System architecture
│   └── SUMMARY.md                     # Integration summary
├── database_setup.sql                 # SQL initialization script
├── run-database.bat                   # Run database version
├── run-legacy.bat                     # Run file version
└── pom.xml                           # Maven configuration
```

## 🚀 Quick Start

### Prerequisites
1. **MySQL Server** (8.0+) - [Download](https://dev.mysql.com/downloads/mysql/)
2. **Java** (22+) - Already configured
3. **Maven** - For dependency management

### Installation (3 Steps)

1. **Install MySQL and start the service**
   ```powershell
   # Verify MySQL is running
   Get-Service MySQL80
   ```

2. **Configure database connection**
   
   Edit `src/main/resources/database.properties`:
   ```properties
   db.password=YOUR_MYSQL_ROOT_PASSWORD
   ```

3. **Run the application**
   
   Double-click `run-database.bat` or use:
   ```powershell
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"
   ```

## 📖 Documentation

| Document | Description |
|----------|-------------|
| [QUICKSTART.md](QUICKSTART.md) | Get started in 5 minutes |
| [INSTALLATION.md](INSTALLATION.md) | Detailed installation guide |
| [DATABASE_SETUP.md](DATABASE_SETUP.md) | Database features & usage |
| [ARCHITECTURE.md](ARCHITECTURE.md) | System design & diagrams |
| [SUMMARY.md](SUMMARY.md) | Integration overview |

## 💻 Usage Examples

### Adding an Item
```
>> add
Enter SKU: LAPTOP001
Enter item name: Dell Laptop
Enter quantity: 50
Enter reorder threshold: 10
Enter shelf life in days: 1825
✅ Item added successfully to database.
```

### Selling Items
```
>> sell
Enter SKU: LAPTOP001
Enter quantity to sell: 5
✅ Item sold successfully.
```

### Viewing Low Stock
```
>> list
📉 Low Stock Items:
[SKU: MOUSE001, Name: Wireless Mouse, Qty: 8, Threshold: 20, Shelf Life: 730 days]
```

### Restock Suggestions
```
>> suggest restock
How many top urgent items to suggest?: 5
🔻 Top 5 items needing restock:
[SKU: MOUSE001, Name: Wireless Mouse, Qty: 8, Threshold: 20, Shelf Life: 730 days]
...
```

## 🗄️ Database Schema

### inventory Table
| Column | Type | Description |
|--------|------|-------------|
| sku | VARCHAR(50) | Primary key, unique identifier |
| name | VARCHAR(255) | Item name |
| quantity | INT | Current stock quantity |
| reorder_threshold | INT | Alert threshold |
| shelf_life_days | INT | Days until expiration |
| created_at | TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | Last update time |

### transaction_log Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT | Auto-increment primary key |
| date | VARCHAR(20) | Transaction date |
| time | VARCHAR(20) | Transaction time |
| action | VARCHAR(50) | Action type (ADDED/SOLD/RESTOCKED) |
| sku | VARCHAR(50) | Item SKU |
| quantity | INT | Quantity involved |
| item_name | VARCHAR(255) | Item name |
| created_at | TIMESTAMP | Record creation time |

## 🎯 Available Commands

| Command | Description |
|---------|-------------|
| `add` | Add new inventory item |
| `sell` | Sell items (reduces quantity) |
| `receive` | Receive stock (increases quantity) |
| `list` | Show low stock items |
| `view all` | Display all inventory items |
| `suggest restock` | Priority-based restock recommendations |
| `suggest expiry` | Items expiring soon |
| `export action` | Export transactions by action type |
| `export date` | Export transactions by date |
| `exit` | Save and exit application |

## 🔍 Querying the Database

Connect to MySQL and explore your data:

```sql
-- View all inventory
SELECT * FROM inventory ORDER BY quantity ASC;

-- Find low stock items
SELECT * FROM inventory WHERE quantity < reorder_threshold;

-- Transaction history
SELECT * FROM transaction_log ORDER BY id DESC LIMIT 10;

-- Transactions by action
SELECT action, COUNT(*) as count 
FROM transaction_log 
GROUP BY action;

-- Items sold today
SELECT * FROM transaction_log 
WHERE action = 'SOLD' 
AND date = '01-11-2025';
```

## 🔧 Configuration

### Database Settings
Edit `src/main/resources/database.properties`:

```properties
# Connection
db.url=jdbc:mysql://localhost:3306/inventory_db
db.username=root
db.password=your_password

# Connection Pool
db.pool.maximumPoolSize=10
db.pool.minimumIdle=5
db.pool.connectionTimeout=30000
```

### Creating Dedicated MySQL User
For better security:

```sql
CREATE USER 'inventory_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON inventory_db.* TO 'inventory_user'@'localhost';
FLUSH PRIVILEGES;
```

## 🐛 Troubleshooting

### MySQL Connection Failed
```powershell
# Check MySQL service
Get-Service MySQL80

# Start MySQL if stopped
Start-Service MySQL80
```

### Access Denied Error
- Verify password in `database.properties`
- Ensure MySQL user has proper privileges

### Maven Not Found
- Install Maven from [maven.apache.org](https://maven.apache.org)
- Add Maven bin folder to System PATH
- Restart terminal/IDE

### Dependencies Not Loading
```powershell
# Clear Maven cache and reinstall
mvn clean install -U
```

See [INSTALLATION.md](INSTALLATION.md) for detailed troubleshooting.

## 📊 Performance

### Connection Pooling Benefits
- **Without pool**: 100-200ms per connection
- **With HikariCP**: < 1ms per connection
- **Result**: 100-200x faster database access

### Scalability
- **CSV (old)**: ~1000 items before performance degrades
- **MySQL (new)**: Millions of items with consistent performance

### Query Optimization
- Indexed columns for fast lookups
- Prepared statements for query caching
- Connection reuse via pooling

## 🛡️ Security Features

1. **Prepared Statements** - Prevents SQL injection
2. **Connection Pooling** - Prevents resource exhaustion
3. **User Privileges** - Limit database access
4. **Password Protection** - Credentials in config file
5. **Transaction Support** - Ensures data integrity

## 🔄 Migration from CSV

The system supports automatic migration from existing CSV files:

1. Keep your `inventory.csv` file
2. Run the database version
3. When prompted, type "yes" to import CSV data
4. All data will be migrated to MySQL

Both systems can coexist:
- **Database mode**: Full features, persistent storage
- **File mode**: Legacy support, CSV-based

## 🌐 Future Enhancements

Potential features to add:
- [ ] Web interface (Spring Boot + React)
- [ ] REST API for mobile apps
- [ ] User authentication & authorization
- [ ] Multi-user support with roles
- [ ] Real-time notifications
- [ ] Barcode scanning
- [ ] Invoice generation
- [ ] Supplier management
- [ ] Analytics dashboard
- [ ] Automated reports

## 📝 Testing

Verify the setup:

```bash
# Run the application
mvn exec:java -Dexec.mainClass="com.Daryappa.Inventory.cli.InventoryCLIDB"

# Add a test item
>> add
Enter SKU: TEST001
...

# Verify in MySQL
mysql -u root -p
USE inventory_db;
SELECT * FROM inventory WHERE sku = 'TEST001';
```

## 📄 License

This project is developed as an educational project for learning Java and database integration.

## 👤 Author

Daryappa
- GitHub: [@daryappa8018](https://github.com/daryappa8018)

## 🙏 Acknowledgments

- Custom data structures (HashTable, PriorityQueue) implemented from scratch
- MySQL Connector/J for database connectivity
- HikariCP for high-performance connection pooling

## 📞 Support

Having issues? Check these resources:

1. **Quick Setup**: [QUICKSTART.md](QUICKSTART.md)
2. **Installation Help**: [INSTALLATION.md](INSTALLATION.md)
3. **Database Info**: [DATABASE_SETUP.md](DATABASE_SETUP.md)
4. **Architecture**: [ARCHITECTURE.md](ARCHITECTURE.md)
5. **Console Errors**: Check application output for specific errors
6. **MySQL**: Verify service is running and credentials are correct

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Object-Oriented Programming (OOP) principles
- ✅ Custom data structure implementation
- ✅ Database integration (JDBC)
- ✅ DAO design pattern
- ✅ Connection pooling
- ✅ Exception handling
- ✅ File I/O operations
- ✅ Maven dependency management
- ✅ SQL query optimization
- ✅ Transaction management

## 🚀 Getting Started Today

1. **Read**: [QUICKSTART.md](QUICKSTART.md) (5 minutes)
2. **Install**: MySQL and configure password (10 minutes)
3. **Run**: Double-click `run-database.bat` (1 minute)
4. **Explore**: Try adding items and viewing reports

**Total time to get started: ~15 minutes**

---

**Happy Inventory Managing! 📦✨**

For detailed documentation, see the `/docs` folder or the individual markdown files in the project root.
