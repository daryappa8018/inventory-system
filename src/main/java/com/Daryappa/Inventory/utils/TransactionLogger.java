package com.Daryappa.Inventory.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;


import java.io.FileWriter;
import java.io.IOException;

public class TransactionLogger {
    public static void log(String actionType, String sku, int qty, String Name){

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");

        String date = LocalDate.now().format(formatter1);
        String time = LocalTime.now().format(formatter2);
        try(FileWriter writer = new FileWriter("log.csv", true)){

            String line= date + "," +
                    time + "," +
                    actionType + "," +
                    sku + "," +
                    qty + "," +
                    Name + "\n";
            writer.write(line);


        }catch (IOException e){
            System.out.println("File Error:" + e.getMessage());
        }
    }

}
