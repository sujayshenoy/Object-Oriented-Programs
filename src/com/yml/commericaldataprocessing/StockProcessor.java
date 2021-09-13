package com.yml.commericaldataprocessing;

public interface StockProcessor {
    double valueof();

    void buy(int amount, String symbol);

    void sell(int amount, String symbol);

    void save(String filename);

    void printReport();
}
