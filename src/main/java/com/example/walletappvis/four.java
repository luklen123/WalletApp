package com.example.walletappvis;

import javafx.beans.property.SimpleStringProperty;

public class four {
    SimpleStringProperty a;
    SimpleStringProperty b;
    SimpleStringProperty c;
    int d;
    double e;
    double f;

    public four(String a, String b, String c, int d, double e, double f) {
        this.a = new SimpleStringProperty(a);
        this.b = new SimpleStringProperty(b);
        this.c = new SimpleStringProperty(c);
        this.d = d;
        this.e = e;
        this.f = f;
    }
    public String getA() {
        return a.get();
    }
    public String getB() {
        return b.get();
    }
    public String getC() {
        return c.get();
    }
    public int getD() {
        return d;
    }
    public double getE() {
        return e;
    }
    public double getF() {
        return f;
    }
}
