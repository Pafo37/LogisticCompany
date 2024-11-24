package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {


}
