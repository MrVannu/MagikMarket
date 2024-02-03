package org.project.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;



//Fetch the data + return a list
public class APIData{
    private JSONObject data;
    private final ObjectMapper mapper = new ObjectMapper();
    private short keyID = 0;

    /*
    This array is used for alternating the API calls to not reach the
    daily rate (to keep this feature free)

    Recently the developing team decided to pay for an unlimited API key with no call-limit.
    As a consequence, the following methods looks useless, however known the subscription bought
    will last until 16th of February, this method could be found paramount after that date as it allows
    to employ the API call much more many times than the basic rate, which was 35/day only.
    */

    private final String[] myKeys = {
            "1d42ba6144msh6f2e48b689d3770p10f476jsn4444c9191d86",  // pos 0
            "f1f087a0edmshe01f814ada01f62p139125jsnbbb280054789",  // pos 1
            "27c048ae5fmshcd2ff733b837b73p1941ebjsnaa4f26e7bbcd",  // pos 2
            "24fdb75248mshc59fb41bb36b935p198301jsn8d097a9c305b",  // pos 3
            "9569609b31msh116e10029f476fbp18a940jsnf0a4ff5bfbd7",  // pos 4
    }; // Max number of API calls per day => 35 (max) * 5 (n of keys) = 175 (calls per day)

    public APIData() {}


