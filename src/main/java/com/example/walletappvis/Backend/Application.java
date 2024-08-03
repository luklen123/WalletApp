package com.example.walletappvis.Backend;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Date;

public class Application {
    private CurrencyTaker currencyTaker;
    private Backend.MetalTaker metalTaker;
    private ShareTaker shareTaker;

    public Application(){
        currencyTaker = new CurrencyTaker();
        metalTaker = new Backend.MetalTaker();
        shareTaker = new ShareTaker();
    }

    public void Change_assets(Client c, String type, String code, double amount){
        int pom = c.change_amount_of_belongings(type, code, amount);
        if(pom == -1) System.out.println("Error");
    }
    public ArrayList<Belongings> getAll_belongings(Client c){
        return c.getWallet();
    }

    public Data[] getCurrency(){
        return currencyTaker.get_data();
    }

    public Data[] getActualCurrency(){
        return currencyTaker.getActual_data();
    }

    public Date getCurrency_date(){
        return currencyTaker.getDate_data();
    }

    public String[] getCurrencyCodes(){
        Data[] currency = getCurrency();
        String[] codes = new String[currency.length];
        int licznik = 0;
        for(Data d : currency){
            codes[licznik] = d.code;
            licznik++;
        }
        return codes;
    }

    public double getCurrency_rate(String code){
        return currencyTaker.getRate(code);
    }

    public double getCurrency_volume(String code){
        return currencyTaker.getVolume(code);
    }


    public void showCurrency(){
        currencyTaker.Show_data();
    }

    public Data[] getMetal(){
        return metalTaker.get_data();
    }

    public Data[] getActualMetal(){
        return metalTaker.getActual_data();
    }

    public String[] getMetalCodes(){
        Data[] metal = getMetal();
        String[] codes = new String[metal.length];
        int licznik = 0;
        for(Data d : metal){
            codes[licznik] = d.type;
            licznik++;
        }
        return codes;
    }

    public double getMetal_rate(String code){
        return metalTaker.getRate(code);
    }

    public Data[] getShare(){
        return shareTaker.get_data();
    }

    public Data[] getActualShare(){
        return shareTaker.getActual_data();
    }


    public String[] getShareCodes(){
        Data[] share = getShare();
        String[] codes = new String[share.length];
        int licznik = 0;
        for(Data d : share){
            codes[licznik] = d.code;
            licznik++;
        }
        return codes;
    }

    public double getShare_rate(String code){
        return shareTaker.getRate(code);
    }

    public double getTotalWorth(Client c){
        double TotalWorth = 0;
        ArrayList<Belongings> belongings = c.getWallet();
        for(Belongings b : belongings){
            if(b.type == "currency"){
                TotalWorth += (currencyTaker.getRate(b.code)*b.amount)/getCurrency_volume(b.code);
            } else if(b.type == "metals"){
                TotalWorth += (metalTaker.getRate(b.code)*b.amount*getCurrency_rate("USD"));
            } else if(b.type == "shares"){
                TotalWorth += (shareTaker.getRate(b.code)*b.amount);
            }
        }
        return TotalWorth;
    }



}
