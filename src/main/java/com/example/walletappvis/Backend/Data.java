package com.example.walletappvis.Backend;

public class Data {

    public String type;
    public String code;
    public int volume;
    public double value;

    public Data(String type, String code, int volume, double value) {
        this.type = type;
        this.code = code;
        this.volume = volume;
        this.value = value;
    }

    public String toString(){
        return type + " " + code + " " + value + " [" + volume + "]";
    }
}
