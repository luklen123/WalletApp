package com.example.walletappvis.Backend;

import com.example.walletappvis.Backend.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

public class ShareTaker {

    Data[] actual_data = null;
    Date date;

    public Data[] get_data(){
        if(actual_data == null) return getActual_data();
        return actual_data;
    }

    public Data[] getActual_data(){
        final String url = "https://www.bankier.pl/gielda/notowania/akcje";
        try{
            final Document file = Jsoup.connect(url).get();
            //System.out.println(file.outerHtml());
            int licznik;
            ArrayList<String> share_code = new ArrayList<>();
            ArrayList<Double> share_value = new ArrayList<>();
            String pom;
            for(Element row: file.select("table.sortTable.floatingHeaderTable tr")){
                //System.out.println(row.text());
                licznik = 1;
                for(Element cell: row.select("td")){
                    if(licznik == 1){
                        if(cell.text().equals("")) break;
                        share_code.add(cell.text());
                    } else if(licznik == 2){
                        pom = cell.text().replace(" ", "").replace(",",".");
                        share_value.add(Double.parseDouble(pom));
                    }
                    licznik++;
                }
            }
            actual_data = new Data[share_code.size()];
            for(int i = 0; i < share_code.size(); i++){
                actual_data[i] = new Data("share", share_code.get(i),1, share_value.get(i));
            }
            date = new Date();
            return actual_data;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
}
