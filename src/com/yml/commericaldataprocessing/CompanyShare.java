package com.yml.commericaldataprocessing;

import java.util.*;

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

    @Override
    public String toString() {
        return "stockSymbol=" + stockSymbol + ", Shares=" + numberOfShares;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public long getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(long numberOfShares) {
        this.numberOfShares = numberOfShares;
    }
    
}
