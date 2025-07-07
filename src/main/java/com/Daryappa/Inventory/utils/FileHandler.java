package com.Daryappa.Inventory.utils;

import com.Daryappa.Inventory.ds.HashTable;
import com.Daryappa.Inventory.model.InventoryRecord;
import com.Daryappa.Inventory.model.ItemNotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileHandler {

    public static void saveInventory(HashTable<String, InventoryRecord> inventory, String filePath) {
        try {
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

//            FileWriter writer = new FileWriter(filePath, true);  // append mode
            FileWriter writer = new FileWriter(filePath);
            String formatted = time.format(formatter);

            // Header and timestamp
            writer.write("Inventory Saved At: " + formatted + "\n");
            writer.write("sku,name,quantity,reorderThreshold,shelfLifeDays\n");

            List<String> keys = inventory.keySet();
            for (String sku : keys) {
                InventoryRecord item = inventory.get(sku);
                if (item == null) throw new ItemNotFoundException("Inventory missing for SKU: " + sku);

                String line = sku + "," +
                        item.getName() + "," +
                        item.getQuantity() + "," +
                        item.getReorderThreshold() + "," +
                        item.getShelfLifeDays() + "\n";

                writer.write(line);
            }

            writer.write("\n");
            writer.close();
            System.out.println("Inventory saved to file successfully.");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ItemNotFoundException e) {
            System.out.println("Missing data: " + e.getMessage());
        }
    }
    public static HashTable<String, InventoryRecord> loadInventory(String filePath){
        HashTable<String, InventoryRecord> inventory= new HashTable<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();// skip the time
            line = reader.readLine(); // skip the header
            while ((line = reader.readLine()) != null) {

                String[] lineSplit = line.split(",");
                if (line.trim().isEmpty() || lineSplit.length < 5) continue;
                InventoryRecord item = new InventoryRecord(lineSplit[0],lineSplit[1],Integer.parseInt(lineSplit[2]),Integer.parseInt(lineSplit[3]),Integer.parseInt(lineSplit[4]));
                inventory.put(lineSplit[0],item);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return inventory;
    }

}