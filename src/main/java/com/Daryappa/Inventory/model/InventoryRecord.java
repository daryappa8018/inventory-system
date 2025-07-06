package com.Daryappa.Inventory.model;

public class InventoryRecord {
    private final String sku;  // Stock Keeping Unit - unique ID
    private String name;
    private int quantity;
    private int reorderThreshold;
    private int shelfLifeDays;
    public InventoryRecord(String sku, String name, int quantity, int reorderThreshold, int shelfLifeDays) {
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
        this.shelfLifeDays = shelfLifeDays;
    }
    public String getSku() {
        return sku;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getReorderThreshold() {
        return reorderThreshold;
    }
    public void setReorderThreshold(int reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }
    public int getShelfLifeDays() {
        return shelfLifeDays;
    }
    public void setShelfLifeDays(int shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }
    @Override
    public String toString() {
        return String.format(
                "[SKU: %s, Name: %s, Qty: %d, Threshold: %d, Shelf Life: %d days]",
                sku, name, quantity, reorderThreshold, shelfLifeDays
        );
    }
}
