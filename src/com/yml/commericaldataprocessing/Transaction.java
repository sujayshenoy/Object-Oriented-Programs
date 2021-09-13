package com.yml.commericaldataprocessing;

import java.util.*;

/**
 * @author Sujay S Shenoy
 * This class is used to model the transactions that take place during buy/sell of the company stocks.
 */
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

    
    /** 
     * @return String
     * getter method for state
     */
    public String getState() {
        return state;
    }

    
    /** 
     * @param state
     * setter method for state
     */
    public void setState(String state) {
        this.state = state;
    }

    
    /** 
     * @return String
     * getter method for dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    
    /** 
     * @param dateTime
     * setter method for dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    
    /** 
     * @return long
     * getter method for numberOfShares
     */
    public long getNumberOfShares() {
        return numberOfShares;
    }

    
    /** 
     * @param numberOfShares
     * setter method for numberOfShares
     */
    public void setNumberOfShares(long numberOfShares) {
        this.numberOfShares = numberOfShares;
    }
    
    

}
