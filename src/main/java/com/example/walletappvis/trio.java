package com.example.walletappvis;

import javafx.beans.property.SimpleStringProperty;

public class trio {
    SimpleStringProperty a;
    double b;
    double c;

    public trio(String a, double b, double c) {
        this.a = new SimpleStringProperty(a);
        this.b = b;
        this.c = c;
    }
}
