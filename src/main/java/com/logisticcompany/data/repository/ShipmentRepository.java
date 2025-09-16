package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findBySenderOrReceiver(Client sender, Client receiver);

    @Query("SELECT COALESCE(SUM(s.price), 0) FROM Shipment s WHERE s.createdAt BETWEEN :start AND :end")
    BigDecimal sumRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Shipment> findAllByAssignedCourier_User_KeycloakId(String keycloakId);
}