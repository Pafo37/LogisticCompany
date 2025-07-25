package com.logisticcompany.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private String role;

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @OneToOne(mappedBy = "user")
    private Client client;

    @OneToOne(mappedBy = "user")
    private Employee employee;

}