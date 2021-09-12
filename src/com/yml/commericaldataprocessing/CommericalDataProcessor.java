package com.yml.commericaldataprocessing;

public class CommericalDataProcessor {
    public static void run() {
        StockAccount account = new StockAccount("output/account.json");

        account.buy(20, "$I");
        System.out.println(account.companyShares.get(0));
        account.buy(20, "$I");
        System.out.println(account.companyShares.get(0));
        account.sell(20, "$I");
        System.out.println(account.companyShares.get(0));
    }
}
