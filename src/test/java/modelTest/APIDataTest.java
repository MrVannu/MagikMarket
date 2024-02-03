package modelTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.model.APIData;
import static org.junit.jupiter.api.Assertions.*;


class APIDataTest {


    @BeforeEach
    void testDataFetch(){
        APIData testData = new APIData();
        assertThrows(IllegalArgumentException.class, () -> {
            testData.fetchData("wrong symbol");
        });

    }

    @Test
    void testExtractSymbolAndName() {

        APIData testData = new APIData();
        testData.fetchData("tsla");
        System.out.println(testData.extractSymbolOfCompany());

        // assert
        assertEquals( testData.extractNameOfCompany(), "Tesla, Inc.");
        assertEquals( testData.extractSymbolOfCompany(), "TSLA");
    }

    @Test
    void testExtractRegularMarketPrice() {
        // Create an instance of APIData
        APIData testData = new APIData();

        // Fetch data for a specific symbol (e.g., "tsla")
        testData.fetchData("tsla");

        // Extract the regular market change value
        double regularMarketChange = testData.regularMarketPrice();

        // Define your threshold
        double threshold = 200.0; // Example threshold

        // Assert that the actual value is less than the threshold
        assertTrue(regularMarketChange < threshold);
    }

    @Test
    void testPreRegularMarketChange() {


        // Create an instance of APIData
        APIData testData = new APIData();

        // Fetch data for a specific symbol (e.g., "tsla")
        testData.fetchData("TSLA");

        // Extract the regular market change value
        double regularMarketChange = testData.preMarketChange();

        // Define your threshold
        double threshold = 102; // Example threshold

        // Assert that the actual value is less than the threshold
        assertTrue(regularMarketChange < threshold);

    }


    @Test
    void extractNameOfCompanyTest() {
        // setup
        //String responseBody = "{\"regularMarketChangePercent\":{\"fmt\":\"+0.47%\",\"raw\":0.0046781195},\"postMarketChange\":{\"fmt\":\"+0.10%\",\"raw\":0.001}}";

        // Create an instance of APIData
        APIData testData = new APIData();

        // Fetch data for a specific symbol (e.g., "tsla")
        testData.fetchData("tsla");


        // assert
        assertEquals( testData.extractNameOfCompany(), "Tesla, Inc.");
    }



}