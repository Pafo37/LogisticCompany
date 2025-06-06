package com.logisticcompany.service.shipment;

import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Shipment;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface ShipmentService {

    List<ShipmentDTO> getAllShipments();

    ShipmentDTO getShipmentById(long id);

    ShipmentDTO saveShipment(ShipmentDTO shipment,Principal principal);

    void deleteShipment(Long id);

    List<ShipmentDTO> getShipmentsByClient(Client client);

    List<ShipmentDTO> findShipmentsBetweenDates(LocalDate startDate, LocalDate endDate);

    BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate);

    List<Shipment> getShipmentsForClient(String username);

    Shipment createShipmentFromDTO(ShipmentDTO shipmentDTO, Principal principal);

    ShipmentDTO updateShipmentFromDTO(Long id, ShipmentDTO dto, Principal principal);
}
