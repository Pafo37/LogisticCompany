package com.logisticcompany.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Courier extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "assignedCourier")
    private List<Shipment> shipments = new ArrayList<>();

}