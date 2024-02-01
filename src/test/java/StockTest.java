import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.project.APIData;
import org.project.Stock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    private final String testCsvPath = "src/test/resources/TestStocksDB.csv"; // Path for the test CSV file

    @AfterEach
    void cleanUpTestData() {
        // Clean up: Remove the last test entry from the CSV
        try {
            List<String> lines = Files.readAllLines(Path.of(testCsvPath));
            if (!lines.isEmpty()) {
                lines.remove(lines.size() - 1); // Remove the last line added by the test
                Files.write(Path.of(testCsvPath), lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void testSaveAndGetStocks() {
        Stock stock = new Stock();

        // Test data
        String username = "testUser";
        String symbol = "AAPL";
        double regularMarketDayHigh = 150.0;
        double regularMarketDayLow = 140.0;
        double regularMarketOpen = 145.0;
        double marketPreviousClose = 142.0;
        double amountBet = 1000.0;
        double regularMarketPrice = 144.0;

        // Save a stock
        stock.saveStocks(username, symbol, regularMarketDayHigh, regularMarketDayLow,
                regularMarketOpen, marketPreviousClose, amountBet, regularMarketPrice);

        // Get saved stocks for the user
        var savedStocks = stock.getSavedStocks(username);

        // Assertions
        assertFalse(savedStocks.isEmpty());
        assertEquals(1, savedStocks.size());

        var savedStock = savedStocks.get(0);
        assertEquals(username, savedStock.get(0));
        assertEquals(symbol, savedStock.get(1));
        assertEquals(regularMarketDayHigh, Double.parseDouble(savedStock.get(2)));
        assertEquals(regularMarketDayLow, Double.parseDouble(savedStock.get(3)));
        assertEquals(regularMarketOpen, Double.parseDouble(savedStock.get(4)));
        assertEquals(marketPreviousClose, Double.parseDouble(savedStock.get(5)));
        assertEquals(amountBet, Double.parseDouble(savedStock.get(6)));
        assertNotNull(savedStock.get(7)); // Time should not be null
        assertEquals(amountBet / regularMarketPrice, Double.parseDouble(savedStock.get(8)));

        // Clean up: Remove the test entry from the CSV
        // Add your logic here to remove the test entry from the CSV file
    }

    @Test
    void testGetSumAndPieces() {
        APIData testData = new APIData();
        testData.fetchData("tsla");
        Stock stock = new Stock();
        stock.setStockDbPath(testCsvPath); // Set the test CSV path

        // Test data
        String username = "testUser";
        String symbolAAPL = "AAPL";
        String symbolGOOGL = "GOOGL";

        // Assuming you have some saved stocks for the user
        stock.saveStocks(username, symbolAAPL, 150.0, 140.0, 145.0, 142.0, 1000.0, 144.0);
        stock.saveStocks(username, symbolAAPL, 160.0, 150.0, 155.0, 152.0, 1200.0, 158.0);
        stock.saveStocks(username, symbolGOOGL, 1200.0, 1100.0, 1150.0, 1120.0, 5000.0, 1180.0);

        // Get sum of pieces for the symbols
        double sumOfPiecesAAPL = stock.getSumAndPieces(username, symbolAAPL);
        double sumOfPiecesGOOGL = stock.getSumAndPieces(username, symbolGOOGL);

        // Assertions
        assertEquals(1000.0 / 144.0 + 1200.0 / 158.0, sumOfPiecesAAPL);
        assertEquals(5000.0 / 1180.0, sumOfPiecesGOOGL);
    }

    @Test
    void testGetAverageOfPurchased() {
        Stock stock = new Stock();
        stock.setStockDbPath(testCsvPath); // Set the test CSV path

        // Test data
        String username = "testUser";
        String symbolAAPL = "AAPL";
        String symbolGOOGL = "GOOGL";

        // Assuming you have some saved stocks for the user
        stock.saveStocks(username, symbolAAPL, 150.0, 140.0, 145.0, 142.0, 1000.0, 144.0);
        stock.saveStocks(username, symbolAAPL, 160.0, 150.0, 155.0, 152.0, 1200.0, 158.0);
        stock.saveStocks(username, symbolGOOGL, 1200.0, 1100.0, 1150.0, 1120.0, 5000.0, 1180.0);

        // Get average of purchased for the symbols
        double averageAAPL = stock.getAverageOfPurchased(username, symbolAAPL);
        double averageGOOGL = stock.getAverageOfPurchased(username, symbolGOOGL);

        // Assertions
        assertEquals((144.0 + 158.0) / 2, averageAAPL);
        assertEquals(1180.0, averageGOOGL);
    }
}
