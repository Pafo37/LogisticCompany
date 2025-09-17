package com.logisticcompany.service;

import com.logisticcompany.service.keycloak.KeyCloakServiceImpl;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyCloakServiceImplTest {

    private static final String REALM = "logistics-app";
    private static final String USER_ID = "userId";
    private static final String ROLE_NAME = "ROLE_CLIENT";

    @Mock
    private Keycloak keycloak;
    @Mock
    private RealmResource realmResource;
    @Mock
    private UsersResource usersResource;
    @Mock
    private UserResource userResource;
    @Mock
    private RolesResource rolesResource;
    @Mock
    private RoleResource roleResource;
    @Mock
    private RoleMappingResource roleMappingResource;
    @Mock
    private RoleScopeResource roleScopeResource;

    @InjectMocks
    private KeyCloakServiceImpl service;

    private void stubCreateUserSuccess(Response response) {
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(usersResource.get(USER_ID)).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);

        when(realmResource.roles()).thenReturn(rolesResource);
        when(rolesResource.get(ROLE_NAME)).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation(ROLE_NAME, null, false));
    }

    private void stubUserGetWithRepresentation(UserRepresentation rep) {
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(USER_ID)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(rep);
    }

    @Test
    void testRegisterUserSuccess() {
        when(keycloak.realm(REALM)).thenReturn(realmResource);

        Response created = Response.status(201)
                .location(URI.create("http://kc/realms/" + REALM + "/users/" + USER_ID))
                .build();
        stubCreateUserSuccess(created);

        String keyCloakId = service.registerUser(
                "Client1", "pw123!", " Email@EXAMPLE.com ", "First", "Last", ROLE_NAME);

        assertThat(keyCloakId).isEqualTo(USER_ID);

        ArgumentCaptor<CredentialRepresentation> credCap = ArgumentCaptor.forClass(CredentialRepresentation.class);
        verify(userResource).resetPassword(credCap.capture());
        assertThat(credCap.getValue().getValue()).isEqualTo("pw123!");

        ArgumentCaptor<List<RoleRepresentation>> rolesCap = ArgumentCaptor.forClass(List.class);
        verify(roleScopeResource).add(rolesCap.capture());
        assertThat(rolesCap.getValue()).extracting(RoleRepresentation::getName).containsExactly(ROLE_NAME);

        ArgumentCaptor<UserRepresentation> userCap = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(usersResource).create(userCap.capture());
        assertThat(userCap.getValue().getUsername()).isEqualTo("Client1");
    }

    @Test
    void testRegisteringUserWhichAlreadyExists() {
        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(Response.status(409).build());

        assertThatThrownBy(() -> service.registerUser("gosho", "123", "gosho@gmail.com", "Georgi", "Ivanov", ROLE_NAME))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to create user");

        verifyNoInteractions(roleScopeResource, roleResource, rolesResource, userResource);
    }

    @Test
    void testUpdateUserBlankKeycloakId() {
        assertThatThrownBy(() -> service.updateUser("  ", "meow@gmail.com", "Garfield", "meowmeow"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("keycloakUserId is required");
        verifyNoInteractions(realmResource);
    }

    @Test
    void testUpdateUserNotFound() {
        when(keycloak.realm(REALM)).thenReturn(realmResource);
        stubUserGetWithRepresentation(null);

        assertThatThrownBy(() -> service.updateUser(USER_ID, "new@gmail.com", "Gosho", "NEW"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Keycloak user not found");
    }

    @Test
    void testUpdatingUser() {
        when(keycloak.realm(REALM)).thenReturn(realmResource);
        UserRepresentation currentUser = new UserRepresentation();
        currentUser.setUsername("old");
        currentUser.setEmail("old@gmail.com");
        currentUser.setFirstName("Old");
        currentUser.setLastName("Name");
        stubUserGetWithRepresentation(currentUser);

        service.updateUser(USER_ID, "new@gmail.com", "New Name", "newuser");

        ArgumentCaptor<UserRepresentation> argumentCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(userResource).update(argumentCaptor.capture());
        UserRepresentation updatedUser = argumentCaptor.getValue();

        assertThat(updatedUser.getUsername()).isEqualTo("newuser");
        assertThat(updatedUser.getEmail()).isEqualTo("new@gmail.com");
        assertThat(updatedUser.getFirstName()).isEqualTo("New Name");
    }

    @Test
    void testDeleteUser() {
        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(USER_ID)).thenReturn(userResource);

        service.deleteUser(USER_ID);

        verify(userResource).remove();
    }
}
