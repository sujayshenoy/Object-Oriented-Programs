package com.yml.commericaldataprocessing;

public class CommericalDataProcessor {
    public static void run() {
        StockAccount account = new StockAccount("output/account.json");
        account.initializeAccountFromFile();

        account.buy(200, "$I");
        account.sell(100, "$I");
        // System.out.println(account.companyShares.get(0));
        account.buy(2, "$R");
        account.sell(1, "$R");
        // account.buy(30, "$I");
        // account.buy(10, "$I");
        // account.buy(5, "$I");
        // System.out.println(account.companyShares.get(0));
        account.buy(20, "$T");
        // System.out.println(account.companyShares.get(0));
        account.save("output/account.json");
    }
}
