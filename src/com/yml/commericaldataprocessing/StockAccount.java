package com.yml.commericaldataprocessing;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import com.yml.linkedlist.Node;
import com.yml.queue.Queue;
import com.yml.stack.Stack;
import com.yml.linkedlist.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.*;

/**
 * @author Sujay S Shenoy
 * This class is used process stock account of a user
 * allowing the user to buy or sell stocks
 * and print stock reports
 */
public class StockAccount implements StockProcessor {
    private final String STOCKS_FILE = "data/stocks.json";

    private String fileName;
    private JSONArray stocksData;
    LinkedList<CompanyShare> companyShares = new LinkedList<CompanyShare>();
    Stack<JSONObject> transactStack = new Stack<JSONObject>();
    Queue<String> dateTimeQueue = new Queue<String>();

    StockAccount(String fileName) {
        this.fileName = fileName;
    }

    /** 
     * @return List<CompanyShare>
     * getter method for companyShares list
     */
    public LinkedList<CompanyShare> getCompanyShares() {
        return companyShares;
    }
    
    /** 
     * @param companyShares
     * Setter method for companyShares list
     */
    public void setCompanyShares(LinkedList<CompanyShare> companyShares) {
        this.companyShares = companyShares;
    }

    /**
     * Method to restore all the data from a file
     * that has been written to storage by save() method
     */
    public void initializeAccountFromFile(){
        try {
            LinkedList<CompanyShare> companySharesList = new LinkedList<CompanyShare>();
            FileReader reader = new FileReader(fileName);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);
            JSONArray companyShares = (JSONArray) obj.get("companyShares");
            JSONArray transStack = (JSONArray) obj.get("transactionStack");
            JSONArray dateTimeQ = (JSONArray) obj.get("dateTimeQueue");
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

            itr = transStack.iterator();
            while (itr.hasNext()) {
                transactStack.push(itr.next());
            }

            Iterator<String> itr2 = dateTimeQ.iterator();
            while (itr2.hasNext()) {
                dateTimeQueue.enqueue(itr2.next());
            }

            System.out.println("Data restored from file");
        } catch (Exception e) {
            System.out.println("Data not restored from file");
            return;
        }
    }

    
    /** 
     * @return double
     * Method to calculate total value of the all the stocks combined in the portfolio
     */
    @Override
    public double valueof() {
        double value = 0;
        for (Node<CompanyShare> companyShare : companyShares) {
            value += valueof(companyShare.getData());
        }  
        return value;
    }

    
    /** 
     * @param amount
     * @param symbol
     * Method to buy a stock from stocks.json
     */
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
            for (Node<CompanyShare> companyShare : companyShares) {
                CompanyShare compShare = companyShare.getData();
                if (compShare.getStockSymbol().equals(symbol)) {
                    newCompanyShare = compShare;
                    companyShares.remove(compShare);
                    break;
                }
            }
            if (newCompanyShare == null) {
                newCompanyShare = new CompanyShare(symbol);
            }

            updateValue(symbol, amount, newCompanyShare ,Transaction.BUY);
        }
        
    }

    
    /** 
     * @param symbol
     * @param numberOfShares
     * @param companyShare
     * @param state
     * Helper method to create/update companyShare object in companyShares List
     * and also update stocks.json file
     * during buy/sell situations
     */
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
        JSONObject transact = new JSONObject();
        transact.put("symbol", symbol);
        transact.put("state", state);
        transactStack.push(transact);
        dateTimeQueue.enqueue(dateTime.toString());
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

    
    /** 
     * @param amount
     * @param symbol
     * Method to Sell a stock in the portfolio
     */
    @Override
    public void sell(int amount, String symbol) {
        readJSON();
        PrintWriter out = new PrintWriter(System.out);
        long numberOfShares = 0;

        for (Node<CompanyShare> companyShare : companyShares) {
            if (companyShare.getData().getStockSymbol().equals(symbol)) {
                numberOfShares = companyShare.getData().getNumberOfShares();
            }
        }

        if (numberOfShares == 0 || amount > numberOfShares) {
            out.println("Insufficient Shares available");
        }
        else {
            CompanyShare selectedShare = null;
            for (Node<CompanyShare> companyShare : companyShares) {
                if (companyShare.getData().getStockSymbol().equals(symbol)) {
                    selectedShare = companyShare.getData();
                    companyShares.remove(companyShare.getData());
                    break;
                }
            }

            if (selectedShare != null) {
                updateValue(symbol, amount, selectedShare, Transaction.SELL);
            }
        }
    }

    
    /** 
     * @param filename
     * Method to save the current status of portfolio into a file in given path
     */
    @Override
    public void save(String filename) {
        JSONArray compShares = new JSONArray();
        for (Node<CompanyShare> companyShare : companyShares) {
            String stockSymbol = companyShare.getData().getStockSymbol();
            long numberOfShares = companyShare.getData().getNumberOfShares();
            JSONArray transactions = new JSONArray();
            for (Transaction transaction : companyShare.getData().getTransactions()) {
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
       
        JSONArray transStack = new JSONArray();
        for (Node<JSONObject> transact : transactStack) {
            transStack.add(transact.getData());
        }

        JSONArray dateQueue = new JSONArray();
        for (Node<String> dateTime : dateTimeQueue) {
            dateQueue.add(dateTime.getData());
        }

        JSONObject finalJSON = new JSONObject();
        finalJSON.put("companyShares", compShares);
        finalJSON.put("transactionStack", transStack);
        finalJSON.put("dateTimeQueue", dateQueue);

       try {
           FileWriter writer = new FileWriter(filename);
           writer.write(finalJSON.toJSONString());
           writer.flush();
           writer.close();
       } catch (Exception e) {
           e.printStackTrace();
       }       
    }
    
    /**
     * Method to print the Current status of stocks in the portfolio
     */
    @Override
    public void printReport() {
        PrintWriter out = new PrintWriter(System.out, true);
        out.println("Stock Report");
        out.println("Holding Shares\n");
        for (Node<CompanyShare> companyShare : companyShares) {
            out.println("Share Symbol : " + companyShare.getData().getStockSymbol());
            out.println("Number of Shares Holding : " + companyShare.getData().getNumberOfShares());
            double valueEach = 0;
            if (companyShare.getData().getNumberOfShares() != 0) {
                valueEach = valueof(companyShare.getData()) / companyShare.getData().getNumberOfShares();
            }
            out.println("Value of each share : " + valueEach);
            out.println("Total Share Value : " + valueof(companyShare.getData()));
            out.println();
        }
        out.println("Total Value of portfolio: " + valueof());
    }
    
    
    /** 
     * @param companyShare
     * @return double
     * Method to calculate total value of a particular company stock held.
     */
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
    
    /**
     * Method to reads stocks.json file and sets the stocksData class variable used by other methods
     */
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
