package com.logisticcompany.service.courier;

import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.data.entity.User;

import java.util.List;
import java.util.Optional;

public interface CourierService {

    Courier getById(Long id);

    List<Courier> getAll();

    Optional<Courier> findByUserKeycloakId(String keycloakId);

    Courier getByUserKeycloakId(String keycloakId);

    Courier createForUser(User user);

    Courier createForUserKeycloakId(String keycloakId);

    void deleteById(Long id);

    List<Shipment> getShipments(Courier courier);

    List<Shipment> getShipmentsByUserKeycloakId(String keycloakId);
}
