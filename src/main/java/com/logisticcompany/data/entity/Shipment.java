package com.logisticcompany.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    private String deliveryAddress;
    private double weight;
    private double price;
    private boolean deliveredToOffice;

    private LocalDateTime sentDate;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Client receiver;

    @ManyToOne
    private Office sourceOffice;

    @ManyToOne
    private Office destinationOffice;

    @ManyToOne
    private Employee registeredBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

}