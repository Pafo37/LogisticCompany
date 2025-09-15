package com.logisticcompany.service.keycloak;

import org.springframework.http.HttpHeaders;

public interface KeyCloakService {

    HttpHeaders createAuthHeaders(String token);

    String registerUser(String username, String password, String email,
                        String firstName, String lastName, String roleName);

    public void updateUser(String keycloakUserId, String email, String name, String username);
    public void deleteUser(String keycloakUserId);

}
