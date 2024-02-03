package modelTest;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.project.model.Stock;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    @Test
    public void testGetDefaultAmountInvested() {
        // Create a sample Stock object
        Stock stock = new Stock("AAPL", "Apple Inc.", 150.0, 160.0, 140.0, 155.0, "300",1000.0);

        // Perform the test
        double investedInvestedAmount = 0; // Assuming the sum of pieces is 10
        assertEquals(investedInvestedAmount, stock.getAmountInvested());
    }

    @Test
    public void testSaveStocks() throws IOException, CsvException {
        // Create a temporary CSV file for testing
        File tempFile = File.createTempFile("testStocksDB", ".csv");

        // Initialize a Stock object
        Stock stock = new Stock();
        stock.setStockDbPath(tempFile.getAbsolutePath());

        // Call the saveStocks method
        stock.saveStocks("testUser", "AAPL", 200.0, 150.0, 180.0, 190.0, 1000.0, 195.0);

        // Read the contents of the CSV file
        try (CSVReader reader = new CSVReader(new FileReader(tempFile))) {
            List<String[]> data = reader.readAll();

            // Check if the data contains the expected record
            boolean recordFound = false;
            for (String[] row : data) {
                if (row[0].equals("testUser") && row[1].equals("AAPL")) {
                    assertEquals("200.00", row[2]);
                    assertEquals("150.00", row[3]);
                    assertEquals("180.00", row[4]);
                    assertEquals("190.00", row[5]);
                    assertEquals("1000.00", row[6]);
                    recordFound = true;
                    break;
                }
            }

            // Assert that the record was found
            assertTrue(recordFound, "The record was found in the CSV file.");
        }

        // Clean up: delete the temporary file
        tempFile.delete();
    }


    @Test
    public void testGetStocks() throws IOException, CsvException{
        // Create a temporary CSV file for testing
        File tempFile = File.createTempFile("testStocksDB", ".csv");

        // Initialize a Stock object
        Stock stock = new Stock();
        stock.setStockDbPath(tempFile.getAbsolutePath());

        //Sample Stocks creation
        stock.saveStocks("testUser", "AAPL", 200.0, 150.0, 180.0, 190.0, 1000.0, 195.0);
        stock.saveStocks("testUser", "TSLA", 200.0, 150.0, 180.0, 190.0, 1000.0, 195.0);
        stock.saveStocks("testUser", "AMC", 200.0, 150.0, 180.0, 190.0, 1000.0, 195.0);

        List<List<String>> sampleStocks = stock.getSavedStocks("testUser");
        sampleStocks.forEach(e-> e.forEach(e2-> System.out.println(e2)));
        // Assert the size of the returned list
        assertEquals(3, sampleStocks.size(), "The number of saved stocks should be 3.");

        // Assert the contents of the first saved stock
        assertEquals("AAPL", sampleStocks.get(0).get(1), "The symbol of the first saved stock should be 'AAPL'.");
        assertEquals("200.00", sampleStocks.get(0).get(2), "The regularMarketDayHigh of the first saved stock should be 200.0.");

        // Assert the contents of the second saved stock
        assertEquals("TSLA", sampleStocks.get(1).get(1), "The symbol of the second saved stock should be 'TSLA'.");
        assertEquals("150.00", sampleStocks.get(1).get(3), "The regularMarketDayLow of the second saved stock should be 150.0.");

        // Assert the contents of the third saved stock
        assertEquals("AMC", sampleStocks.get(2).get(1), "The symbol of the third saved stock should be 'AMC'.");
        assertEquals("180.00", sampleStocks.get(2).get(4), "The regularMarketOpen of the third saved stock should be 180.0.");

        // Assert that attempting to access a non-existent element returns null
        assertThrows(IndexOutOfBoundsException.class, () ->{
            assertNull(sampleStocks.get(3), "Accessing a non-existent element should return null.");
        });


        tempFile.delete();

    }

}
