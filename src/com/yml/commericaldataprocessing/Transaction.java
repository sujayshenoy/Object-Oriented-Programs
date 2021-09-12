package com.yml.commericaldataprocessing;

import java.util.*;

public class Transaction {
    final static String BUY = "buy";
    final static String SELL = "sell";

    private Date dateTime;
    private long numberOfShares;
    private String state;

    Transaction() {
        
    }

    Transaction(Date dateTime, long numberOfShares, String state) {
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public long getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(long numberOfShares) {
        this.numberOfShares = numberOfShares;
    }
    
    

}
