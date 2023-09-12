package org.project;

import java.io.IOException;

public interface HistoryManagement {

    static void updateDataHistory(
            int maxAge,
            double postMarketChangePercent,
            double regularMarketChangePercent,
            double preMarketChange,
            String extractNameOfCompany,
            double regularMarketDayHigh,
            double regularMarketDayLow,
            double regularMarketPreviousClose,
            String symbolOfCompany,
            String extractCurrencySymbol
    ) throws IOException {}

    double generateNextPrevision(String nameToScanFor, String parameter, short precisionRange);

    void giveATip(String nameToScanFor, String parameter);

}