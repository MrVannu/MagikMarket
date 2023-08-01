package org.project;

import javafx.css.StyleableStringProperty;

public class Stock {
    private double price;
    private double regularMarketDayHigh;
    private double regularMarketDayLow;
    private double markerPreviousClose;
    private String symbol;
    private String id;
    private String description;
    private boolean available;

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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
        return this.getId().equals(obj.getId());
    }
}
