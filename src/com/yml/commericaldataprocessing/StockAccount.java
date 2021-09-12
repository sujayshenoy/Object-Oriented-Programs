package com.yml.commericaldataprocessing;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StockAccount implements StockProcessor {
    private String fileName;
    private JSONArray stocksData;
    List<CompanyShare> companyShares = new ArrayList<CompanyShare>();

    StockAccount(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public double valueof() {
        return 0;
    }

    @Override
    public void buy(int amount, String symbol) {
        readJSON();
        Iterator<JSONObject> itr = stocksData.iterator();
        PrintWriter out = new PrintWriter(System.out);

        long numberOfShares = 0;
        while (itr.hasNext()) {
            JSONObject stock = itr.next();
            if (stock.get("stockSymbol").equals(symbol)) {
                numberOfShares = (long) stock.get("numberOfShares");
            }
        }

        if (amount > numberOfShares) {
            out.println("Insufficient Shares Available");
        }
        else {
            CompanyShare newCompanyShare = null;
            for (CompanyShare companyShare : companyShares) {
                if (companyShare.getStockSymbol().equals(symbol)) {
                    newCompanyShare = companyShare;
                    companyShares.remove(companyShare);
                    break;
                }
            }
            if (newCompanyShare == null) {
                newCompanyShare = new CompanyShare(symbol);
            }

            
            updateValue(symbol, amount, newCompanyShare ,Transaction.BUY);
        }
        
    }

    private void updateValue(String symbol, long numberOfShares, CompanyShare companyShare, String state) {
        //Add transaction to CompanyShare Object
        long prevShares = companyShare.getNumberOfShares();
        if (state == Transaction.BUY) {
            companyShare.setNumberOfShares(prevShares + numberOfShares);
        }
        else {
            companyShare.setNumberOfShares(prevShares - numberOfShares);
        }
        long millis = System.currentTimeMillis();
        Date dateTime = new Date(millis);
        Transaction transaction = new Transaction(dateTime, numberOfShares, state);
        companyShare.addTransaction(transaction);
        companyShares.add(companyShare);

        //Update stocks.json file
        Iterator<JSONObject> itr = stocksData.iterator();
        
        while (itr.hasNext()) {
            JSONObject stock = itr.next();
            if (stock.get("stockSymbol").equals(symbol)) {
                prevShares = (long)stock.get("numberOfShares");
                stock.remove("numberOfShares");
                if (state == Transaction.BUY) {
                    stock.put("numberOfShares", prevShares - numberOfShares);
                }
                else {
                    stock.put("numberOfShares", prevShares + numberOfShares);
                }
            }
        }

        try {
            FileWriter writer = new FileWriter("data/stocks.json");
            JSONObject result = new JSONObject();
            result.put("stocks", stocksData);
            writer.write(result.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void sell(int amount, String symbol) {
        PrintWriter out = new PrintWriter(System.out);
        long numberOfShares = 0;

        for (CompanyShare companyShare : companyShares) {
            if (companyShare.getStockSymbol().equals(symbol)) {
                numberOfShares = companyShare.getNumberOfShares();
            }
        }

        if (numberOfShares == 0 || amount > numberOfShares) {
            out.println("Insufficient Shares available");
        }
        else {
            for (CompanyShare companyShare : companyShares) {
                if (companyShare.getStockSymbol().equals(symbol)) {
                    updateValue(symbol, amount,companyShare, Transaction.SELL);
                }
            }
    
        }
    }

    @Override
    public void save(String filename) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printReport() {
        // TODO Auto-generated method stub
        
    }
    
    private void readJSON(){
        try{
            FileReader reader = new FileReader("data/stocks.json");
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);
            stocksData = (JSONArray) obj.get("stocks");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
