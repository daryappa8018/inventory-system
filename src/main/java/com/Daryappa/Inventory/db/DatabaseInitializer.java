package com.Daryappa.Inventory.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create inventory table
            String createInventoryTable = """
                CREATE TABLE IF NOT EXISTS inventory (
                    sku VARCHAR(50) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    quantity INT NOT NULL DEFAULT 0,
                    reorder_threshold INT NOT NULL DEFAULT 0,
                    shelf_life_days INT NOT NULL DEFAULT 0,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createInventoryTable);
            
            // Create transaction log table
            String createLogTable = """
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
                    INDEX idx_sku (sku)
                )
            """;
            stmt.execute(createLogTable);
            
            System.out.println("✅ Database tables initialized successfully.");
            
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void resetDatabase() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("DROP TABLE IF EXISTS transaction_log");
            stmt.execute("DROP TABLE IF EXISTS inventory");
            
            System.out.println("✅ Database reset successfully.");
            initializeDatabase();
            
        } catch (SQLException e) {
            System.err.println("❌ Error resetting database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
