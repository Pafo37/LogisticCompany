package com.logisticcompany.service.keycloak;

import com.logisticcompany.data.dto.RegistrationDTO;
import org.springframework.http.HttpHeaders;

public interface KeyCloakService {

    String getAdminAccessToken();

    void registerUserInKeycloak(RegistrationDTO dto);

    void assignRoleToUser(String userId, String roleName, String token);

    HttpHeaders createAuthHeaders(String token);

    String findUserIdByUsername(String username, String token);
}
