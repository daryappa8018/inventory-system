package com.Daryappa.Inventory.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Daryappa.Inventory.db.InventoryDAO;
import com.Daryappa.Inventory.ds.HashTable;
import com.Daryappa.Inventory.ds.PriorityQueue;
import com.Daryappa.Inventory.model.InsufficientStockException;
import com.Daryappa.Inventory.model.InventoryRecord;
import com.Daryappa.Inventory.model.ItemNotFoundException;

public class InventoryManagerDB implements IInventoryManager {
    private InventoryDAO inventoryDAO;
    
    public InventoryManagerDB() {
        this.inventoryDAO = new InventoryDAO();
    }
    
    @Override
    public void addItem(InventoryRecord record) {
        try {
            inventoryDAO.saveItem(record);
        } catch (SQLException e) {
            System.err.println("Error adding item to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void sellItem(String sku, int qty) throws InsufficientStockException, ItemNotFoundException {
        try {
            if (!inventoryDAO.exists(sku)) {
                throw new ItemNotFoundException("The item is not available in the inventory");
            }
            
            InventoryRecord item = inventoryDAO.getItem(sku);
            if (item.getQuantity() < qty) {
                throw new InsufficientStockException(
                    String.format("The item %s inventory is under stock", item.getName())
                );
            }
            
            item.setQuantity(item.getQuantity() - qty);
            inventoryDAO.saveItem(item);
            
        } catch (SQLException e) {
            System.err.println("Error selling item: " + e.getMessage());
            throw new ItemNotFoundException("Database error occurred");
        }
    }
    
    public void receiveStock(String sku, int qty) throws ItemNotFoundException {
        try {
            if (!inventoryDAO.exists(sku)) {
                throw new ItemNotFoundException("The item is not available in the inventory and the item restocking failed.");
            }
            
            InventoryRecord item = inventoryDAO.getItem(sku);
            item.setQuantity(item.getQuantity() + qty);
            inventoryDAO.saveItem(item);
            
        } catch (SQLException e) {
            System.err.println("Error receiving stock: " + e.getMessage());
            throw new ItemNotFoundException("Database error occurred");
        }
    }
    
    public List<InventoryRecord> listLowStock() {
        try {
            return inventoryDAO.getLowStockItems();
        } catch (SQLException e) {
            System.err.println("Error listing low stock items: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<InventoryRecord> getAllItems() {
        try {
            return inventoryDAO.getAllItems();
        } catch (SQLException e) {
            System.err.println("Error getting all items: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<InventoryRecord> suggestRestocks(int count) {
        try {
            PriorityQueue<InventoryRecord> pq = new PriorityQueue<>();
            List<InventoryRecord> items = inventoryDAO.getAllItems();
            List<Integer> priorities = new ArrayList<>();
            List<InventoryRecord> result = new ArrayList<>();
            
            for (InventoryRecord item : items) {
                int threshold = item.getReorderThreshold();
                int priority;
                // if threshold is 0, treat it as urgent (high priority)
                if (threshold == 0) {
                    priority = 0;
                } else {
                    priority = item.getQuantity() * 100 / threshold;
                }
                priorities.add(priority);
            }
            
            pq.heapify(items, priorities);
            for (int i = 0; i < count && !pq.isEmpty(); i++) {
                result.add(pq.poll());
            }
            
            return result;
        } catch (SQLException e) {
            System.err.println("Error suggesting restocks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<InventoryRecord> suggestExpiringSoon(int count) {
        try {
            PriorityQueue<InventoryRecord> pq = new PriorityQueue<>();
            List<InventoryRecord> items = inventoryDAO.getAllItems();
            List<Integer> priorities = new ArrayList<>();
            List<InventoryRecord> result = new ArrayList<>();
            
            for (InventoryRecord item : items) {
                priorities.add(item.getShelfLifeDays());
            }
            
            pq.heapify(items, priorities);
            for (int i = 0; i < count && !pq.isEmpty(); i++) {
                result.add(pq.poll());
            }
            
            return result;
        } catch (SQLException e) {
            System.err.println("Error suggesting expiring items: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // For backward compatibility with old file-based system
    public void setInventory(HashTable<String, InventoryRecord> loadedInventory) {
        try {
            List<String> keys = loadedInventory.keySet();
            for (String sku : keys) {
                InventoryRecord item = loadedInventory.get(sku);
                if (item != null) {
                    inventoryDAO.saveItem(item);
                }
            }
            System.out.println("✅ Legacy inventory loaded into database successfully.");
        } catch (SQLException e) {
            System.err.println("Error loading legacy inventory: " + e.getMessage());
        }
    }
    
    public HashTable<String, InventoryRecord> getInventory() {
        HashTable<String, InventoryRecord> inventory = new HashTable<>();
        try {
            List<InventoryRecord> items = inventoryDAO.getAllItems();
            for (InventoryRecord item : items) {
                inventory.put(item.getSku(), item);
            }
        } catch (SQLException e) {
            System.err.println("Error getting inventory: " + e.getMessage());
        }
        return inventory;
    }
}
