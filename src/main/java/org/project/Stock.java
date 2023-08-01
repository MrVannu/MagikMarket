package org.project;

public class Stock {
    private double price;
    private double regularMarketDayHigh;
    private double regularMarketDayLow;
    private double markerPreviousClose;
    private String id;
    private String description;
    private boolean available;

    public double getSymbol() {
        return price;
    }
    public void setSymbol(double price) {
        this.price = price;
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
    public void setRegularMarketDayLow(String description) {
        this.description = description;
    }
    public boolean isAvailable() {
        return available;
    }
    public double getRegularMarketDayHigh(){return regularMarketDayHigh;}
    /*private double discount(){
        return 0.0;
    }*/
    public boolean equals(Stock obj) {
        return this.getId().equals(obj.getId());
    }
}
