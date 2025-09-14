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
    private String deliveryOfficeName;
    private String status;
    private double weight;
    private Double price;
    private String registeredByName;
    private String assignedCourierName;
    private Long deliveryOfficeId;
}
