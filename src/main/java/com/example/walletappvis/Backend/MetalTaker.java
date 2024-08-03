package Backend;

import com.example.walletappvis.Backend.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.IllformedLocaleException;
import java.util.NoSuchElementException;

public class MetalTaker {

    private Data[] actual_data;
    Date date;

    public Data[] get_data(){
        return actual_data;
    }

    public Data[] getActual_data(){
        final String url = "https://www.money.pl/gielda/surowce/";
        try{
            final Document document = Jsoup.connect(url).get();
            //System.out.println(document.outerHtml());

            int licznik;
            ArrayList<String> met_code = new ArrayList<>();
            ArrayList<String> met_volume = new ArrayList<>();
            ArrayList<Double> met_rate = new ArrayList<>();
            for(Element row: document.select("div.rt-tr")){
                licznik = 1;
                for(Element cell: row.select("div.rt-td")){
                    if(licznik == 1){
                        met_code.add(cell.text());
                    } else if(licznik == 2){
                        if(cell.text().equals("tona")){
                            met_volume.add("ton");
                        } else if(cell.text().equals("uncja")){
                            met_volume.add("ounce");
                        } else if(cell.text().equals("baryłka")){
                            met_volume.add("barrel");
                        } else {
                            met_volume.add(cell.text());
                        }
                    } else if(licznik == 3){
                        String pom = cell.text().replace(" ", "").replace(",", ".");
                        met_rate.add(Double.parseDouble(pom));
                    }
                    licznik++;
                }
            }
            actual_data = new Data[met_code.size()];
            date = new Date();
            for(int i = 0; i < met_code.size(); i++){
                actual_data[i] = new Data(met_code.get(i), met_volume.get(i),1, met_rate.get(i));
            }
            return actual_data;
        } catch(Exception e){
            System.out.println("ERROR");
        }
        return null;
    }

    public double getRate(String name){
        for(Data d: actual_data){
            if(d.type == name) {
                return d.value;
            }
        }
        return -1;
    }
}
