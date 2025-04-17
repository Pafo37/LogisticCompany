package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findBySenderOrReceiver(Client sender, Client receiver);

    List<Shipment> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}