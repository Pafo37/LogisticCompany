package com.logisticcompany.service.shipment;

import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.data.repository.ShipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public Shipment getShipmentById(long id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shipment id" + id));
    }
}
