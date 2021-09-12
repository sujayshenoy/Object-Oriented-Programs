package com.yml.commericaldataprocessing;

import java.util.*;

public class Transaction {
    final static String BUY = "buy";
    final static String SELL = "sell";

    private String dateTime;
    private long numberOfShares;
    private String state;

    Transaction() {
        
    }

    Transaction(String dateTime, long numberOfShares, String state) {
        this.dateTime = dateTime;
        this.numberOfShares = numberOfShares;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(long numberOfShares) {
        this.numberOfShares = numberOfShares;
    }
    
    

}