    //Requesting APIData (live)
    public void fetchData(String symbol) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://yahoo-finance127.p.rapidapi.com/price/" + symbol))
                .header("content-type", "application/octet-stream")
                .header("X-RapidAPI-Key", myKeys[keyID])
                .header("X-RapidAPI-Host", "yahoo-finance127.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            String errorMessageAPI = "{\"message\":\"Blocked User. Please contact your API provider.\"}";
            if (response.statusCode() == 429 ||
                    response.body().contains(errorMessageAPI)) { // Means daily rate limit has been exceeded
                System.out.println("\nERROR 429 DETECTED -> NO MORE API CALLS AVAILABLE\n");

                if (keyID < myKeys.length - 1) { // Check if there are more keys available
                    ++keyID;
                    fetchData(symbol); // Retry with the next key
                    System.out.println("Switching to API Key ID: " + keyID);
                }
                else System.out.println("NO MORE API KEYS AVAILABLE ATM");
                // Eventually: grace period (depends on the API provider)

            } else {
                data = new JSONObject(response.body());

                extractNameOfCompany();
            }
        } catch (IOException | InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public double preMarketChange() {
        double defaultValue = 101.0; // Default value

        try {
            if(data != null) {
                    JsonNode toRead = mapper.readTree(data.toString());
                    JsonNode preMarketChangeNode = toRead.get("preMarketChange");

                    // Controlla se preMarketChangeNode non è null e può essere convertito in un double
                    if (preMarketChangeNode != null && preMarketChangeNode.isDouble()) {
                        return preMarketChangeNode.asDouble();
                    }
                    else System.out.println("Value not available or invalid: preMarketChange");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Se non è stato possibile ottenere un valore, restituisci il valore predefinito

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

                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketDayHigh");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value

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

                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketOpen");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
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
                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketDayLow");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value
        return defaultValue;
    }

    public double regularMarketPreviousClose() {
        double defaultValue = 101.0; // Defined value

        try {
            if (data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode regularMarketOpenNode = toRead.get("regularMarketPreviousClose");

                // Check if regularMarketOpenNode is not null
                if (regularMarketOpenNode != null) { //&& regularMarketOpenNode.isDouble()
                    double fmtValue = regularMarketOpenNode.get("fmt").asDouble();

                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketPreviousClose");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return defaultValue;
    }

    public double regularMarketPrice() {
        double defaultValue = 101.0; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode regularMarketOpenNode = toRead.get("regularMarketPrice");

                // Check if regularMarketOpenNode is not null
                if (regularMarketOpenNode != null) { //&& regularMarketOpenNode.isDouble()
                    double fmtValue = regularMarketOpenNode.get("fmt").asDouble();
                    return fmtValue;
                } else {
                    System.out.println("Value not available or invalid: regularMarketPrice");
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default value

        return defaultValue;
    }

    public String extractSymbolOfCompany() {
        String defaultValue = "101"; // Defined value

        try {
            if(data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode symbolNode = toRead.get("symbol");


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

        return defaultValue;
    }

    public String extractAverageDailyVolume3MonthFmt() {
        String defaultValue = "Value not available or invalid"; // Default value

        try {
            if (data != null) {
                JsonNode toRead = mapper.readTree(data.toString());
                JsonNode averageDailyVolume3MonthNode = toRead.get("averageDailyVolume3Month");

                // Check if averageDailyVolume3MonthNode is not null and contains the "fmt" key
                if (averageDailyVolume3MonthNode != null && averageDailyVolume3MonthNode.has("fmt")) {
                    String value = averageDailyVolume3MonthNode.get("fmt").asText();

                    // Remove the final 'M' character if it exists
                    if (value.endsWith("M")) {
                        value = value.substring(0, value.length() - 1);
                    }

                    return value;
                } else System.out.println("Value not available or invalid: averageDailyVolume3Month");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // If it wasn't possible to obtain a value, return the default message

        return defaultValue;
    }

    public double parseFormattedValue(String averageDailyVolumeFmt) {
        // Remove any commas or other non-numeric characters
        String cleanedValue = averageDailyVolumeFmt.replaceAll("[^0-9.]", "");

        // Parse the cleaned string to a double
        double numericValue = Double.parseDouble(cleanedValue);

        // Round the numeric value to two decimal places and return it
        return Math.round(numericValue * 100.0) / 100.0;
    }


    // Beta features --> to be implemented in the future
    //
    // The following methods were used int the first version of this application.
    // By the time it has been significantly improved and some data became obsolete.
    // The choice of keeping them is due to the fact they could be very useful for some
    // future implementation of new features.
    //
//    public int maxAge() {
//        int defaultValue = 101; // Default value
//
//        try {
//            if (data != null) {
//                JsonNode toRead = mapper.readTree(data.toString());
//                JsonNode maxAgeNode = toRead.get("maxAge");
//
//                // Check if maxAgeNode is null and if it could be converted into int
//                if (maxAgeNode != null && maxAgeNode.isInt()) {
//                    return maxAgeNode.asInt();
//                } else {
//                    System.out.println("Value not available or invalid: maxAge");
//                }
//            } else {
//                System.out.println("Data is null.");
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        //If it was not possible to obtain a value, return the default value
//        System.out.println("Using default value: " + defaultValue);
//        return defaultValue;
//    }
//
//    // For future improvements to the platform - WIP
//    public String extractCurrencySymbol() {
//        String defaultValue = "101"; // Defined value
//
//        try {
//            if(data != null) {
//                JsonNode toRead = mapper.readTree(data.toString());
//                JsonNode currencySymbolNode = toRead.get("currencySymbol");
//
//                // Check if currencySymbolNode is not null and if currecySymbolNode contains a string
//                if (currencySymbolNode != null && currencySymbolNode.isTextual()) {
//                    return currencySymbolNode.asText();
//                } else {
//                    System.out.println("Value not available or invalid: currencySymbol");
//                }
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        // If it wasn't possible to obtain a value, return the default value "N/A"
//        System.out.println("Using default value: " + defaultValue);
//        return defaultValue;
//    }
//
//    public double postMarketChangePercent() {
//        double defaultValue = 101.0; // Default value
//
//        try {
//            if(data != null) {
//                JsonNode toRead = mapper.readTree(data.toString());
//                JsonNode postMarketChangePercentNode = toRead.get("postMarketChangePercent");
//
//                // Check if postMarketChangePercentNode is not null and could not be parsed in double
//                if (postMarketChangePercentNode != null && postMarketChangePercentNode.isDouble()) {
//                    return postMarketChangePercentNode.asDouble();
//                } else {
//                    System.out.println("Value not available or invalid: postMarketChangePercent");
//                }
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        // If a value couldn't be obtained, return the default value
//        System.out.println("Using default value: " + defaultValue);
//        return defaultValue;
//    }
//
//    public double regularMarketChangePercent() {
//        double defaultValue = 101.0; // Default value
//
//        try {
//            if(data != null) {
//                JsonNode toRead = mapper.readTree(data.toString());
//                JsonNode regularMarketChangePercentNode = toRead.get("regularMarketChangePercent");
//
//                // Check if regularMarketChangePercentNode is not null, and it can be pared with double
//                if (regularMarketChangePercentNode != null && regularMarketChangePercentNode.isDouble()) {
//                    return regularMarketChangePercentNode.asDouble();
//                } else {
//                    System.out.println("Value not available or invalid: regularMarketChangePercent");
//                }
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        // If a value couldn't be obtained, return the default value
//        System.out.println("Using default value: " + defaultValue);
//        return defaultValue;
//    }

}

//NAMING CONVENTION 101 = VALUE NOT FOUND / CURRENTLY NOT AVAILABLE