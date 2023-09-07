package org.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


//Fetch the data + return a list
public class APIData {

    private String symbol;
    private JSONObject data;
    private ObjectMapper mapper = new ObjectMapper();

    public APIData() {}
    //Requesting APIData (live)
    public void fetchData(/*String symbol*/) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://yahoo-finance127.p.rapidapi.com/price/"+symbol))
                .header("content-type", "application/octet-stream")
                .header("X-RapidAPI-Key", "c3f98819famsh3e9d4ed812b7142p19441fjsnee76f5e53fe3")
                .header("X-RapidAPI-Host", "yahoo-finance127.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.body());
        JSONObject jsonResponse = new JSONObject(response.body());
        data = jsonResponse;
        System.out.println("RESPONSE is: " + response.body());
    }


    //EXAMPLE OF API RESPONSE:
    //private data responseBody = "{\"maxAge\":1,\"preMarketChange\":{},\"preMarketPrice\":{},\"preMarketSource\":\"FREE_REALTIME\",
    // \"postMarketChangePercent\":{\"raw\":-0.0046434025,\"fmt\":\"-0.46%\"},\"postMarketChange\":{\"raw\":-0.7799988,\"fmt\":\"-0.78\"},
    // \"postMarketTime\":1683935999,\"postMarketPrice\":{\"raw\":167.2,\"fmt\":\"167.20\"},\"postMarketSource\":\"DELAYED\",\
    // "regularMarketChangePercent\":{\"raw\":-0.023826163,\"fmt\":\"-2.38%\"},\"regularMarketChange\":{\"raw\":-4.100006,\"fmt\":\"-4.10\"},\
    // "regularMarketTime\":1683921604,\"priceHint\":{\"raw\":2,\"fmt\":\"2\",\"longFmt\":\"2\"},\"regularMarketPrice\":{\"raw\":167.98,\"fmt\":\"167.98\"},
    // \"regularMarketDayHigh\":{\"raw\":177.38,\"fmt\":\"177.38\"},\"regularMarketDayLow\":{\"raw\":167.23,\"fmt\":\"167.23\"},
    // \"regularMarketVolume\":{\"raw\":157849625,\"fmt\":\"157.85M\",\"longFmt\":\"157,849,625.00\"},\"averageDailyVolume10Day\":{},
    // \"averageDailyVolume3Month\":{},\"regularMarketPreviousClose\":{\"raw\":172.08,\"fmt\":\"172.08\"},\"regularMarketSource\":\"FREE_REALTIME\",
    // \"regularMarketOpen\":{\"raw\":176.07,\"fmt\":\"176.07\"},\"strikePrice\":{},\"openInterest\":{},\"exchange\":\"NMS\",\"exchangeName\":\"NasdaqGS\",
    // \"exchangeDataDelayedBy\":0,\"marketState\":\"CLOSED\",\"quoteType\":\"EQUITY\",\"symbol\":\"TSLA\",\"underlyingSymbol\":null,
    // \"shortName\":\"Tesla, Inc.\",\"longName\":\"Tesla, Inc.\",\"currency\":\"USD\",\"quoteSourceName\":\"Delayed Quote\",\"currencySymbol\":\"$\",
    // \"fromCurrency\":null,\"toCurrency\":null,\"lastMarket\":null,\"volume24Hr\":{},\"volumeAllCurrencies\":{},\"circulatingSupply\":{},
    // \"marketCap\":{\"raw\":532412596224,\"fmt\":\"532.41B\",\"longFmt\":\"532,412,596,224.00\"}}";


    public int maxAge() {
        int value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("maxAge");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.asInt();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public double postMarketChangePercent(){
        double value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("postMarketChangePercent");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.asDouble();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public double regularMarketChangePercent(){
        double value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("regularMarketChangePercent");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.asDouble();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public double preMarketChange(){
        double value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("preMarketChange");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.asDouble();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public String extractNameOfCompany(){
        String value = "101";
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("shortName");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.toString();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public double regularMarketDayHigh(){
        double value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("regularMarketDayHigh");

            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");

            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.asDouble();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public double regularMarketDayLow(){
        double value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("regularMarketDayLow");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.asDouble();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public double regularMarketPreviousClose(){
        double value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("regularMarketPreviousClose");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.asDouble();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public String extractSymbolOfCompany(){
        String value = "101";
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("symbol");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.toString();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


    public String extractCurrencySymbol(){
        String value = "101";
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("currencySymbol");
            System.out.println(maxAgeNode);
            toRead= mapper.readTree(maxAgeNode.toString());
            maxAgeNode = toRead.get("raw");
            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null) {
                value = maxAgeNode.toString();
            } else System.out.println("Value not available: regularMarketPreviousClose");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }


}



//NAMING CONVENTION 101 = VALUE NOT FOUND