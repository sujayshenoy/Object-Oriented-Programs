package com.yml.jsoninventory;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class JsonInventory {
    public static void run() {
        FileReader reader;
        PrintWriter out = new PrintWriter(System.out,true);

        try {
            reader = new FileReader("src/com/yml/jsoninventory/inventory.json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);

            JSONObject inventory = (JSONObject) obj;
            JSONArray array = (JSONArray) inventory.get("inventory");
            
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject item = iterator.next();
                out.println("Type: " + item.get("type"));
                out.println("Name: " + item.get("name"));
                out.println("Weight: " + item.get("weight"));
                out.println("Price per KG: " + item.get("pricePerKG"));
                out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
