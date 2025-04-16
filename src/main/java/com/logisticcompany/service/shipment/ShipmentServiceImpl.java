package com.logisticcompany.service.shipment;

import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.data.repository.ShipmentRepository;
import com.logisticcompany.service.client.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<Shipment> getShipmentsDeliveredToOffice() {
        return shipmentRepository.findByDeliveredToOfficeTrue();
    }

    @Override
    public List<Shipment> getShipmentsDeliveredToAddress() {
        return shipmentRepository.findByDeliveredToOfficeFalse();
    }

    @Override
    public List<Shipment> getShipmentsByWeightGreaterThan(double weight) {
        return shipmentRepository.findByWeightGreaterThan(weight);
    }

    @Override
    public List<Shipment> searchShipmentsBySenderName(String senderName) {
        return shipmentRepository.findBySenderNameContaining(senderName);
    }
}