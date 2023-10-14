package org.project;

import java.io.IOException;
import java.util.ArrayList;

public interface HistoryManagement {

    default void updateDataHistory(
            int maxAge,
            double postMarketChangePercent,
            double regularMarketChangePercent,
            double preMarketChange,
            String extractNameOfCompany,
            double regularMarketOpen,
            double regularMarketDayHigh,
            double regularMarketDayLow,
            double regularMarketPreviousClose,
            String symbolOfCompany,
            String extractCurrencySymbol
    ) throws IOException {}


    ArrayList<Double> generateNextPrevision(String nameToScanFor, LoginControl loginControl);

    void saveStocks (String username, String name, double regularMarketDayHigh, double regularMarketDayLow,
                     double regularMarketOpen, double markerPreviousClose, double amountBet);

    void getSavedStocks (String username);

}
