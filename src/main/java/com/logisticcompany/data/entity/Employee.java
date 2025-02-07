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
@Table(name = "employee")
public class Employee extends BaseEntity {

    private String name;
    private String role;
    private String email;
    private String phone;

   /* @ManyToOne //TODO:check wtf is that
    private Company company;*/

}