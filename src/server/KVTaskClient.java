package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String urlPrefix;
    private final HttpClient client;
    private final String apiToken;

    public KVTaskClient(String path) {
        this.urlPrefix = path;
        this.client = HttpClient.newHttpClient();
        apiToken = register();

    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(urlPrefix + "/save/" + key + "?API_TOKEN=" + apiToken))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(urlPrefix + "/load/" + key + "?API_TOKEN=" + apiToken))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String register() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(urlPrefix + "/register"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);

        }
    }
}
