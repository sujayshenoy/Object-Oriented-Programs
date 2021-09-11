package com.yml.jsoninventory;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class JsonInventory {
    public static void run() {
        FileReader reader;
        PrintWriter out = new PrintWriter(System.out, true);
        Map<String, Double> priceMap = new HashMap<String,Double>();

        try {
            reader = new FileReader("src/com/yml/jsoninventory/inventory.json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);

            JSONObject inventory = (JSONObject) obj;
            JSONArray array = (JSONArray) inventory.get("inventory");

            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject item = iterator.next();
                String name = (String) item.get("name");
                double weight = (double) item.get("weight");
                double pricePerKG = (double) item.get("pricePerKG");
                double totalPrice = weight * pricePerKG;
                priceMap.put(name, totalPrice);
            }
            
            JSONObject finalJSON = new JSONObject();
            finalJSON.putAll(priceMap);
            out.println(finalJSON.toJSONString());

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
