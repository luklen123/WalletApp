package com.example.walletappvis.Backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.IllformedLocaleException;
import java.util.NoSuchElementException;

public class CurrencyTaker {

    private Data[] actual_data = null;
    private Date date;

    public CurrencyTaker() {
        actual_data = getActual_data();
    }

    public Data[] get_data(){
        if(actual_data == null) getActual_data();
        return actual_data;
    }

    public Data[] getActual_data() {
        final String url = "https://www.bankier.pl/waluty/kursy-walut/nbp";
        try {
            final Document document = Jsoup.connect(url).get();
            // wypisanie całej strony HTML
            //System.out.println(document.outerHtml());
            int licznik;
            ArrayList<String> cur_code = new ArrayList<>();
            ArrayList<Integer> cur_volume = new ArrayList<>();
            ArrayList<Double> cur_rate = new ArrayList<>();
            for (Element row : document.select("table.sortTableMixedData.floatingHeaderTable tr")) {
                licznik = 1;
                for (Element cell : row.select("td")) {
                    if (licznik == 3) {
                        cur_code.add(Extract_code(cell.text()));
                        cur_volume.add(Extract_volume(cell.text()));
                    } else if (licznik == 4) {
                        String rate = cell.text();
                        rate = rate.replace(',', '.');
                        cur_rate.add(Double.parseDouble(rate));
                    }
                    licznik++;
                }
            }
            Data[] data = new Data[cur_code.size()+1];
            for(int i=0; i<cur_code.size(); i++) {
                data[i] = new Data("currency", cur_code.get(i), cur_volume.get(i), cur_rate.get(i));
                //System.out.println(cur_code.get(i)+" ["+cur_volume.get(i)+"] "+cur_rate.get(i));
            }
            data[cur_code.size()] = new Data("currency", "PLN", 1, 1);
            actual_data = data;
            date = new Date();
            return actual_data;
        } catch (Exception e) {
            throw new IllformedLocaleException();
        }
    }

    private String Extract_code(String s){
        String sub = "";
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i)>='A' && s.charAt(i)<='Z'){
                sub = sub + s.charAt(i);
            }
        }
        return sub;
    }

    private int Extract_volume(String s){
        String sub = "";
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i)>='0' && s.charAt(i)<='9'){
                sub = sub + s.charAt(i);
            }
        }
        return Integer.parseInt(sub);
    }

    public double getRate(String s){
        getActual_data();
        if(actual_data != null){
            for(Data d: actual_data){
                if(d.code.equals(s)){
                    return d.value;
                }
            }
        }
        throw new NoSuchElementException();
    }

    public double getVolume(String s){
        getActual_data();
        if(actual_data != null){
            for(Data d: actual_data){
                if(d.code.equals(s)){
                    return d.volume;
                }
            }
        }
        throw new NoSuchElementException();
    }


    public Date getDate_data(){
        return date;
    }

    public void Show_data(){
        System.out.println("ZAPISANE KURSY WALUT:");
        if(actual_data == null) getActual_data();
        for(Data d: actual_data){
            System.out.println(d);
        }
    }
}