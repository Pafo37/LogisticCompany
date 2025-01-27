package com.logisticcompany.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client extends BaseEntity {
    private String name;
    private String email;
    private String phone;
    private String address;
}
