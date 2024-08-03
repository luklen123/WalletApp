package com.example.walletappvis;

import com.example.walletappvis.Backend.Application;
import com.example.walletappvis.Backend.Belongings;
import com.example.walletappvis.Backend.Client;
import com.example.walletappvis.Backend.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable{

    Application application = new Application();
    Client c1 = new Client("123","LUKASZ","LENART","lenartluk@gmail.com","665966278","01_07_2004");

// WALLET WINDOW
    @FXML
    TableView<four> dataTable;
    @FXML
    TableColumn<four, String> colType;
    @FXML
    TableColumn<four, String> colCode;
    @FXML
    TableColumn<four, Integer> colVolume;
    @FXML
    TableColumn<four, Double> colPrice;
    @FXML
    TableColumn<four, Double> colWorth;
    @FXML
    public Text Data_text;
    @FXML
    public Text Data_text1;
    @FXML
    public Text Data_text2;
    @FXML
    public Text Data_text3;
    @FXML
    public Text worth_text;


    double worth = 0;
    Date data_date;

    public void refresh(){
        //pobierz danie ze strony
        //oblicz aktualna wartosc
        worth = setPrecision(application.getTotalWorth(c1));
        worth_text.setText(String.format("%.2f",worth)+" zł");
        data_date = new Date();
        Data_text.setText(data_date+"");
        Data_text1.setText(data_date+"");
        Data_text2.setText(data_date+"");
        Data_text3.setText(data_date+"");
    }

/// EDIT WINDOW

    @FXML
    ComboBox<String> choiceOperation;
    @FXML
    ComboBox<String> choiceType;
    @FXML
    ComboBox<String> choiceCode;
    @FXML
    TextField input;
    @FXML
    Label informationLog;

    String[] operations = {"BUY", "SELL"};
    String[] types = {"currency", "metals","shares"};
    String[] codes1 = {"gold", "silver", "bronze"};
    String choosenOperation;
    String choosenType;
    int amount;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colType.setCellValueFactory(new PropertyValueFactory<>("a"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("b"));
        colVolume.setCellValueFactory(new PropertyValueFactory<>("d"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("e"));
        colWorth.setCellValueFactory(new PropertyValueFactory<>("f"));

        Curtabcode.setCellValueFactory(new PropertyValueFactory<>("a"));
        Curtabvolume.setCellValueFactory(new PropertyValueFactory<>("d"));
        Curtabprice.setCellValueFactory(new PropertyValueFactory<>("e"));
        Data[] data = application.getActualCurrency();
        for(Data d : data){
            CurrencyTable.getItems().add(new four(d.code, d.code, d.code, d.volume, d.value, d.value));
        }

        Shatabcode.setCellValueFactory(new PropertyValueFactory<>("a"));
        Shatabprice.setCellValueFactory(new PropertyValueFactory<>("f"));
        data = application.getActualShare();
        for(Data d : data){
            ShareTable.getItems().add(new four(d.code, d.code, d.code, d.volume, d.value, setPrecision(d.value)));
        }

        Mettabname.setCellValueFactory(new PropertyValueFactory<>("a"));
        Mettabvolume.setCellValueFactory(new PropertyValueFactory<>("b"));
        Mettabprice.setCellValueFactory(new PropertyValueFactory<>("f"));
        data = application.getActualMetal();
        double USDrate = application.getCurrency_rate("USD");
        for(Data d : data){
            Metaltable.getItems().add(new four(d.type, d.code, d.code, d.volume, d.value, setPrecision(d.value*USDrate)));
        }


        informationLog.setVisible(false);
        choiceOperation.getItems().addAll(operations);
        choiceType.setDisable(true);
        choiceCode.setDisable(true);
        choiceOperation.setOnAction(this::getOperation);
        refresh();
    }

    public void getOperation(ActionEvent event){
        informationLog.setVisible(false);
        choosenOperation = choiceOperation.getValue();
        choiceType.setDisable(false);
        if(choiceType.getItems().isEmpty()) choiceType.getItems().addAll(types);
        choiceType.setOnAction(this::getType);
    }

    public void getType(ActionEvent event){
        informationLog.setVisible(false);
        choosenType = choiceType.getValue();
        choiceCode.setDisable(false);
        AddingCodeChoice();
    }
    public void Submit(ActionEvent event){
        try{
            amount = Integer.parseInt(input.getText());
        } catch (Exception e){
            informationLog.setVisible(true);
            informationLog.setTextFill(Color.RED);
            informationLog.setText("ERROR: Insert only positive number bigger than 0");
            return;
        }
        if(amount <= 0){
            informationLog.setVisible(true);
            informationLog.setTextFill(Color.RED);
            informationLog.setText("ERROR: Insert only positive number bigger than 0");
            return;
        }
        if(choiceOperation.getValue() == null || choiceType.getValue() == null || choiceCode.getValue() == null || input.getText().isEmpty()){
            informationLog.setVisible(true);
            informationLog.setText("ERROR: Not every data inserted");
        } else {
            System.out.println("CHOOSEN : "+choiceOperation.getValue()+" "+choiceType.getValue()+" "+choiceCode.getValue()+" "+input.getText());
            informationLog.setVisible(true);
            informationLog.setTextFill(Color.GREEN);
            informationLog.setText("SUBMITTED SUCCESFULLY!");
            // zakladam ze sa tylko waluty
            if(choiceOperation.getValue() == "SELL") amount *= (-1);
            application.Change_assets(c1, choiceType.getValue(), choiceCode.getValue(), amount);
            //dataTable.getItems().add(new four(choiceType.getValue(),choiceCode.getValue(), choiceCode.getValue(),amount, application.getCurrency_rate(choiceCode.getValue()), setPrecision((amount*application.getCurrency_rate(choiceCode.getValue()))/application.getCurrency_volume(choiceCode.getValue()))));
            AddingAssetstoMainTable();
            worth = setPrecision(application.getTotalWorth(c1));
            worth_text.setText(String.format("%.2f",worth)+" zł");
        }


    }

    public void AddingAssetstoMainTable(){
        dataTable.getItems().clear();
        ArrayList<Belongings> act = application.getAll_belongings(c1);
        for(Belongings b: act){
            if(b.type == "currency"){
                dataTable.getItems().add(new four(b.type, b.code, b.code, (int)b.amount, application.getCurrency_rate(b.code), setPrecision(b.amount*application.getCurrency_rate(b.code))));
            } else if(b.type == "shares"){
                //dataTable.getItems().add(new four());
            } else if(b.type == "metals"){
                dataTable.getItems().add(new four(b.type, b.code, b.code, (int)b.amount, application.getMetal_rate(b.code)*application.getCurrency_rate("USD"),setPrecision(application.getCurrency_rate("USD")*(b.amount*application.getMetal_rate(b.code)))));
            }
        }
    }

    public void AddingCodeChoice(){
        if(choosenType.equals("currency")){
            choiceCode.getItems().clear();
            choiceCode.getItems().addAll(application.getCurrencyCodes());
        } else if(choosenType.equals("metals")){
            choiceCode.getItems().clear();
            choiceCode.getItems().addAll(application.getMetalCodes());
        } else{
            choiceCode.getItems().clear();
            choiceCode.getItems().addAll(application.getShareCodes());
        }
    }


    // CURRENCY
    @FXML
    TableView<four> CurrencyTable;
    @FXML
    TableColumn<four, String> Curtabcode;
    @FXML
    TableColumn<four, Integer> Curtabvolume;
    @FXML
    TableColumn<four, Double> Curtabprice;

    public double setPrecision(double value){
        int pom = (int) (value*100);
        double pom1 = pom;
        pom1/=100;
        return pom1;
    }

    // SHARES
    @FXML
    TableView<four> ShareTable;
    @FXML
    TableColumn<four, String> Shatabcode;
    @FXML
    TableColumn<four, Double> Shatabprice;

    // METALS

    @FXML
    TableView<four> Metaltable;
    @FXML
    TableColumn<four, String> Mettabname;
    @FXML
    TableColumn<four, String> Mettabvolume;
    @FXML
    TableColumn<four, Double> Mettabprice;

}