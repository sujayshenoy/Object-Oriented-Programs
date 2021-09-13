package com.yml.commericaldataprocessing;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StockAccount implements StockProcessor {
    private final String STOCKS_FILE = "data/stocks.json";

    private String fileName;
    public List<CompanyShare> getCompanyShares() {
        return companyShares;
    }

    public void setCompanyShares(List<CompanyShare> companyShares) {
        this.companyShares = companyShares;
    }

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
            if (companyShares == null) {
                return;
            }
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
            System.out.println("Data restored from file");
        } catch (Exception e) {
            System.out.println("Data not restored from file");
            return;
        }
    }

    @Override
    public double valueof() {
        double value = 0;
        for (CompanyShare companyShare : companyShares) {
            value += valueof(companyShare);
        }  
        return value;
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
        readJSON();
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
            FileWriter writer = new FileWriter(STOCKS_FILE);
            JSONObject result = new JSONObject();
            result.put("stocks", stocksData);
            writer.write(result.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (state == Transaction.BUY) {
            System.out.println("Buy Successfull");
        }
        else {
            System.out.println("Sell Successfull");
        }
        
    }

    @Override
    public void sell(int amount, String symbol) {
        readJSON();
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
        PrintWriter out = new PrintWriter(System.out, true);
        out.println("Stock Report");
        out.println("Holding Shares\n");
        for (CompanyShare companyShare : companyShares) {
            out.println("Share Symbol : " + companyShare.getStockSymbol());
            out.println("Number of Shares Holding : " + companyShare.getNumberOfShares());
            double valueEach = 0;
            if (companyShare.getNumberOfShares() != 0) {
                valueEach = valueof(companyShare) / companyShare.getNumberOfShares();
            }
            out.println("Value of each share : " + valueEach);
            out.println("Total Share Value : " + valueof(companyShare));
            out.println();
        }
        out.println("Total Value of portfolio: " + valueof());
    }
    
    public double valueof(CompanyShare companyShare) {
        readJSON();
        Iterator<JSONObject> itr = stocksData.iterator();
        double sharePrice = 0.0;
        while (itr.hasNext()) {
            JSONObject stock = itr.next();
            if (stock.get("stockSymbol").equals(companyShare.getStockSymbol())) {
                sharePrice = (double) stock.get("sharePrice");
            }
        }
        
        return sharePrice * companyShare.getNumberOfShares();
    }

    private void readJSON(){
        try{
            FileReader reader = new FileReader(STOCKS_FILE);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);
            stocksData = (JSONArray) obj.get("stocks");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
