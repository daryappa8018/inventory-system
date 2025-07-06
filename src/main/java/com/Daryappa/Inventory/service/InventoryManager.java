package com.Daryappa.Inventory.service;

import com.Daryappa.Inventory.ds.HashTable;
import com.Daryappa.Inventory.model.InsufficientStockException;
import com.Daryappa.Inventory.model.InventoryRecord;
import com.Daryappa.Inventory.model.ItemNotFoundException;

import java.util.LinkedList;
import java.util.List;
//import com.Daryappa.Inventory.model.InventoryRecord;
public class InventoryManager implements IInventoryManager {
    private HashTable<String, InventoryRecord> inventory = new HashTable<>();
    @Override
    public void addItem(InventoryRecord record){
        inventory.put(record.getSku(),record);
    }
    public  void sellItem(String sku, int qty) throws InsufficientStockException, ItemNotFoundException {
        if (!inventory.containsKey(sku)) {
            throw new ItemNotFoundException("the item is not available in the inventory");

        }
        InventoryRecord item = inventory.get(sku);
        if (item.getQuantity() < qty) {
            throw new InsufficientStockException(String.format("the item %s inventory is under stock", item.getName()));
//            throw new InsufficientStockException("the item %s inventory in under stock",item.getName());        }
        }
        item.setQuantity(item.getQuantity()-qty);

    }

    public  void receiveStock(String sku, int qty) throws ItemNotFoundException{
        if (!inventory.containsKey(sku)) {
            throw new ItemNotFoundException("the item is not available in the inventory and the item restocking failed.");
        }
        InventoryRecord item = inventory.get(sku);

        item.setQuantity(item.getQuantity()+qty);
    }
    public  List<InventoryRecord> listLowStock(){
        List<InventoryRecord> thresholdItems= new LinkedList<>();
        List<String> list= inventory.keySet();
        for (String sku: list){
            InventoryRecord item = inventory.get(sku);
            if(item.getQuantity()<item.getReorderThreshold()){
                thresholdItems.add(item);
            }
        }


        return thresholdItems;
    }

}
