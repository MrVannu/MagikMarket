import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("\nHello World!");

       /* HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://myallies-breaking-news-v1.p.rapidapi.com/GetCompanyDetailsBySymbol?symbol=eni"))
                .header("content-type", "application/octet-stream")
                .header("X-RapidAPI-Key", "7fd9cfb8dbmshb2f9a3cb3aba987p14eec2jsn31f2b3486328")
                .header("X-RapidAPI-Host", "myallies-breaking-news-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.body());*/


        //ArrayList<HttpResponse> contents = new ArrayList<>();




/*
        String url = "https://www.example.com";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("Response code: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response1 = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response1.append(inputLine);
        }
        in.close();
        String html = response1.toString();
        System.out.println(html);
        */


        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://yahoo-finance127.p.rapidapi.com/price/tsla"))
                .header("content-type", "application/octet-stream")
                .header("X-RapidAPI-Key", "7fd9cfb8dbmshb2f9a3cb3aba987p14eec2jsn31f2b3486328")
                .header("X-RapidAPI-Host", "yahoo-finance127.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response2 = null;

        try {
            response2 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response2.body());


        System.out.println("\nEND");
    }

}