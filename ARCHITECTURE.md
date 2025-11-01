# Architecture Overview

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     USER INTERFACE                           │
│                                                               │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │  InventoryCLI    │         │ InventoryCLIDB   │          │
│  │  (File-based)    │         │ (Database-based) │          │
│  └────────┬─────────┘         └────────┬─────────┘          │
└───────────┼───────────────────────────┼────────────────────┘
            │                           │
┌───────────▼───────────────────────────▼────────────────────┐
│                   SERVICE LAYER                              │
│                                                               │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │InventoryManager  │         │InventoryManagerDB│          │
│  │  (HashTable)     │         │   (MySQL DAO)    │          │
│  └────────┬─────────┘         └────────┬─────────┘          │
└───────────┼───────────────────────────┼────────────────────┘
            │                           │
┌───────────▼───────────────────────────▼────────────────────┐
│                    DATA LAYER                                │
│                                                               │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │   FileHandler    │         │  Database DAOs   │          │
│  │                  │         │                  │          │
│  │ - saveInventory  │         │ - InventoryDAO   │          │
│  │ - loadInventory  │         │ - TransactionDAO │          │
│  └────────┬─────────┘         └────────┬─────────┘          │
└───────────┼───────────────────────────┼────────────────────┘
            │                           │
┌───────────▼───────────────────────────▼────────────────────┐
│                   STORAGE LAYER                              │
│                                                               │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │   CSV Files      │         │  MySQL Database  │          │
│  │                  │         │                  │          │
│  │ - inventory.csv  │         │ - inventory      │          │
│  │ - log.csv        │         │ - transaction_log│          │
│  └──────────────────┘         └──────────────────┘          │
└─────────────────────────────────────────────────────────────┘
```

## Component Description

### User Interface Layer
- **InventoryCLI**: Original CLI using file-based storage
- **InventoryCLIDB**: New CLI using database storage

### Service Layer
- **InventoryManager**: Original in-memory manager with HashTable
- **InventoryManagerDB**: New database-backed manager with DAO pattern

### Data Access Layer
- **FileHandler**: Reads/writes CSV files
- **InventoryDAO**: Database operations for inventory table
- **TransactionLogDAO**: Database operations for transaction_log table
- **DatabaseConfig**: Connection pool management (HikariCP)

### Storage Layer
- **CSV Files**: Legacy flat-file storage
- **MySQL Database**: New relational database storage

## Data Flow Diagram

### Adding an Item (Database Mode)

```
User Input
    │
    ▼
InventoryCLIDB.main()
    │
    ▼
InventoryManagerDB.addItem(record)
    │
    ▼
InventoryDAO.saveItem(record)
    │
    ▼
DatabaseConfig.getConnection()
    │
    ▼
MySQL Database INSERT/UPDATE
    │
    ▼
TransactionLoggerDB.log()
    │
    ▼
TransactionLogDAO.logTransaction()
    │
    ▼
MySQL Database INSERT
    │
    ▼
Success Message to User
```

## Class Relationships

```
┌──────────────────────────────────────────────┐
│         InventoryCLIDB (Main)                 │
│                                               │
│  + main(String[] args)                        │
└───────────┬──────────────┬───────────────────┘
            │              │
            │              └──────────────┐
            │                             │
┌───────────▼─────────────┐    ┌──────────▼────────────┐
│  InventoryManagerDB     │    │  TransactionLoggerDB  │
│                         │    │                       │
│  + addItem()            │    │  + log()              │
│  + sellItem()           │    └──────────┬────────────┘
│  + receiveStock()       │               │
│  + listLowStock()       │    ┌──────────▼────────────┐
│  + getAllItems()        │    │  TransactionLogDAO    │
│  + suggestRestocks()    │    │                       │
│  + suggestExpiringSoon()│    │  + logTransaction()   │
└───────────┬─────────────┘    │  + getAllLogs()       │
            │                  │  + getLogsByAction()  │
┌───────────▼─────────────┐    │  + getLogsByDate()    │
│     InventoryDAO        │    └───────────────────────┘
│                         │
│  + saveItem()           │
│  + getItem()            │
│  + getAllItems()        │
│  + updateQuantity()     │
│  + exists()             │
│  + getLowStockItems()   │
│  + deleteItem()         │
└───────────┬─────────────┘
            │
