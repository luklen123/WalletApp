package com.example.walletappvis.Backend;

import java.util.ArrayList;

public class Client {

    private String pesel;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String dateOfBirth;
    private ArrayList<Belongings> wallet;

    public Client(String pesel, String name, String surname, String email, String phone, String dateOfBirth) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        wallet = new ArrayList<>();
    }

    public void loadData(){

    }

    public int change_amount_of_belongings(String type, String code, double amount) {
        for(Belongings b : wallet){
            if(type.equals(b.type) && code.equals(b.code)){
                if(b.amount+amount < 0) return -1;
                b.setAmount(b.amount+amount);
                if(b.amount == 0){
                    wallet.remove(b);
                }
                return 1;
            }
        }
        wallet.add(new Belongings(type, code, amount));
        /*System.out.println("-----------------------------------------------------");
        System.out.println("ERROR: No Belongings found or incorrect currency code");
        System.out.println("-----------------------------------------------------");*/
        return 1;
    }

    public ArrayList<Belongings> getWallet() {
        return wallet;
    }
}
