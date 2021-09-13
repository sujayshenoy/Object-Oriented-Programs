package com.yml.commericaldataprocessing;

import java.util.*;

/**
 * @author Sujay S Shenoy
 * This class holds Stock and transaction information of a particular company
 */
public class CompanyShare {
    private String stockSymbol;
    private long numberOfShares;

    private List<Transaction> transactions = new ArrayList<Transaction>();

    CompanyShare() {

    }
    
    CompanyShare(String stockSymbol) {
        this.stockSymbol = stockSymbol;
        this.numberOfShares = 0;
    }

    
    /** 
     * @return String
     * To print the CompanyShare object
     */
    @Override
    public String toString() {
        return "stockSymbol=" + stockSymbol + ", Shares=" + numberOfShares;
    }

    
    /** 
     * @param transaction
     * add a new Buy/sell transaction of a company
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    
    /** 
     * @return List<Transaction>
     * get buy/sell stock transactions of a company 
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    
    /** 
     * @param transactions
     * set buy/sell stock transactions of a company 
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    
    /** 
     * @return String
     * return stock symbol of a company
     */
    public String getStockSymbol() {
        return stockSymbol;
    }

    
    /** 
     * @param stockSymbol
     * set Stock symbol of a company
     */
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    
    /** 
     * @return long
     * Return number of shares held of a particular company
     */
    public long getNumberOfShares() {
        return numberOfShares;
    }

    
    /** 
     * @param numberOfShares
     * set number of Shares held of a particular company
     */
    public void setNumberOfShares(long numberOfShares) {
        this.numberOfShares = numberOfShares;
    }
    
}
