package org.project;

import javafx.css.StyleableStringProperty;

public class Stock {
    //private double price;
    private double regularMarketDayHigh;
    private double regularMarketDayLow;
    private double regularMarketOpen;
    private double marketPreviousClose;
    private String symbol;
    private String name;
    private String description;
    private boolean available;
    private double amountBetted = 0.0;
    private boolean investedOn = false;

    public Stock(String symbol, String name, double regularMarketOpen, double regularMarketDayHigh, double regularMarketDayLow, double marketPreviousClose){
        this.symbol= symbol;
        this.name = name;
        this.regularMarketOpen = regularMarketOpen;
        this.regularMarketDayHigh = regularMarketDayHigh;
        this.regularMarketDayLow= regularMarketDayLow;
        this.marketPreviousClose = marketPreviousClose;
    }

    public void setAmountBetted(double amountBetted) {
        this.amountBetted = amountBetted;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getAmountBetted() {
        return amountBetted;
    }

    public void setSymbol(String sym) {
        this.symbol = symbol;
    }
    public double getMarkerPreviousClose() {
        return marketPreviousClose;
    }

    public double getMarketPreviousClose() {
        return marketPreviousClose;
    }

    public double getRegularMarketOpen() {
        return regularMarketOpen;
    }

    public void setMarketPreviousClose(double marketPreviousClose) {
        this.marketPreviousClose = marketPreviousClose;
    }

    public void setRegularMarketOpen(double regularMarketOpen) {
        this.regularMarketOpen = regularMarketOpen;
    }

    public void setInvestedOn(boolean investedOn) {
        this.investedOn = investedOn;
    }

    public void setMarkerPreviousClose(double markerPreviousClose) {
        this.marketPreviousClose = markerPreviousClose;
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
