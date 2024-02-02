package org.project.model;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.project.model.APIData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Stock extends APIData {
    private String stockDbPath = "src/main/resources/StocksDB.csv";
    private double regularMarketDayHigh;
    private double regularMarketDayLow;
    private double regularMarketOpen;
    private double marketPreviousClose;

    private double regularMarketPrice;

    private double volume;
    private String symbol;
    private String name;
    private double amountBetted = 0.0;
    private boolean investedOn = false;




    public Stock(String symbol, String name, double regularMarketOpen, double regularMarketDayHigh, double regularMarketDayLow, double marketPreviousClose, String volume, double regularMarketPrice){
        this.symbol= symbol;
        this.name = name;
        this.regularMarketOpen = regularMarketOpen;
        this.regularMarketDayHigh = regularMarketDayHigh;
        this.regularMarketDayLow= regularMarketDayLow;
        this.marketPreviousClose = marketPreviousClose;
        this.volume= Double.parseDouble(volume);
        this.regularMarketPrice = regularMarketPrice;
    }

    public Stock(String symbol, double regularMarketOpen, double regularMarketDayHigh, double regularMarketDayLow, double marketPreviousClose, double amountBet){
        this.symbol= symbol;
        this.regularMarketOpen = regularMarketOpen;
        this.regularMarketDayHigh = regularMarketDayHigh;
        this.regularMarketDayLow= regularMarketDayLow;
        this.marketPreviousClose = marketPreviousClose;
    }

    public Stock() {}

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

    public void setStockDbPath(String stockDbPath) {
        this.stockDbPath = stockDbPath;
    }

    public double getRegularMarketOpen() {
        return regularMarketOpen;
    }

    public double getRegularMarketPrice(){ return regularMarketPrice;}
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
    public double getVolume() {
        return volume;
    }
    public double getRegularMarketDayHigh(){return regularMarketDayHigh;}

    public void setRegularMarketDayHigh(double regularMarketDayHigh) {
        this.regularMarketDayHigh = regularMarketDayHigh;
    }

    public static String getTime() {
        ZoneId zoneId = ZoneId.of("Europe/Rome");
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy-HH:mm");
        return currentTime.format(formatter);
    }




    // Save the stocks which the user invested in including parameters, and the time of the investment
    public void saveStocks(String username, String symbol, double regularMarketDayHigh, double regularMarketDayLow,
                                   double regularMarketOpen, double marketPreviousClose, double amountBet,
                                   double regularMarketPrice) {

        String numberOfPieces = String.valueOf(amountBet / regularMarketPrice);

        String toWrite = String.format("%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%s,%s",
                username, symbol, regularMarketDayHigh, regularMarketDayLow,
                regularMarketOpen, marketPreviousClose, amountBet, getTime(), numberOfPieces);


        try (CSVReader reader = new CSVReader(new FileReader(stockDbPath))) {
            List<String[]> existingData = reader.readAll();

            // Check if the string exists already
            boolean isBetNew = true;
            for (String[] row : existingData) {
                String existingRecord = String.join(",", row);
                if (existingRecord.equals(toWrite)) {
                    System.out.println("The investment already exists in the database.");
                    isBetNew = false;
                    break;
                }
            }

            if (isBetNew) {
                try (CSVWriter writer = new CSVWriter(new FileWriter(stockDbPath, true))) {
                    String[] data = toWrite.split(",");
                    writer.writeNext(data);
                    System.out.println("Record added to the database.");
                } catch (IOException e) {
                    System.out.println("Error occurred while recording the investment.");
                }
            }
        } catch (IOException | CsvException e) {
            System.out.println("Error occurred while reading from the database.");
        }
    }


    // Retrieve the saved stocks which the user invested in + details
    public List<List<String>> getSavedStocks(String username) {
        List<List<String>> userStocks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(stockDbPath))) {
            List<String[]> allData = reader.readAll();
            for (String[] row : allData) {
                if (row[0].equals(username)) {
                    List<String> rowData = new ArrayList<>();
                    Collections.addAll(rowData, row);
                    userStocks.add(rowData);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading from the database.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userStocks;
    }



    public double getSumAndPieces(String username, String symbol) {
        List<List<String>> result = getSavedStocks(username);
        double sumOfPieces = 0;

        for (List<String> row : result) {
            if(row.get(1).equals(symbol)) sumOfPieces += Double.parseDouble(row.get(8));
        }

        return sumOfPieces;
    }


    public double getAverageOfPurchased(String username, String symbol) {
        List<List<String>> result = getSavedStocks(username);
        double total = 0.0;
        int counterOfItems = 0;

        for (List<String> row : result) {
            if (row.get(1).equals(symbol.toLowerCase())) {
                double purchasePrice = Double.parseDouble(row.get(6));
                double quantity = Double.parseDouble(row.get(8));

                if (quantity != 0.0) {
                    total += (purchasePrice / quantity);
                    counterOfItems += 1;
                }
            }
        }

        // Check if there are items before performing the division
        if (counterOfItems != 0) return Math.abs(total / counterOfItems);
        else return 0.0;

    }


}
