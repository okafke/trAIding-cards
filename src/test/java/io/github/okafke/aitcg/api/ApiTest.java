package io.github.okafke.aitcg.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class ApiTest {
    public static void main(String[] args) {
        // Create a sample DTO
        CardCreationRequest cardRequest = new CardCreationRequest(Arrays.asList("anatomically accurate", "correctly colored", "insanely cute"), "lynx");

        // Set the API endpoint URL
        String apiUrl = "http://localhost:8080/card/create"; // Replace with your actual API endpoint

        // Call the method to make the POST request
        sendPostRequest(apiUrl, cardRequest);
    }

    @SneakyThrows
    private static void sendPostRequest(String apiUrl, CardCreationRequest cardRequest) {
        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Convert the DTO to JSON
        String jsonBody = new ObjectMapper().writeValueAsString(cardRequest);

        // Create a HttpRequest with the JSON body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status code and body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
