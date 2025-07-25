package com.logisticcompany.data.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String role;
    private String keycloakUserId;
}
