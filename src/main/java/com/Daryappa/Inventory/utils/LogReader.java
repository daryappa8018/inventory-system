package com.Daryappa.Inventory.utils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LogReader {

    public static List<String[]> filterByDate(String filePath, String date) {
        List<String[]> result = new ArrayList<>();
        // We will implement this
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                if (line.startsWith(date)){
                    result.add(line.split(",",-1));
                }
            }
        }catch (IOException e){
            System.out.println("logging file error:"+ e.getMessage());
        }
        return result;
    }

    public static List<String[]> filterByAction(String filePath, String action) {
        List<String[]> result = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                String[] parts=line.split(",");
                if (parts.length >= 3 && parts[2].equalsIgnoreCase(action)) {
                    result.add(parts);
                }
            }
        }catch (IOException e){
            System.out.println("logging file error:"+ e.getMessage());
        }
        return result;
    }

    public static void exportToFile(List<String[]> data, String action) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String date = LocalDate.now().format(dateFormatter);
        String time = LocalTime.now().format(timeFormatter);
        String exportDirectory="src/main/java/com/Daryappa/Inventory/exports";
        String exportPath= exportDirectory+"/"+action.toUpperCase()+ "_" + date+ "_" + time + ".csv";
        try (FileWriter writer = new FileWriter(exportPath)) {
            writer.write("Date,Time,Action,SKU,Quantity,ItemName\n");
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
            System.out.println("Exported filtered data to: " + exportPath);
        } catch (IOException e) {
            System.out.println("Export error: " + e.getMessage());
        }
    }
}
