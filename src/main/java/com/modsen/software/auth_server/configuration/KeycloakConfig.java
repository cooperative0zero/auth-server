package com.modsen.software.auth_server.configuration;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakAdminApiProperties adminApiProperties;

    @Bean
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
                    .serverUrl(adminApiProperties.getKeycloakUrl())
                    .realm("master")
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId("admin-cli")
                    .clientSecret(adminApiProperties.getClientSecret())
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build())
                    .build();
    }
}
