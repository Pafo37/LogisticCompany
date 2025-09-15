package com.logisticcompany.service.keycloak;

import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class KeyCloakServiceImpl implements KeyCloakService {

    private final Keycloak keycloak;
    private final String REALM = "logistics-app";

    @Override
    public String registerUser(String username, String password, String email, String firstName, String lastName, String roleName) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        Response response = keycloak.realm(REALM).users().create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatus());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        keycloak.realm(REALM)
                .users()
                .get(userId)
                .resetPassword(passwordCred);

        RoleRepresentation role = keycloak.realm(REALM)
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(REALM)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(role));

        return userId;
    }

    @Override
    public void updateUser(String keycloakUserId, String email, String name, String username) {
        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            throw new IllegalArgumentException("keycloakUserId is required");
        }

        UsersResource users = keycloak.realm(REALM).users();
        UserResource userResource = users.get(keycloakUserId);
        UserRepresentation userResourceRepresentation = userResource.toRepresentation();
        if (userResourceRepresentation == null) {
            throw new IllegalArgumentException("Keycloak user not found: " + keycloakUserId);
        }

        if (username != null && !username.isBlank()) {
            userResourceRepresentation.setUsername(username.trim().toLowerCase());
        }
        if (email != null) {
            userResourceRepresentation.setEmail(email.trim());
        }
        //Limitation, name is now combined from first and last name and keycloak sdk uses first and last name
        if (name != null) userResourceRepresentation.setFirstName(name.trim());

        userResource.update(userResourceRepresentation);
    }

    @Override
    public void deleteUser(String keycloakUserId) {
        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            return; // nothing to do
        }
        keycloak.realm(REALM).users().get(keycloakUserId).remove();
    }
}
