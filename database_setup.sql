-- ====================================
-- Inventory Management System Database
-- ====================================

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS inventory_db;

-- Use the database
USE inventory_db;

-- ====================================
-- Table: inventory
-- Stores all inventory items
-- ====================================
CREATE TABLE IF NOT EXISTS inventory (
    sku VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    reorder_threshold INT NOT NULL DEFAULT 0,
    shelf_life_days INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_quantity (quantity),
    INDEX idx_reorder (reorder_threshold)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================
-- Table: transaction_log
-- Stores all transaction history
-- ====================================
CREATE TABLE IF NOT EXISTS transaction_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date VARCHAR(20) NOT NULL,
    time VARCHAR(20) NOT NULL,
    action VARCHAR(50) NOT NULL,
    sku VARCHAR(50),
    quantity INT,
    item_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_date (date),
    INDEX idx_action (action),
    INDEX idx_sku (sku),
    INDEX idx_date_action (date, action)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================
-- Sample Data (Optional)
-- Uncomment to insert sample data
-- ====================================

/*
-- Insert sample inventory items
INSERT INTO inventory (sku, name, quantity, reorder_threshold, shelf_life_days) VALUES
('SKU001', 'Laptop', 50, 10, 1825),
('SKU002', 'Mouse', 200, 50, 730),
('SKU003', 'Keyboard', 150, 30, 730),
('SKU004', 'Monitor', 75, 20, 1095),
('SKU005', 'USB Cable', 500, 100, 365);

-- Insert sample transaction logs
INSERT INTO transaction_log (date, time, action, sku, quantity, item_name) VALUES
('01-11-2025', '10:30:00', 'ADDED', 'SKU001', 50, 'Laptop'),
('01-11-2025', '11:15:00', 'SOLD', 'SKU001', 5, 'Laptop'),
('01-11-2025', '14:20:00', 'RESTOCKED', 'SKU002', 100, 'Mouse');
*/

-- ====================================
-- Useful Queries
-- ====================================

-- View all inventory items
-- SELECT * FROM inventory ORDER BY sku;

-- View low stock items
-- SELECT * FROM inventory WHERE quantity < reorder_threshold;

-- View recent transactions
-- SELECT * FROM transaction_log ORDER BY id DESC LIMIT 20;

-- View transactions by action type
-- SELECT * FROM transaction_log WHERE action = 'SOLD' ORDER BY id DESC;

-- View transactions by date
-- SELECT * FROM transaction_log WHERE date = '01-11-2025' ORDER BY time;

-- Count items by action
-- SELECT action, COUNT(*) as count FROM transaction_log GROUP BY action;

-- Total inventory value (assuming price field exists)
-- SELECT SUM(quantity) as total_items FROM inventory;

-- Items needing restock
-- SELECT sku, name, quantity, reorder_threshold, 
--        (reorder_threshold - quantity) as shortage
-- FROM inventory 
-- WHERE quantity < reorder_threshold
-- ORDER BY shortage DESC;

-- ====================================
-- Create User (Optional)
-- Replace 'your_password' with a secure password
-- ====================================

/*
-- Create a dedicated user for the application
CREATE USER IF NOT EXISTS 'inventory_user'@'localhost' IDENTIFIED BY 'your_password';

-- Grant privileges
GRANT SELECT, INSERT, UPDATE, DELETE ON inventory_db.* TO 'inventory_user'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;

-- Then update database.properties with:
-- db.username=inventory_user
-- db.password=your_password
*/

-- ====================================
-- Verification Queries
-- ====================================

-- Check if tables exist
SHOW TABLES;

-- Check inventory table structure
DESCRIBE inventory;

-- Check transaction_log table structure
DESCRIBE transaction_log;

-- Count records
-- SELECT 
--     (SELECT COUNT(*) FROM inventory) as inventory_count,
--     (SELECT COUNT(*) FROM transaction_log) as log_count;

SELECT 'Database setup completed successfully!' as status;
