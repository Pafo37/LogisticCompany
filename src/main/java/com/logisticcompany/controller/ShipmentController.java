package com.logisticcompany.controller;

import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping("api/shipments")
    List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @GetMapping("api/shipments/{id}")
    Shipment getShipmentById(@PathVariable("id") long id) {
        return shipmentService.getShipmentById(id);
    }
}
