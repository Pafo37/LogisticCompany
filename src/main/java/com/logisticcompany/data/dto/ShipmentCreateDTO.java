package com.logisticcompany.data.dto;

import lombok.Data;

@Data
public class ShipmentCreateDTO {
    private Long receiverId;
    private String deliveryAddress;
    private Long deliveryOfficeId;
    private double weight;
}