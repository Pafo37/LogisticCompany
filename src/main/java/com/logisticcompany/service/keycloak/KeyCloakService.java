package com.logisticcompany.service.keycloak;

public interface KeyCloakService {

    String registerUser(String username, String password, String email,
                        String firstName, String lastName, String roleName);

    void updateUser(String keycloakUserId, String email, String name, String username);
    void deleteUser(String keycloakUserId);

}
