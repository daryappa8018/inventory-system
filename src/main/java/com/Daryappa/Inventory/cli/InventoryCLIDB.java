package com.Daryappa.Inventory.cli;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.Daryappa.Inventory.db.DatabaseInitializer;
import com.Daryappa.Inventory.ds.HashTable;
import com.Daryappa.Inventory.model.InsufficientStockException;
import com.Daryappa.Inventory.model.InventoryRecord;
import com.Daryappa.Inventory.model.ItemNotFoundException;
import com.Daryappa.Inventory.service.InventoryManagerDB;
import com.Daryappa.Inventory.utils.FileHandler;
import com.Daryappa.Inventory.utils.LogReaderDB;
import com.Daryappa.Inventory.utils.TransactionLoggerDB;

public class InventoryCLIDB {

    public static void main(String[] args) {
        
        // Initialize database tables
        System.out.println("🔧 Initializing database...");
        DatabaseInitializer.initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        InventoryManagerDB manager = new InventoryManagerDB();
        
        // Migrate legacy data if needed
        System.out.print("Do you want to load legacy CSV data into database? (yes/no): ");
        String loadLegacy = scanner.nextLine().trim().toLowerCase();
        if (loadLegacy.equals("yes")) {
            HashTable<String, InventoryRecord> loadedInventory = FileHandler.loadInventory("inventory.csv");
            manager.setInventory(loadedInventory);
        }

        System.out.println("📦 Welcome to Inventory Management System (Database Mode)");

        while (true) {
            System.out.println("\nChoose an option: add | sell | receive | list | view all | suggest restock | suggest expiry | export action | export date | exit");
            System.out.print(">> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "add":
                    System.out.print("Enter SKU: ");
                    String sku = scanner.nextLine();

                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter quantity: ");
                    int qty = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter reorder threshold: ");
                    int threshold = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter shelf life in days: ");
                    int shelfLife = Integer.parseInt(scanner.nextLine());

                    InventoryRecord item = new InventoryRecord(sku, name, qty, threshold, shelfLife);
                    manager.addItem(item);
                    TransactionLoggerDB.log("ADDED", sku, qty, name);

                    System.out.println("✅ Item added successfully to database.");
                    break;

                case "sell":
                    System.out.print("Enter SKU: ");
                    String sellSku = scanner.nextLine();

                    System.out.print("Enter quantity to sell: ");
                    int sellQty = Integer.parseInt(scanner.nextLine());

                    try {
                        manager.sellItem(sellSku, sellQty);
                        TransactionLoggerDB.log("SOLD", sellSku, sellQty, null);
                        System.out.println("✅ Item sold successfully.");
                    } catch (InsufficientStockException | ItemNotFoundException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case "receive":
                    System.out.print("Enter SKU: ");
                    String receiveSku = scanner.nextLine();

                    System.out.print("Enter quantity to receive: ");
                    int receiveQty = Integer.parseInt(scanner.nextLine());

                    try {
                        manager.receiveStock(receiveSku, receiveQty);
                        TransactionLoggerDB.log("RESTOCKED", receiveSku, receiveQty, null);
                        System.out.println("✅ Stock updated successfully.");
                    } catch (ItemNotFoundException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case "list":
                    List<InventoryRecord> lowStockItems = manager.listLowStock();
                    if (lowStockItems.isEmpty()) {
                        System.out.println("✅ All items are sufficiently stocked.");
                    } else {
                        System.out.println("📉 Low Stock Items:");
                        for (InventoryRecord r : lowStockItems) {
                            System.out.println(r);
                        }
                    }
                    break;
                    
                case "view all":
                    List<InventoryRecord> allItems = manager.getAllItems();
                    if (allItems.isEmpty()) {
                        System.out.println("📦 Inventory is empty.");
                    } else {
                        System.out.println("📦 All Inventory Items (" + allItems.size() + " items):");
                        for (InventoryRecord r : allItems) {
                            System.out.println(r);
                        }
                    }
                    break;
                    
                case "export action":
                    System.out.print("Enter action (ADDED / SOLD / RESTOCKED): ");
                    String action = scanner.nextLine().trim().toUpperCase();
                    List<String[]> filteredByAction = LogReaderDB.filterByAction(action);
                    if (filteredByAction.isEmpty()) {
                        System.out.println("❌ No logs found for action: " + action);
                    } else {
                        LogReaderDB.exportToFile(filteredByAction, action);
                    }
                    break;

                case "export date":
                    System.out.print("Enter date (dd-MM-yyyy): ");
                    String date = scanner.nextLine().trim();
                    List<String[]> filteredByDate = LogReaderDB.filterByDate(date);
                    if (filteredByDate.isEmpty()) {
                        System.out.println("❌ No logs found on date: " + date);
                    } else {
                        LogReaderDB.exportToFile(filteredByDate, "FILTERED");
                    }
                    break;

                case "suggest restock":
                    System.out.print("How many top urgent items to suggest?: ");
                    int restockCount = Integer.parseInt(scanner.nextLine());

                    List<InventoryRecord> restockSuggestions = manager.suggestRestocks(restockCount);
                    if (restockSuggestions.isEmpty()) {
                        System.out.println("✅ All items are sufficiently stocked.");
                    } else {
                        System.out.println("🔻 Top " + restockCount + " items needing restock:");
                        for (InventoryRecord r : restockSuggestions) {
                            System.out.println(r);
                        }

                        System.out.print("Do you want to export to CSV? (yes/no): ");
                        String exportChoice1 = scanner.nextLine().trim().toLowerCase();
                        if (exportChoice1.equals("yes")) {
                            List<String[]> exportData = new ArrayList<>();
                            for (InventoryRecord r : restockSuggestions) {
                                exportData.add(new String[] {
                                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                        "RESTOCK_SUGGESTION",
                                        r.getSku(),
                                        String.valueOf(r.getQuantity()),
                                        r.getName()
                                });
                            }
                            LogReaderDB.exportToFile(exportData, "RESTOCK_SUGGESTION");
                        }
                    }
                    break;

                case "suggest expiry":
                    System.out.print("How many soon-to-expire items to suggest?: ");
                    int expiryCount = Integer.parseInt(scanner.nextLine());

                    List<InventoryRecord> expiringSuggestions = manager.suggestExpiringSoon(expiryCount);
                    if (expiringSuggestions.isEmpty()) {
                        System.out.println("✅ No items are close to expiry.");
                    } else {
                        System.out.println("⏳ Top " + expiryCount + " expiring soon items:");
                        for (InventoryRecord r : expiringSuggestions) {
                            System.out.println(r);
                        }

                        System.out.print("Do you want to export to CSV? (yes/no): ");
                        String exportChoice2 = scanner.nextLine().trim().toLowerCase();
                        if (exportChoice2.equals("yes")) {
                            List<String[]> exportData = new ArrayList<>();
                            for (InventoryRecord r : expiringSuggestions) {
                                exportData.add(new String[] {
                                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                        "EXPIRY_SUGGESTION",
                                        r.getSku(),
                                        String.valueOf(r.getShelfLifeDays()),
                                        r.getName()
                                });
                            }
                            LogReaderDB.exportToFile(exportData, "EXPIRY_SUGGESTION");
                        }
                    }
                    break;

                case "exit":
                    System.out.println("👋 Exiting Inventory System. All data saved to database. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("❌ Invalid command. Please try again.");
            }
        }
    }
}
