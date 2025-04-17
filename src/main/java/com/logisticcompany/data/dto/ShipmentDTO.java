package com.logisticcompany.data.dto;

import lombok.Data;

@Data
public class ShipmentDTO {
    private Long senderId;
    private Long receiverId;
    private String deliveryAddress;
    private double weight;
    private boolean deliveredToOffice;
    private Long deliveryOfficeId;
}
