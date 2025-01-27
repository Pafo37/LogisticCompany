package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByClient(Client client);

    List<Shipment> findByDeliveredToOfficeTrue();

    List<Shipment> findByDeliveredToOfficeFalse();

    List<Shipment> findByWeightGreaterThan(double weight);

    List<Shipment> findBySenderNameContaining(String senderName);

    List<Shipment> findByReceiverNameContaining(String receiverName);

    List<Shipment> findByDeliveryAddressContaining(String deliveryAddress);
}