package com.Daryappa.Inventory.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.Daryappa.Inventory.model.InventoryRecord;

public class InventoryDAO {
    
    // Create or update inventory item
    public void saveItem(InventoryRecord record) throws SQLException {
        String sql = """
            INSERT INTO inventory (sku, name, quantity, reorder_threshold, shelf_life_days)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                name = VALUES(name),
                quantity = VALUES(quantity),
                reorder_threshold = VALUES(reorder_threshold),
                shelf_life_days = VALUES(shelf_life_days)
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getSku());
            pstmt.setString(2, record.getName());
            pstmt.setInt(3, record.getQuantity());
            pstmt.setInt(4, record.getReorderThreshold());
            pstmt.setInt(5, record.getShelfLifeDays());
            
            pstmt.executeUpdate();
        }
    }
    
    // Get item by SKU
    public InventoryRecord getItem(String sku) throws SQLException {
        String sql = "SELECT * FROM inventory WHERE sku = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sku);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new InventoryRecord(
                    rs.getString("sku"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getInt("reorder_threshold"),
                    rs.getInt("shelf_life_days")
                );
            }
            return null;
        }
    }
    
    // Get all items
    public List<InventoryRecord> getAllItems() throws SQLException {
        String sql = "SELECT * FROM inventory ORDER BY sku";
        List<InventoryRecord> items = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                items.add(new InventoryRecord(
                    rs.getString("sku"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getInt("reorder_threshold"),
                    rs.getInt("shelf_life_days")
                ));
            }
        }
        
        return items;
    }
    
    // Update quantity
    public void updateQuantity(String sku, int newQuantity) throws SQLException {
        String sql = "UPDATE inventory SET quantity = ? WHERE sku = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newQuantity);
            pstmt.setString(2, sku);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Item with SKU " + sku + " not found");
            }
        }
    }
    
    // Check if item exists
    public boolean exists(String sku) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inventory WHERE sku = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sku);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    // Get low stock items
    public List<InventoryRecord> getLowStockItems() throws SQLException {
        String sql = "SELECT * FROM inventory WHERE quantity < reorder_threshold ORDER BY quantity ASC";
        List<InventoryRecord> items = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                items.add(new InventoryRecord(
                    rs.getString("sku"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getInt("reorder_threshold"),
                    rs.getInt("shelf_life_days")
                ));
            }
        }
        
        return items;
    }
    
    // Delete item
    public void deleteItem(String sku) throws SQLException {
        String sql = "DELETE FROM inventory WHERE sku = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sku);
            pstmt.executeUpdate();
        }
    }
    
    // Get total item count
    public int getTotalItemCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inventory";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}
