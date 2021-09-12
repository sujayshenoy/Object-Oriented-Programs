package com.yml.commericaldataprocessing;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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

    public void initializeAccountFromFile(){
        try {
            List<CompanyShare> companySharesList = new ArrayList<CompanyShare>();
            FileReader reader = new FileReader(fileName);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);
            JSONArray companyShares = (JSONArray) obj.get("companyShares");
            Iterator<JSONObject> itr = companyShares.iterator();
            while (itr.hasNext()) {
                CompanyShare companyShare = new CompanyShare();
                JSONObject compShare = itr.next();
                companyShare.setStockSymbol(compShare.get("stockSymbol").toString());
                companyShare.setNumberOfShares((long) compShare.get("numberOfShares"));

                JSONArray transactions = (JSONArray) compShare.get("transactions");
                Iterator<JSONObject> itr2 = transactions.iterator();

                List<Transaction> transactionList = new ArrayList<Transaction>();
                while (itr2.hasNext()) {
                    Transaction transact = new Transaction();
                    JSONObject transaction = itr2.next();
                    transact.setDateTime(transaction.get("DateTime").toString());
                    transact.setNumberOfShares((long) transaction.get("numberOfShares"));
                    transact.setState((String) transaction.get("State"));
                    transactionList.add(transact);
                }

                companyShare.setTransactions(transactionList);
                companySharesList.add(companyShare);
            }
            this.companyShares = companySharesList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public double valueof() {
        return 0;
    }

    @Override
    public void buy(int amount, String symbol) {
        readJSON();
        Iterator<JSONObject> itr = stocksData.iterator();
        PrintWriter out = new PrintWriter(System.out,true);

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
        Transaction transaction = new Transaction(dateTime.toString(), numberOfShares, state);
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
            CompanyShare selectedShare = null;
            for (CompanyShare companyShare : companyShares) {
                if (companyShare.getStockSymbol().equals(symbol)) {
                    selectedShare = companyShare;
                    companyShares.remove(companyShare);
                    break;
                }
            }

            if (selectedShare != null) {
                updateValue(symbol, amount, selectedShare, Transaction.SELL);
            }
        }
    }

    @Override
    public void save(String filename) {
        JSONArray compShares = new JSONArray();
        for (CompanyShare companyShare : companyShares) {
            String stockSymbol = companyShare.getStockSymbol();
            long numberOfShares = companyShare.getNumberOfShares();
            JSONArray transactions = new JSONArray();
            for (Transaction transaction : companyShare.getTransactions()) {
                JSONObject transactionObject = new JSONObject();
                transactionObject.put("DateTime", transaction.getDateTime().toString());
                transactionObject.put("numberOfShares", transaction.getNumberOfShares());
                transactionObject.put("State", transaction.getState());
                transactions.add(transactionObject);
            }
            JSONObject obj = new JSONObject();
            obj.put("stockSymbol", stockSymbol);
            obj.put("numberOfShares", numberOfShares);
            obj.put("transactions", transactions);
            compShares.add(obj);
       }

        JSONObject finalJSON = new JSONObject();
        finalJSON.put("companyShares", compShares);

       try {
           FileWriter writer = new FileWriter(filename);
           writer.write(finalJSON.toJSONString());
           writer.flush();
           writer.close();
       } catch (Exception e) {
           e.printStackTrace();
       }       
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
