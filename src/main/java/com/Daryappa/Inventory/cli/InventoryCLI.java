package com.Daryappa.Inventory.cli;

import com.Daryappa.Inventory.ds.HashTable;
import com.Daryappa.Inventory.model.InventoryRecord;
import com.Daryappa.Inventory.model.InsufficientStockException;
import com.Daryappa.Inventory.model.ItemNotFoundException;
import com.Daryappa.Inventory.service.InventoryManager;
import com.Daryappa.Inventory.utils.FileHandler;
import com.Daryappa.Inventory.utils.TransactionLogger;

import java.util.List;
import java.util.Scanner;

public class InventoryCLI {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();
        HashTable<String, InventoryRecord> loadedInventory = FileHandler.loadInventory("inventory.csv");
        manager.setInventory(loadedInventory);

        System.out.println("📦 Welcome to Inventory Management System");

        while (true) {
            System.out.println("\nChoose an option: add | sell | receive | list | exit");
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
                    TransactionLogger.log("ADDED", sku, qty, name);

                    System.out.println("Item added successfully.");
                    break;

                case "sell":
                    System.out.print("Enter SKU: ");
                    String sellSku = scanner.nextLine();

                    System.out.print("Enter quantity to sell: ");
                    int sellQty = Integer.parseInt(scanner.nextLine());

                    try {
                        manager.sellItem(sellSku, sellQty);
                        TransactionLogger.log("SOLD", sellSku, sellQty, null);
                        System.out.println("Item sold successfully.");
                    } catch (InsufficientStockException | ItemNotFoundException e) {
                        System.out.println(" Error: " + e.getMessage());
                    }
                    break;

                case "receive":
                    System.out.print("Enter SKU: ");
                    String receiveSku = scanner.nextLine();

                    System.out.print("Enter quantity to receive: ");
                    int receiveQty = Integer.parseInt(scanner.nextLine());

                    try {
                        manager.receiveStock(receiveSku, receiveQty);
                        TransactionLogger.log("RESTOCKED", receiveSku, receiveQty, null);
                        System.out.println("Stock updated successfully.");
                    } catch (ItemNotFoundException e) {
                        System.out.println(" Error: " + e.getMessage());
                    }
                    break;

                case "list":
                    List<InventoryRecord> lowStockItems = manager.listLowStock();
                    if (lowStockItems.isEmpty()) {
                        System.out.println("All items are sufficiently stocked.");
                    } else {
                        System.out.println("Low Stock Items:");
                        for (InventoryRecord r : lowStockItems) {
                            System.out.println(r);
                        }
                    }
                    break;

                case "exit":
                    System.out.println("Saving and Exiting Inventory System. Goodbye!");
                    FileHandler.saveInventory(manager.getInventory(), "inventory.csv");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }
}
