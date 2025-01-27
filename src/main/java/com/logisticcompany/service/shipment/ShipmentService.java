package com.logisticcompany.service.shipment;

import com.logisticcompany.data.entity.Shipment;

import java.util.List;

public interface ShipmentService {

    List<Shipment> getAllShipments();

    Shipment getShipmentById(long id);

    void saveShipment(Shipment shipment);

    void deleteShipment(Shipment shipment);
}
