package com.logisticcompany.service.shipment;

import com.logisticcompany.data.entity.Shipment;

import java.util.List;

public interface ShipmentService {

    List<Shipment> getAllShipments();

    Shipment getShipmentById(long id);

    void saveShipment(Shipment shipment);

    void deleteShipment(Long id);

    List<Shipment> getShipmentsDeliveredToOffice();

    List<Shipment> getShipmentsDeliveredToAddress();

    List<Shipment> getShipmentsByWeightGreaterThan(double weight);

    List<Shipment> searchShipmentsBySenderName(String senderName);
}
