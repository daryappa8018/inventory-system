package com.Daryappa.Inventory.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogDAO {
    
    // Log a transaction
    public void logTransaction(String date, String time, String action, String sku, int quantity, String itemName) throws SQLException {
        String sql = """
            INSERT INTO transaction_log (date, time, action, sku, quantity, item_name)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setString(3, action);
            pstmt.setString(4, sku);
            pstmt.setInt(5, quantity);
            pstmt.setString(6, itemName);
            
            pstmt.executeUpdate();
        }
    }
    
    // Get all logs
    public List<String[]> getAllLogs() throws SQLException {
        String sql = "SELECT date, time, action, sku, quantity, item_name FROM transaction_log ORDER BY id DESC";
        List<String[]> logs = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                logs.add(new String[] {
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("action"),
                    rs.getString("sku"),
                    String.valueOf(rs.getInt("quantity")),
                    rs.getString("item_name")
                });
            }
        }
        
        return logs;
    }
    
    // Filter logs by action
    public List<String[]> getLogsByAction(String action) throws SQLException {
        String sql = "SELECT date, time, action, sku, quantity, item_name FROM transaction_log WHERE action = ? ORDER BY id DESC";
        List<String[]> logs = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, action);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(new String[] {
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("action"),
                    rs.getString("sku"),
                    String.valueOf(rs.getInt("quantity")),
                    rs.getString("item_name")
                });
            }
        }
        
        return logs;
    }
    
    // Filter logs by date
    public List<String[]> getLogsByDate(String date) throws SQLException {
        String sql = "SELECT date, time, action, sku, quantity, item_name FROM transaction_log WHERE date = ? ORDER BY id DESC";
        List<String[]> logs = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(new String[] {
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("action"),
                    rs.getString("sku"),
                    String.valueOf(rs.getInt("quantity")),
                    rs.getString("item_name")
                });
            }
        }
        
        return logs;
    }
    
    // Get log count
    public int getLogCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM transaction_log";
        
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
