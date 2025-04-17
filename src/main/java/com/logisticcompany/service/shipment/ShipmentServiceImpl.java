package com.logisticcompany.service.shipment;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.data.repository.ShipmentRepository;
import com.logisticcompany.service.client.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final ClientService clientService;

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public Shipment getShipmentById(long id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shipment id" + id));
    }

    @Override
    public void saveShipment(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    @Override
    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }

    @Override
    public List<Shipment> getShipmentsByClient(Client client) {
        return shipmentRepository.findBySenderOrReceiver(client, client);
    }

    @Override
    public List<Shipment> findShipmentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return shipmentRepository.findAllByCreatedAtBetween(
                startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

    @Override
    public BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate) {
        List<Shipment> shipments = findShipmentsBetweenDates(startDate, endDate);
        return shipments.stream()
                .map(shipment -> BigDecimal.valueOf(shipment.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}