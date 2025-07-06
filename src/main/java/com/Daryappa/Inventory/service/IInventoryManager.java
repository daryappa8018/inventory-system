package com.Daryappa.Inventory.service;
import com.Daryappa.Inventory.model.InventoryRecord;
import com.Daryappa.Inventory.model.InsufficientStockException;
import com.Daryappa.Inventory.model.ItemNotFoundException;

import java.util.List;

public interface IInventoryManager {
    void addItem(InventoryRecord record);
    void sellItem(String sku, int qty) throws InsufficientStockException, ItemNotFoundException;
    void receiveStock(String sku, int qty) throws ItemNotFoundException;
    List<InventoryRecord> listLowStock();
}
