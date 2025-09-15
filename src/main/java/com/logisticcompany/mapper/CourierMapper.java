package com.logisticcompany.mapper;

import com.logisticcompany.data.dto.CourierDTO;
import com.logisticcompany.data.entity.Courier;

public class CourierMapper {
    public static CourierDTO toDTO(Courier courier) {
        return new CourierDTO(
                courier.getId(),
                courier.getUser() != null ? courier.getUser().getUsername() : null,
                courier.getFirstName(),
                courier.getLastName(),
                courier.getEmail()
        );
    }
}