┌───────────▼─────────────┐
│    DatabaseConfig       │
│                         │
│  + getConnection()      │
│  - HikariDataSource     │
└─────────────────────────┘
```

## Database Schema

```
┌─────────────────────────────────┐
│         inventory               │
├─────────────────────────────────┤
│ sku               VARCHAR(50) PK│
│ name              VARCHAR(255)  │
│ quantity          INT           │
│ reorder_threshold INT           │
│ shelf_life_days   INT           │
│ created_at        TIMESTAMP     │
│ updated_at        TIMESTAMP     │
└─────────────────────────────────┘
              │
              │ Referenced by
              │ (sku column)
              ▼
┌─────────────────────────────────┐
│      transaction_log            │
├─────────────────────────────────┤
│ id                INT PK AUTO    │
│ date              VARCHAR(20)   │
│ time              VARCHAR(20)   │
│ action            VARCHAR(50)   │
│ sku               VARCHAR(50)   │
│ quantity          INT           │
│ item_name         VARCHAR(255)  │
│ created_at        TIMESTAMP     │
└─────────────────────────────────┘
```

## Connection Pool Architecture

```
┌─────────────────────────────────────────────┐
│           Application Threads                │
│  [Thread 1] [Thread 2] ... [Thread N]       │
└──────────────┬──────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────────┐
│         HikariCP Connection Pool             │
│                                              │
│  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐       │
│  │ Conn │ │ Conn │ │ Conn │ │ Conn │       │
│  │  1   │ │  2   │ │  3   │ │  4   │ ...   │
│  └──────┘ └──────┘ └──────┘ └──────┘       │
│                                              │
│  Max Pool Size: 10 connections              │
│  Min Idle: 5 connections                    │
└──────────────┬──────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────────┐
│            MySQL Database                    │
│                                              │
│  [inventory] [transaction_log]              │
└─────────────────────────────────────────────┘
```

## Migration Path

```
┌─────────────────┐
│  Legacy System  │
│                 │
│  CSV Files      │
│  inventory.csv  │
│  log.csv        │
└────────┬────────┘
         │
         │ FileHandler.loadInventory()
         │
         ▼
┌─────────────────┐
│  HashTable      │
│  (In Memory)    │
└────────┬────────┘
         │
         │ manager.setInventory()
         │
         ▼
┌─────────────────┐
│  MySQL Database │
│                 │
│  inventory      │
│  transaction_log│
└─────────────────┘
```

## Key Design Patterns Used

### 1. Data Access Object (DAO) Pattern
- `InventoryDAO`: Encapsulates database operations for inventory
- `TransactionLogDAO`: Encapsulates database operations for logs
- Benefits: Separation of concerns, easier testing, maintainability

### 2. Singleton Pattern
- `DatabaseConfig`: Single connection pool instance
- Benefits: Resource efficiency, consistent configuration

### 3. Factory Pattern
- Connection creation via `DatabaseConfig.getConnection()`
- Benefits: Abstraction, flexibility in connection management

### 4. Strategy Pattern
- Two implementations: `InventoryManager` vs `InventoryManagerDB`
- Benefits: Flexibility to switch between file and database storage

## Performance Considerations

### Connection Pooling
```
Without Pool:          With HikariCP Pool:
┌──────────┐          ┌──────────┐
│ Request  │          │ Request  │
└────┬─────┘          └────┬─────┘
     │                     │
     ▼                     ▼
  Create Conn           Get from Pool
  (100-200ms)           (< 1ms)
     │                     │
     ▼                     ▼
  Use Conn              Use Conn
     │                     │
     ▼                     ▼
  Close Conn            Return to Pool
     │                     │
     ▼                     ▼
  Destroy Conn          Reuse Conn
```

### Indexed Queries
```
Sequential Search (CSV):    Indexed Query (MySQL):
O(n) - Linear time          O(log n) - Logarithmic time

1000 items: ~1000 ops       1000 items: ~10 ops
10000 items: ~10000 ops     10000 items: ~13 ops
```

## Security Layers

```
┌─────────────────────────────────────┐
│  User Input Validation              │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  Prepared Statements                │
│  (SQL Injection Prevention)         │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  Connection Pool                    │
│  (Resource Management)              │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  MySQL User Privileges              │
│  (Access Control)                   │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  Database (ACID Properties)         │
└─────────────────────────────────────┘
```

---

This architecture provides:
- ✅ **Scalability**: Handle thousands of items
- ✅ **Performance**: Fast queries with indexing
- ✅ **Reliability**: ACID transactions
- ✅ **Maintainability**: Clean separation of concerns
- ✅ **Security**: Multiple layers of protection
- ✅ **Flexibility**: Support both file and database modes
