package com.example.walletappvis.Backend;
public class Belongings {

    public String type;
    public String code;
    public double amount;

    public Belongings(String type, String code, double amount) {
        this.type = type;
        this.code = code;
        this.amount = amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
