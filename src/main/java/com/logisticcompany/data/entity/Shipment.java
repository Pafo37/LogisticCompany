package com.logisticcompany.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment extends BaseEntity {

    private String sender;
    private String receiver;
    private String deliveryAddress;
    private double weight;

    @ManyToOne //TODO: check wtf is that
    private Company company;

    // constructors, getters, setters
}