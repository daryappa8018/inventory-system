package com.Daryappa.Inventory.utils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.Daryappa.Inventory.db.TransactionLogDAO;

public class TransactionLoggerDB {
    private static final TransactionLogDAO logDAO = new TransactionLogDAO();
    
    public static void log(String actionType, String sku, int qty, String name) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        String date = LocalDate.now().format(formatter1);
        String time = LocalTime.now().format(formatter2);
        
        try {
            logDAO.logTransaction(date, time, actionType, sku, qty, name);
        } catch (SQLException e) {
            System.err.println("Error logging transaction to database: " + e.getMessage());
            // Fallback to file-based logging
            TransactionLogger.log(actionType, sku, qty, name);
        }
    }
}
