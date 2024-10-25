package com.example.keycloak;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.keycloak.events.admin.AdminEvent;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class HttpEventListenerProvider implements EventListenerProvider {

    @Override
    public void onEvent(Event event) {
        System.out.println("Event received: " + event.getType());
        System.out.println("User ID: " + event.getUserId());

        System.out.println(getAccessToken());

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        System.out.println("Admin Event received: " + adminEvent.getOperationType());
        System.out.println("Resource: " + adminEvent.getResourceType());
        getAccessToken();
    }

    @Override
    public void close() {
        // Clean up resources if needed
    }
    private String getAccessToken() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)  // Tiempo de conexión en milisegundos
                .setSocketTimeout(5000)   // Tiempo de espera de respuesta
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            HttpPost post = new HttpPost("http://localhost:8080/realms/master/protocol/openid-connect/token");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            params.add(new BasicNameValuePair("client_id", "event-listener-client"));
            params.add(new BasicNameValuePair("client_secret", "GwCzBWS23GmPwAr5BZgCelJAp0UYqC3T"));
            post.setEntity(new UrlEncodedFormEntity(params));

            // Log de la solicitud antes de enviarla
            System.out.println("Sending POST request to: " + post.getURI());

            try (CloseableHttpResponse response = httpClient.execute(post)) {
                // Log de la respuesta recibida
                System.out.println("Response status: " + response.getStatusLine());

                if (response.getStatusLine().getStatusCode() == 200) {
                    String json = EntityUtils.toString(response.getEntity());
                    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                    return jsonObject.get("access_token").getAsString();
                } else {
                    System.out.println("Failed to obtain access token. Status: " + response.getStatusLine().getStatusCode());
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}