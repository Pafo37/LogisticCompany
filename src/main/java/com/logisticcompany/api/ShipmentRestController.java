package com.logisticcompany.api;

import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@AllArgsConstructor
public class ShipmentRestController {

    private final ShipmentService shipmentService;

    @GetMapping
    public List<ShipmentDTO> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @GetMapping("/{id}")
    public ShipmentDTO getShipmentById(@PathVariable Long id) {
        return shipmentService.getShipmentById(id);
    }

    @PostMapping
    public Shipment createShipment(@RequestBody ShipmentDTO dto, Principal principal) {
        return shipmentService.createShipmentFromDTO(dto, principal);
    }

    @PutMapping("/{id}")
    public ShipmentDTO updateShipment(@PathVariable Long id, @RequestBody ShipmentDTO dto, Principal principal) {
        return shipmentService.updateShipmentFromDTO(id, dto, principal);
    }

    @DeleteMapping("/{id}")
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }

    @GetMapping("/client")
    public List<Shipment> getShipmentsForLoggedInClient(Principal principal) {
        return shipmentService.getShipmentsForClient(principal.getName());
    }
}