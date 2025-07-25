package com.logisticcompany.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8081")
                .realm("logistics-app")
                .clientId("admin-cli")
                .clientSecret("Hg7QoHSzlhlg40DLdiKdrGvI2rmp6Cci")
                .username("pafo")
                .password("123")
                .grantType("client_credentials")
                .build();
    }
}
