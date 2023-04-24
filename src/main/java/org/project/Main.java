package org.project;

import java.io.IOException;
import java.net.http.*;
import java.net.*;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.body());
    }
}