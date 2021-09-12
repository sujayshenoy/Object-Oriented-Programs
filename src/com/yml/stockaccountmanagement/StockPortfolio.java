package com.yml.stockaccountmanagement;

import java.util.*;

public class StockPortfolio {
    private List<Stock> stocks = new ArrayList<Stock>();

    StockPortfolio() {

    }
    
    StockPortfolio(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }
    
    public List<Stock> getStocks() {
        return stocks;
    }

    public double valueOf(Stock stock) {
        return (double) (stock.getSharePrice() * stock.getNumberOfShares());
    }
    
    public double valueOfPortfolio() {
        double value = 0;
        for (Stock stock : stocks) {
            value += valueOf(stock);
        }

        return value;
    }
    
}
