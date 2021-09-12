package com.yml.inventorymanager;

import java.io.PrintWriter;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InventoryManager {
    public static void run() {
        InventoryFactory inventoryFactory = new InventoryFactory();
        List<JSONObject> inventories = new ArrayList<JSONObject>();
        PrintWriter out = new PrintWriter(System.out,true);
        
        //Iterate through the inventory list and calculate total price for each item in an inventory
        for (JSONArray inventory : inventoryFactory.getInventories()) {
            //priceMap to hold all the items and their total price in a inventory
            Map<String, Double> priceMap = new HashMap<String, Double>();
            Iterator<JSONObject> itr = inventory.iterator();
            while (itr.hasNext()) {
                JSONObject item = (JSONObject) itr.next();
                String name = (String) item.get("name");
                double weight = (double) item.get("weight");
                double pricePerKG = (double) item.get("pricePerKG");
                double totalPrice = weight * pricePerKG;
                priceMap.put(name, totalPrice);
            }

            //convert Map to JSON and add to inventories array
            JSONObject inventoryJSON = new JSONObject();
            inventoryJSON.putAll(priceMap);
            inventories.add(inventoryJSON);
        }
        
        //Traverse through all the inventories and calculate total cost of an inventory and display JSON string of items in it.
        int count = 1;
        for (JSONObject inventory : inventories) {
            out.println("Inventory "+count);
            double sum = 0;
            for (Object item : inventory.keySet()) {
                sum += (double) inventory.get(item);
            }
            out.println("Cost: " + sum);
            out.println("Inventory Items");
            out.println(inventory.toJSONString());
            out.println();
            count++;
        }
    }
}
