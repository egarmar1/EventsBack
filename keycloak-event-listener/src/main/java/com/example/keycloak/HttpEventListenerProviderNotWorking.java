//package com.example.keycloak;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.keycloak.events.Event;
//import org.keycloak.events.EventListenerProvider;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.entity.StringEntity;
//import org.keycloak.events.admin.AdminEvent;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HttpEventListenerProvider implements EventListenerProvider {
//
//    private final String targetUrl;
//
//    public HttpEventListenerProvider(String targetUrl) {
//        this.targetUrl = targetUrl;
//    }
//
//
//    @Override
//    public void onEvent(Event event) {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost request = new HttpPost(targetUrl);
//            String accessToken = getAccessToken();
//
//            String jsonPayload = String.format(
//                    "{\"eventType\": \"%s\", \"userId\": \"%s\"}",
//                    event.getType(), event.getUserId());
//
//            StringEntity entity = new StringEntity(jsonPayload);
//            request.setEntity(entity);
//            request.setHeader("Content-Type", "application/json");
//
//            // Añadimos el token JWT al header de la solicitud
//            request.setHeader("Authorization", "Bearer " + accessToken);
//
//            httpClient.execute(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost request = new HttpPost(targetUrl);
//            String accessToken = getAccessToken();
//
//            // Estructura básica del payload JSON para eventos administrativos
//            String jsonPayload = String.format(
//                    "{\"eventType\": \"%s\", \"resourceType\": \"%s\", \"operationType\": \"%s\"}",
//                    adminEvent.getOperationType(), adminEvent.getResourceType(), adminEvent.getResourcePath()
//            );
//
//            // Enviar el evento al microservicio
//            StringEntity entity = new StringEntity(jsonPayload);
//            request.setEntity(entity);
//            request.setHeader("Content-Type", "application/json");
//
//            request.setHeader("Authorization", "Bearer " + accessToken);
//
//
//            httpClient.execute(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void close() {
//
//    }
//
//    private String getAccessToken() {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            RequestConfig requestConfig = RequestConfig.custom()
//                    .setConnectTimeout(5000)  // Tiempo de conexión (en milisegundos)
//                    .setSocketTimeout(5000)   // Tiempo de espera de respuesta
//                    .build();
//
//            HttpPost post = new HttpPost("http://localhost:8080/realms/master/protocol/openid-connect/token");
//            List<NameValuePair> params = new ArrayList<>();
//            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
//            params.add(new BasicNameValuePair("client_id", "event-listener-client"));
//            params.add(new BasicNameValuePair("client_secret", "jSNmlf2gtit8Ll3nGWYGXFgPSpYEAukW"));
//            post.setEntity(new UrlEncodedFormEntity(params));
//
//            try (CloseableHttpResponse response = httpClient.execute(post)) {
//                String json = EntityUtils.toString(response.getEntity());
//                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
//                return jsonObject.get("access_token").getAsString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//}