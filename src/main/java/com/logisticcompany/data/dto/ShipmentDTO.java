package com.logisticcompany.data.dto;

import lombok.Data;

@Data
public class ShipmentDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private String deliveryAddress;
    private double weight;
    private Double price;
    private String registeredByName;
    private Long deliveryOfficeId;
}
