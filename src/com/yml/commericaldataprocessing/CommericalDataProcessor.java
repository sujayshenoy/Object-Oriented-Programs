package com.yml.commericaldataprocessing;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class CommericalDataProcessor {
    final static String STOCKS_FILE = "data/stocks.json";
    final static String ACCOUNT_FILE = "output/account.json";

    private static StockAccount account = new StockAccount(ACCOUNT_FILE);

    public static void run() {
        account.initializeAccountFromFile();

        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);

        out.println("Welcome to the Stock broker program");
        out.println("Select an Option to proceed");
        while (true) {
            out.println("Main Menu");
            out.println("1. Buy Stocks\n2. Sell Stocks\n3. Print Stock Report\n4. Exit");
            int choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1:
                    buyStocksMenu();
                    break;
                case 2:
                    sellStocksMenu();
                    break;
                case 3:
                    account.printReport();
                    break;
                case 4:
                    return;
                default:
                    out.println("Invalid Option");
                    break;
            }
        }
    }
    
    /**
     * Method used to provide Console UI to users for selling stocks
     */
    private static void sellStocksMenu() {
        PrintWriter out = new PrintWriter(System.out, true);
        Scanner in = new Scanner(System.in);
        
        out.println("Select the stock you want to Sell");
        int count = 1;
        for (CompanyShare companyShare : account.getCompanyShares()) {
            out.println(count + ":");
            out.println("Stock Symbol : " + companyShare.getStockSymbol());
            out.println("Number Of Shares : " + companyShare.getNumberOfShares());
            out.println();
            count++;
        }

        int stockChoice = in.nextInt();
        while (stockChoice >= count) {
            out.println("Invalid option");
            stockChoice = in.nextInt();
        }

        out.println("Enter the amount to sell");
        int amount = in.nextInt();
        CompanyShare selectedStock = account.getCompanyShares().get(stockChoice - 1);
        while (amount > (long) selectedStock.getNumberOfShares() || amount<=0)
        {
            out.println("Enter a valid amount");
            amount = in.nextInt();
        }

        account.sell(amount, selectedStock.getStockSymbol());
        account.save(ACCOUNT_FILE);
    }

    /**
     * Method used to provide Console UI to users for buying stocks
     */
    private static void buyStocksMenu() {
        PrintWriter out = new PrintWriter(System.out, true);
        Scanner in = new Scanner(System.in);

        out.println("Select the stock you want to buy");
        JSONArray stocks = readJSON();
        Iterator<JSONObject> itr = stocks.iterator();
        int count = 1;
        while (itr.hasNext()) {
            out.println(count + ":");
            JSONObject stock = itr.next();
            out.println("Stock Name: " + stock.get("stockName"));
            out.println("Stock Symbol: " + stock.get("stockSymbol"));
            out.println("Share price: " + stock.get("sharePrice"));
            out.println("Number Of Shares: " + stock.get("numberOfShares"));
            out.println();
            count++;
        }
        
        int stockChoice = in.nextInt();
        while (stockChoice >= count) {
            out.println("Invalid option");
            stockChoice = in.nextInt();
        }

        out.println("Enter the amount to buy");
        int amount = in.nextInt();
        JSONObject selectedStock = (JSONObject) stocks.get(stockChoice - 1);
        while (amount > (long) selectedStock.get("numberOfShares") || amount<=0)
        {
            out.println("Enter a valid amount");
            amount = in.nextInt();
        }

        account.buy(amount, (String) selectedStock.get("stockSymbol"));
        account.save(ACCOUNT_FILE);
    }
    
    
    /** 
     * @return JSONArray
     * Method used to read JSON from stocks.json file
     * and return the JSONArray
     */
    private static JSONArray readJSON() {
        try {
            FileReader reader = new FileReader(STOCKS_FILE);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);
            JSONArray stocks = (JSONArray) obj.get("stocks");
            return stocks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
