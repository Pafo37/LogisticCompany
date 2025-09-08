package com.logisticcompany.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OfficeEmployee extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
