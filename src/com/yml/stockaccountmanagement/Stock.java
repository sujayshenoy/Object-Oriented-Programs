package com.yml.stockaccountmanagement;

public class Stock {
    private String stockName;
    private String stockSymbol;
    private long numberOfShares;
    private double sharePrice;

    Stock() {

    }
    
    Stock(String stockName, String stockSymbol, long numberOfShares, double sharePrice) {
        this.stockName = stockName;
        this.stockSymbol = stockSymbol;
        this.numberOfShares = numberOfShares;
        this.sharePrice = sharePrice;
    }

    public String getStockName() {
        return stockName;
    }

    @Override
    public String toString() {
        return "Stock [numberOfShares=" + numberOfShares + ", sharePrice=" + sharePrice + ", stockName=" + stockName
                + ", stockSymbol=" + stockSymbol + "]";
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
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

    public double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    
}
