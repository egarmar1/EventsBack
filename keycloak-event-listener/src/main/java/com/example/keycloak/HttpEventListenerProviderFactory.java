package com.example.keycloak;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class HttpEventListenerProviderFactory implements EventListenerProviderFactory {

    private String targetUrl;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new HttpEventListenerProvider();
    }

    @Override
    public void init(Config.Scope config) {
        this.targetUrl = "http://localhost:8072/fastbook/users/api/updateUsersTableInfo"; // URL fija
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // Se pueden añadir inicializaciones adicionales aquí, si es necesario
    }

    @Override
    public void close() {
        // Libera recursos si es necesario
    }

    @Override
    public String getId() {
        // El ID que Keycloak usará para identificar este proveedor de eventos
        return "http-event-listener";
    }
}
