package org.project;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.css.StyleableStringProperty;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stock{
    //private double price;
    private final String stockDbPath = "src/main/resources/StocksDB.csv";
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



    public void saveStocksIfNewBet(String username, String name, double regularMarketDayHigh, double regularMarketDayLow,
                                   double regularMarketOpen, double marketPreviousClose) {
        String toWrite = username + "," + name + "," + regularMarketDayHigh + "," + regularMarketDayLow
                + "," + regularMarketOpen + "," + marketPreviousClose;

        try (CSVReader reader = new CSVReader(new FileReader(stockDbPath))) {
            List<String[]> existingData = reader.readAll();

            // Check if the string exists already
            boolean isBetNew = true;
            for (String[] row : existingData) {
                String existingRecord = String.join(",", row);
                if (existingRecord.equals(toWrite)) {
                    System.out.println("The bet already exists in the database.");
                    isBetNew = false;
                    break;
                }
            }

            if (isBetNew) {
                try (CSVWriter writer = new CSVWriter(new FileWriter(stockDbPath, true))) {
                    String[] data = toWrite.split(",");
                    writer.writeNext(data);
                    System.out.println("Bet added to the database.");
                } catch (IOException e) {
                    System.out.println("Error occurred while writing the bet.");
                }
            }
        } catch (IOException | CsvException e) {
            System.out.println("Error occurred while reading the database.");
        }
    }





    public List<Object> getSavedStocks(String username) {
        List<Object> userStocks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(stockDbPath))) {
            List<String[]> allData = reader.readAll();

            for (String[] row : allData) {
                if (row.length > 0 && row[0].equals(username)) {
                    for (String column : row) {
                        try {
                            double value = Double.parseDouble(column);
                            userStocks.add(value);
                        } catch (NumberFormatException e) {
                            userStocks.add(column);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del database.");
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        return userStocks;
    }







}
