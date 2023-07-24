import org.junit.jupiter.api.Test;
import org.project.APIData;
import static org.junit.jupiter.api.Assertions.*;

class APIDataTest {

    @Test
    void testExtractPostMarketChange() {
        // setup
        //String responseBody = "{\"regularMarketChangePercent\":{\"fmt\":\"+0.47%\",\"raw\":0.0046781195},\"postMarketChange\":{\"fmt\":\"+0.10%\",\"raw\":0.001}}";

        // exercise
        APIData marketData = new APIData();
       double result = marketData.postMarketChangePercent();

        // assert
        assertEquals( result, -0.46);
    }

    @Test
    void testExtractRegularMarketChange() {
        // setup
        //tring responseBody = "{\"regularMarketChangePercent\":{\"fmt\":\"+0.47%\",\"raw\":0.0046781195},\"postMarketChange\":{\"fmt\":\"+0.10%\",\"raw\":0.001}}";

        // exercise
        APIData marketData = new APIData();
        double result = marketData.regularMarketChangePercent();

        // assert
        assertEquals( result, -2.38);
    }

    @Test
    void testPreRegularMarketChange() {
        // setup
        String responseBody = "{\"regularMarketChangePercent\":{\"fmt\":\"+0.47%\",\"raw\":0.0046781195},\"postMarketChange\":{\"fmt\":\"+0.10%\",\"raw\":0.001}}";

        // exercise
        APIData marketData = new APIData();
        double result = marketData.preMarketChange();

        // assert
        assertEquals( result, 101);
    }


    @Test
    void extractNameOfCompanyTest() {
        // setup
        //String responseBody = "{\"regularMarketChangePercent\":{\"fmt\":\"+0.47%\",\"raw\":0.0046781195},\"postMarketChange\":{\"fmt\":\"+0.10%\",\"raw\":0.001}}";

        // exercise
        APIData marketData = new APIData();
        String result = marketData.extractNameOfCompany();

        // assert
        assertEquals( result, "Tesla, Inc.");
    }



}