package com.logisticcompany.data.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String name;
    private String role;
    private String email;
    private String phone;
}
