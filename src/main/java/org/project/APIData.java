package org.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

//Fetch the data + return a list
public class APIData {

    private JSONObject data;

    //Requesting APIData (live)
    public void fetchData() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://yahoo-finance127.p.rapidapi.com/price/eth-usd"))
                .header("content-type", "application/octet-stream")
                .header("X-RapidAPI-Key", "7fd9cfb8dbmshb2f9a3cb3aba987p14eec2jsn31f2b3486328")
                .header("X-RapidAPI-Host", "yahoo-finance127.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(response.body());
        JSONObject jsonResponse = new JSONObject(response.body());
        data = jsonResponse;
        System.out.println("RESPONSE is: " + response.body());
    }




   //private String responseBody = "{\"maxAge\":1,\"preMarketChange\":{},\"preMarketPrice\":{},\"preMarketSource\":\"FREE_REALTIME\",\"postMarketChangePercent\":{\"raw\":-0.0046434025,\"fmt\":\"-0.46%\"},\"postMarketChange\":{\"raw\":-0.7799988,\"fmt\":\"-0.78\"},\"postMarketTime\":1683935999,\"postMarketPrice\":{\"raw\":167.2,\"fmt\":\"167.20\"},\"postMarketSource\":\"DELAYED\",\"regularMarketChangePercent\":{\"raw\":-0.023826163,\"fmt\":\"-2.38%\"},\"regularMarketChange\":{\"raw\":-4.100006,\"fmt\":\"-4.10\"},\"regularMarketTime\":1683921604,\"priceHint\":{\"raw\":2,\"fmt\":\"2\",\"longFmt\":\"2\"},\"regularMarketPrice\":{\"raw\":167.98,\"fmt\":\"167.98\"},\"regularMarketDayHigh\":{\"raw\":177.38,\"fmt\":\"177.38\"},\"regularMarketDayLow\":{\"raw\":167.23,\"fmt\":\"167.23\"},\"regularMarketVolume\":{\"raw\":157849625,\"fmt\":\"157.85M\",\"longFmt\":\"157,849,625.00\"},\"averageDailyVolume10Day\":{},\"averageDailyVolume3Month\":{},\"regularMarketPreviousClose\":{\"raw\":172.08,\"fmt\":\"172.08\"},\"regularMarketSource\":\"FREE_REALTIME\",\"regularMarketOpen\":{\"raw\":176.07,\"fmt\":\"176.07\"},\"strikePrice\":{},\"openInterest\":{},\"exchange\":\"NMS\",\"exchangeName\":\"NasdaqGS\",\"exchangeDataDelayedBy\":0,\"marketState\":\"CLOSED\",\"quoteType\":\"EQUITY\",\"symbol\":\"TSLA\",\"underlyingSymbol\":null,\"shortName\":\"Tesla, Inc.\",\"longName\":\"Tesla, Inc.\",\"currency\":\"USD\",\"quoteSourceName\":\"Delayed Quote\",\"currencySymbol\":\"$\",\"fromCurrency\":null,\"toCurrency\":null,\"lastMarket\":null,\"volume24Hr\":{},\"volumeAllCurrencies\":{},\"circulatingSupply\":{},\"marketCap\":{\"raw\":532412596224,\"fmt\":\"532.41B\",\"longFmt\":\"532,412,596,224.00\"}}";


    public int maxAge() {
        ObjectMapper mapper = new ObjectMapper();
        int value = 101;
        try {
            JsonNode toRead = mapper.readTree(data.toString());
            JsonNode maxAgeNode = toRead.get("maxAge");

            // Check if maxAgeNode is null or if it can be converted to a double
            if (maxAgeNode != null && maxAgeNode.isNumber()) {
                value = maxAgeNode.asInt();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value);
        return value;
    }






/*
    public double postMarketChangePercent(){
        responseBody = responseBody.replaceAll("\"", " ");
        //System.out.println(responseBody);
        Pattern pattern = Pattern.compile("[-+0-9.]+%\\s},\\spostMarketChange");
        Matcher matcher = pattern.matcher(responseBody);
        //System.out.println(matcher.matches());

        String cleaner = "";
        while (matcher.find()){
            cleaner = matcher.group();
        }

        if(cleaner.isEmpty()) return 101;

        cleaner = cleaner.replaceAll("%\\s},\\spostMarketChange", "");
        if(cleaner.charAt(0) == '-'){
            cleaner = cleaner.replace("-", "");
            return Double.parseDouble(cleaner)*(-1);
        }
        return Double.parseDouble(cleaner);
    }




    public double regularMarketChangePercent(){
        responseBody= responseBody.replaceAll("\"", " ");
        //System.out.println(responseBody);

        Pattern pattern = Pattern.compile("[-+0-9.]+%\\s},\\sregularMarketChange");
        Matcher matcher = pattern.matcher(responseBody);

        String cleaner = "";
        while (matcher.find()){
            cleaner = matcher.group();
        }
        if(cleaner.isEmpty()) return 101;


        cleaner = cleaner.replaceAll("%\\s},\\sregularMarketChange", "");
        if(cleaner.charAt(0) == '-'){
            cleaner = cleaner.replace("-", "");
            return Double.parseDouble(cleaner)*(-1);
        }
        return Double.parseDouble(cleaner);
    }



    public double preMarketChange(){
        responseBody= responseBody.replaceAll("\"", " ");

        Pattern pattern = Pattern.compile("[-+0-9.]+%\\s},\\spreMarketPrice");
        Matcher matcher = pattern.matcher(responseBody);



        String cleaner = "";
        while (matcher.find()){
            cleaner = matcher.group();
        }

        if(cleaner.isEmpty()) return 101;


        cleaner = cleaner.replaceAll("%\\s},\\spreMarketPrice", "");
        if(cleaner.charAt(0) == '-'){
            cleaner = cleaner.replace("-", "");
            return Double.parseDouble(cleaner)*(-1);
        }
        return Double.parseDouble(cleaner);
    }




    public String extractNameOfCompany(){
        responseBody= responseBody.replaceAll("\"", " ");

        Pattern pattern = Pattern.compile("shortName\\s:\\s.*?,.*?\\.");
        Matcher matcher = pattern.matcher(responseBody);

        String cleaner = "";
        while (matcher.find()){
            cleaner = matcher.group();
        }

        if(cleaner.isEmpty()) return "101";

        return cleaner.replaceAll("shortName : ", "");
    }

*/


}




//NAMING CONVENTION 101 = NOT FOUND (VALUE)