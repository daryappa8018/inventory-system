package com.Daryappa.Inventory.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.Daryappa.Inventory.db.TransactionLogDAO;

public class LogReaderDB {
    private static final TransactionLogDAO logDAO = new TransactionLogDAO();
    
    public static List<String[]> filterByAction(String action) {
        try {
            return logDAO.getLogsByAction(action);
        } catch (SQLException e) {
            System.err.println("Error filtering logs by action: " + e.getMessage());
            // Fallback to file-based system
            return LogReader.filterByAction("log.csv", action);
        }
    }
    
    public static List<String[]> filterByDate(String date) {
        try {
            return logDAO.getLogsByDate(date);
        } catch (SQLException e) {
            System.err.println("Error filtering logs by date: " + e.getMessage());
            // Fallback to file-based system
            return LogReader.filterByDate("log.csv", date);
        }
    }
    
    public static List<String[]> getAllLogs() {
        try {
            return logDAO.getAllLogs();
        } catch (SQLException e) {
            System.err.println("Error getting all logs: " + e.getMessage());
            return List.of();
        }
    }
    
    public static void exportToFile(List<String[]> data, String prefix) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH-mm-ss");
        
        String date = LocalDate.now().format(formatter1);
        String time = LocalTime.now().format(formatter2);
        String filename = "src/main/java/com/Daryappa/Inventory/exports/" + prefix + "_" + date + "_" + time + ".csv";
        
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Date,Time,Action,SKU,Quantity,ItemName\n");
            
            for (String[] row : data) {
                writer.write(String.join(",", row) + "\n");
            }
            
            System.out.println("✅ Exported to: " + filename);
            
        } catch (IOException e) {
            System.err.println("Error exporting to file: " + e.getMessage());
        }
    }
}
