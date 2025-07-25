package com.logisticcompany.service.keycloak;

import com.logisticcompany.data.dto.RegistrationDTO;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class KeyCloakServiceImpl implements KeyCloakService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String KEYCLOAK_BASE_URL = "http://localhost:8081";
    private final String REALM = "logistics-app";
    private final String CLIENT_ID = "admin-cli";
    private final String ADMIN_USERNAME = "pafo";
    private final String ADMIN_PASSWORD = "123";

    @Override
    public String getAdminAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", CLIENT_ID);
        body.add("username", ADMIN_USERNAME);
        body.add("password", ADMIN_PASSWORD);

        HttpEntity<?> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token",
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    @Override
    public void registerUserInKeycloak(RegistrationDTO dto) {
        String token = getAdminAccessToken();

        // 1. Create the user
        HttpHeaders headers = createAuthHeaders(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", dto.getUsername());
        userPayload.put("enabled", true);
        userPayload.put("email", dto.getEmail());
        userPayload.put("firstName", dto.getFirstName());
        userPayload.put("lastName", dto.getLastName());

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", dto.getPassword());
        credential.put("temporary", false);
        userPayload.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                KEYCLOAK_BASE_URL + "/admin/realms/logistics-app/users",
                request,
                Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("User creation failed: " + response.getStatusCode());
        }

        String userId = findUserIdByUsername(dto.getUsername(), token);

        assignRoleToUser(userId, dto.getRole(), token);
    }

    @Override
    public void assignRoleToUser(String userId, String roleName, String token) {
        // Fetch all realm roles and find the one matching by name
        ResponseEntity<RoleRepresentation[]> response = restTemplate.exchange(
                KEYCLOAK_BASE_URL + "/admin/realms/" + REALM + "/roles",
                HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(token)),
                RoleRepresentation[].class
        );

        RoleRepresentation[] allRoles = response.getBody();
        if (allRoles == null) {
            throw new RuntimeException("Failed to fetch roles from Keycloak");
        }

        RoleRepresentation role = Arrays.stream(allRoles)
                .filter(r -> r.getName().equals(roleName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        HttpEntity<List<RoleRepresentation>> assignRequest =
                new HttpEntity<>(List.of(role), createAuthHeaders(token));

        ResponseEntity<Void> assignResponse = restTemplate.postForEntity(
                KEYCLOAK_BASE_URL + "/admin/realms/" + REALM + "/users/" + userId + "/role-mappings/realm",
                assignRequest,
                Void.class
        );

        if (!assignResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to assign role: " + assignResponse.getStatusCode());
        }
    }

    @Override
    public HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public String findUserIdByUsername(String username, String token) {
        ResponseEntity<UserRepresentation[]> response = restTemplate.exchange(
                KEYCLOAK_BASE_URL + "/admin/realms/" + REALM + "/users?username=" + username,
                HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(token)),
                UserRepresentation[].class
        );

        return Objects.requireNonNull(response.getBody())[0].getId();
    }
}
