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
    private String nameOfCompany;

    public APIData() {this.symbol=symbol;}
    //Requesting APIData (live)
    public void fetchData(String symbol) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://yahoo-finance127.p.rapidapi.com/price/"+symbol))
                .header("content-type", "application/octet-stream")
                .header("X-RapidAPI-Key", "53a9ffe29bmshe9a312dbe835035p107921jsnd394d54af9d8")
                .header("X-RapidAPI-Host", "yahoo-finance127.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        data = new JSONObject(response.body());
        System.out.println("RESPONSE is: " + response.body());
        nameOfCompany=this.extractNameOfCompany();
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


    public String getNameOfCompany() {
        return nameOfCompany;
    }

    public int maxAge() {
        int defaultValue = 101; // Defined value

        try {
            if (data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode maxAgeNode = toRead.get("maxAge");

                // Check if maxAgeNode is null and if it could be converted into int
                if (maxAgeNode != null && maxAgeNode.isInt()) {
                    return maxAgeNode.asInt();
                } else {
                    System.out.println("Value not available or invalid: maxAge");
                }
            } else {
                System.out.println("Data is null.");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //If it was not possible to obtain a value, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }





    public double postMarketChangePercent() {
        double defaultValue = 101.0; // Default value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode postMarketChangePercentNode = toRead.get("postMarketChangePercent");

                // Check if postMarketChangePercentNode is not null and could not be parsed in double
                if (postMarketChangePercentNode != null && postMarketChangePercentNode.isDouble()) {
                    return postMarketChangePercentNode.asDouble();
                } else {
                    System.out.println("Value not available or invalid: postMarketChangePercent");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If a value couldn't be obtained, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }


    public double regularMarketChangePercent() {
        double defaultValue = 101.0; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode regularMarketChangePercentNode = toRead.get("regularMarketChangePercent");

                // Check if regularMarketChangePercentNode is not null, and it can be pared with double
                if (regularMarketChangePercentNode != null && regularMarketChangePercentNode.isDouble()) {
                    return regularMarketChangePercentNode.asDouble();
                } else {
                    System.out.println("Value not available or invalid: regularMarketChangePercent");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If a value couldn't be obtained, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }




    public double preMarketChange() {
        double defaultValue = 101.0; // Defined value

        try {
            if(data != null) {
                {
                    JsonNode toRead = mapper.readTree(data.toString());
                    JsonNode preMarketChangeNode = toRead.get("preMarketChange");

                    // Controlla se preMarketChangeNode non è null e può essere convertito in un double
                    if (preMarketChangeNode != null && preMarketChangeNode.isDouble()) {
                        return preMarketChangeNode.asDouble();
                    } else {
                        System.out.println("Value not available or invalid: preMarketChange");
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Se non è stato possibile ottenere un valore, restituisci il valore predefinito
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }



    public String extractNameOfCompany() {
        String value = "101"; // Initialize as empty string

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode shortNameNode = toRead.get("shortName");

                // Check if shortNameNot is not null
                if (shortNameNode != null) {
                    value = shortNameNode.asText();
                } else {
                    System.out.println("Value not available: shortName");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(value);
        return value;
    }




    public double regularMarketDayHigh() {
        double defaultValue = 101.0; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode regularMarketOpenNode = toRead.get("regularMarketDayHigh");

                // Check if regualrMarketOpenNode is not null
                if (regularMarketOpenNode != null) { //&& regularMarketOpenNode.isDouble()
                    double fmtValue = regularMarketOpenNode.get("fmt").asDouble();
                    System.out.println("Value in fmt format: " + fmtValue);
                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketDayHigh");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }


    public double regularMarketDayOpen() {
        double defaultValue = 101.0; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode regularMarketOpenNode = toRead.get("regularMarketOpen");

                // Check if regularMarketOpenNode is not null
                if (regularMarketOpenNode != null) { //&& regularMarketOpenNode.isDouble()
                    double fmtValue = regularMarketOpenNode.get("fmt").asDouble();
                    System.out.println("Value in fmt format: " + fmtValue);
                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketOpen");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }



    public double regularMarketDayLow() {
        double defaultValue = 101.0; // Define value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode regularMarketOpenNode = toRead.get("regularMarketDayLow");

                // Check if regularMarketOpenNode is not null
                if (regularMarketOpenNode != null) { //&& regularMarketOpenNode.isDouble()
                    double fmtValue = regularMarketOpenNode.get("fmt").asDouble();
                    System.out.println("Value in fmt format: " + fmtValue);
                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketDayLow");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }


    public double regularMarketPreviousClose() {
        double defaultValue = 101.0; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode regularMarketOpenNode = toRead.get("regularMarketPreviousClose");

                // Check if regularMarketOpenNode is not null
                if (regularMarketOpenNode != null) { //&& regularMarketOpenNode.isDouble()
                    double fmtValue = regularMarketOpenNode.get("fmt").asDouble();
                    System.out.println("Value in fmt format: " + fmtValue);
                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketPreviousClose");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }


    public String extractSymbolOfCompany() {
        String defaultValue = "101"; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode symbolNode = toRead.get("symbol");
                //System.out.println(symbolNode);

                // Check if symbolNode is not null and if symbolNode has a string
                if (symbolNode != null && symbolNode.isTextual()) {
                    return symbolNode.asText();
                } else {
                    System.out.println("Value not available or invalid: symbol");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }



    public String extractCurrencySymbol() {
        String defaultValue = "101"; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode currencySymbolNode = toRead.get("currencySymbol");

                // Check if currencySymbolNode is not null and if currecySymbolNode contains a string
                if (currencySymbolNode != null && currencySymbolNode.isTextual()) {
                    return currencySymbolNode.asText();
                } else {
                    System.out.println("Value not available or invalid: currencySymbol");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value "N/A"
        System.out.println("Using default value: " + defaultValue);
        return defaultValue;
    }

    public double extractAverageDailyVolume3Month() {
        double defaultValue = 0.0; // Defined value

        try {
            if (data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode averageDailyVolume3MonthNode = toRead.get("averageDailyVolume3Month");

                // Check if averageDailyVolume3MonthNode is not null and could be converted into double
                if (averageDailyVolume3MonthNode != null && averageDailyVolume3MonthNode.isDouble()) {
                    return averageDailyVolume3MonthNode.asDouble();
                } else {
                    System.out.println("Value not available or invalid: averageDailyVolume3Month");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
        System.out.println("Usage of default value: " + defaultValue);
        return defaultValue;
    }


}



//NAMING CONVENTION 101 = VALUE NOT FOUND