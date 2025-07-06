package com.Daryappa.Inventory.model;

public class InventoryRecord {
    private final String sku;  // Unique Stock Keeping Unit
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
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(int reorderThreshold) {
        if (reorderThreshold < 0) throw new IllegalArgumentException("Threshold cannot be negative");
        this.reorderThreshold = reorderThreshold;
    }

    public int getShelfLifeDays() {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(int shelfLifeDays) {
        if (shelfLifeDays < 0) throw new IllegalArgumentException("Shelf life cannot be negative");
        this.shelfLifeDays = shelfLifeDays;
    }

    public boolean isLowStock() {
        return quantity <= reorderThreshold;
    }

    public boolean isExpired(int currentDay) {
        return shelfLifeDays < currentDay;
    }

    public boolean isValidRecord() {
        return sku != null && !sku.isEmpty() &&
                name != null && !name.isEmpty() &&
                quantity >= 0 && reorderThreshold >= 0 && shelfLifeDays >= 0;
    }

    public void sell(int qty) {
        if (qty < 0) throw new IllegalArgumentException("Quantity to sell cannot be negative");
        if (qty > quantity) throw new IllegalArgumentException("Insufficient stock to sell " + qty + " items");
        this.quantity -= qty;
    }

    public void receive(int qty) {
        if (qty < 0) throw new IllegalArgumentException("Quantity received cannot be negative");
        this.quantity += qty;
    }

    public void updateRecord(String name, int quantity, int reorderThreshold, int shelfLifeDays) {
        setName(name);
        setQuantity(quantity);
        setReorderThreshold(reorderThreshold);
        setShelfLifeDays(shelfLifeDays);
    }

    @Override
    public String toString() {
        return String.format("[SKU: %s, Name: %s, Qty: %d, Threshold: %d, Shelf Life: %d days]",
                sku, name, quantity, reorderThreshold, shelfLifeDays);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof InventoryRecord)) return false;
        InventoryRecord other = (InventoryRecord) obj;
        return sku.equals(other.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}
