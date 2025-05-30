package com.logisticcompany.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeDTO {
    private Long id;
    private String name;
    private String address;
}
