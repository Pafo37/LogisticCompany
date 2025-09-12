package com.logisticcompany.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment extends BaseEntity {

    public enum Status {
        PENDING_ASSIGNMENT,
        ASSIGNED,
        DELIVERED
    }

    private String deliveryAddress;
    private double weight;
    private double price;

    private LocalDateTime sentDate;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Client receiver;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING_ASSIGNMENT;

    @ManyToOne
    private Office deliveryOffice;

    @ManyToOne
    @JoinColumn(name = "registered_by_id")
    private OfficeEmployee registeredBy;

    @ManyToOne
    @JoinColumn(name = "assigned_courier_id")
    private Courier assignedCourier;

    @CreationTimestamp
    private LocalDateTime createdAt;

}