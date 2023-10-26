package org.project;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stock{
    private final String stockDbPath = "src/main/resources/StocksDB.csv";
    private double regularMarketDayHigh;
    private double regularMarketDayLow;
    private double regularMarketOpen;
    private double marketPreviousClose;

    private double volume;
    private String symbol;
    private String name;
    private String description;
    private boolean available;
    private double amountBetted = 0.0;
    private boolean investedOn = false;




    public Stock(String symbol, String name, double regularMarketOpen, double regularMarketDayHigh, double regularMarketDayLow, double marketPreviousClose, String volume){
        this.symbol= symbol;
        this.name = name;
        this.regularMarketOpen = regularMarketOpen;
        this.regularMarketDayHigh = regularMarketDayHigh;
        this.regularMarketDayLow= regularMarketDayLow;
        this.marketPreviousClose = marketPreviousClose;
        this.volume= Double.parseDouble(volume);
    }

    public Stock(String symbol, double regularMarketOpen, double regularMarketDayHigh, double regularMarketDayLow, double marketPreviousClose, double amountBet){
        this.symbol= symbol;
        this.regularMarketOpen = regularMarketOpen;
        this.regularMarketDayHigh = regularMarketDayHigh;
        this.regularMarketDayLow= regularMarketDayLow;
        this.marketPreviousClose = marketPreviousClose;
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
    public boolean isInvestedOn() {
        return investedOn;
    }

    public void setAmountBet(double amountBet) {
        this.amountBetted= amountBet;
    }


    public void setMarketPreviousClose(double marketPreviousClose) {
        this.marketPreviousClose = marketPreviousClose;
    }

    public void setRegularMarketOpen(double regularMarketOpen) {
        this.regularMarketOpen = regularMarketOpen;
    }


    public void setRegularMarketDayLow(double regularMarketDayLow) {
        this.regularMarketDayLow = regularMarketDayLow;
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

    public double getVolume() {
        return volume;
    }

    public double getRegularMarketDayHigh(){return regularMarketDayHigh;}

    public void setRegularMarketDayHigh(double regularMarketDayHigh) {
        this.regularMarketDayHigh = regularMarketDayHigh;
    }

    public boolean isEquals(Stock obj) {
        return this.getName().equals(obj.getName());
    }



    // Save the stocks which the user invested in
    public void saveStocks(String username, String symbol, double regularMarketDayHigh, double regularMarketDayLow,
                                   double regularMarketOpen, double marketPreviousClose, double amountBet) {
        String toWrite = username + "," + symbol + "," + regularMarketDayHigh + "," + regularMarketDayLow
                + "," + regularMarketOpen + "," + marketPreviousClose + "," + amountBet;

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



    // Retrieve the saved stocks which the user invested in
    public List<Stock> getSavedStocks(String username) {
        List<Stock> userStocks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(stockDbPath))) {
            List<String[]> allData = reader.readAll();
            for (String[] row : allData) {
                if (row[0].equals(username)) {
                    // symbol, regularMarketDayHigh, regularMarketDayLow, regularMarketOpen, marketPreviousClose
                    Stock obj = new Stock(row[1], Double.parseDouble(row[2]), Double.parseDouble(row[3]),
                            Double.parseDouble(row[4]),Double.parseDouble(row[5]), Double.parseDouble(row[6]));

                    userStocks.add(obj);
                } // else System.out.println("Row checked but no eligible value has been found");
            }
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del database.");
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        return userStocks;
    }


}
