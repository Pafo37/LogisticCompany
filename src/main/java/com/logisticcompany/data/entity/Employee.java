package com.logisticcompany.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String role;
    private String email;
    private String phone;


}