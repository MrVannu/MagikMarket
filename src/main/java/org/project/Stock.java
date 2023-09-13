package org.project;

import javafx.css.StyleableStringProperty;

public class Stock {
    private double price;
    private double regularMarketDayHigh;
    private double regularMarketDayLow;
    private double markerPreviousClose;
    private String symbol;
    private String name;
    private String description;
    private boolean available;
    private boolean investedOn = false;

    public Stock(String symbol, String name){
        this.symbol= symbol;
        this.name = name;
    }
    public double getSymbol() {
        return price;
    }
    public void setSymbol(String sym) {
        this.symbol = symbol;
    }
    public double getMarkerPreviousClose() {
        return markerPreviousClose;
    }
    public void setMarkerPreviousClose(double markerPreviousClose) {
        this.markerPreviousClose = markerPreviousClose;
    }
    public String getName() {
        return name;
    }
    public void setName(String id) {
        this.name = id;
    }
    public double getRegularMarketDayLow() {
        return regularMarketDayLow;
    }
    public void setRegularMarketDayLow(double description) {
        this.regularMarketDayLow = regularMarketDayLow;
    }
    public boolean isAvailable() {
        return available;
    }
    public double getRegularMarketDayHigh(){return regularMarketDayHigh;}

    public void setRegularMarketDayHigh(double regularMarketDayHigh) {
        this.regularMarketDayHigh = regularMarketDayHigh;
    }

    /*private double discount(){
            return 0.0;
        }*/
    public boolean equals(Stock obj) {
        return this.getName().equals(obj.getName());
    }
}
