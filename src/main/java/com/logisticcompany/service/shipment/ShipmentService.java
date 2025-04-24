package com.logisticcompany.service.shipment;

import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Shipment;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface ShipmentService {

    List<Shipment> getAllShipments();

    Shipment getShipmentById(long id);

    void saveShipment(Shipment shipment);

    void deleteShipment(Long id);

    List<Shipment> getShipmentsByClient(Client client);

    List<Shipment> findShipmentsBetweenDates(LocalDate startDate, LocalDate endDate);

    BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate);

    List<Shipment> getShipmentsForClient(String username);

    Shipment createShipmentFromDTO(ShipmentDTO shipmentDTO, Principal principal);

    Shipment updateShipmentFromDTO(Long id, ShipmentDTO dto, Principal principal);
}
