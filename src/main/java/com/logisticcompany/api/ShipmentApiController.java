package com.logisticcompany.api;

import com.logisticcompany.data.dto.CreateShipmentDTO;
import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@AllArgsConstructor
public class ShipmentApiController {

    private final ShipmentService shipmentService;

    @GetMapping
    @PreAuthorize("hasRole('OFFICE_EMPLOYEE')")
    public List<ShipmentDTO> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('CLIENT')")
    public List<ShipmentDTO> getMineShipments(Principal principal) {
        return shipmentService.getShipmentsForCurrentClient(principal);
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('COURIER')")
    public List<ShipmentDTO> getAssignedShipments(Principal principal) {
        return shipmentService.getShipmentsForCourier(principal);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ShipmentDTO> createShipment(@RequestBody CreateShipmentDTO createDTO, Principal principal) {
        ShipmentDTO shipment = shipmentService.createShipment(createDTO, principal);

        return ResponseEntity.ok(shipment);
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('OFFICE_EMPLOYEE')")
    public void assignShipment(@PathVariable Long id, @RequestParam Long courierId, Principal principal) {
        shipmentService.assignCourier(id, courierId, principal);
    }

    @PostMapping("/{id}/deliver")
    @PreAuthorize("hasRole('COURIER')")
    public void deliverShipment(@PathVariable Long id, Principal principal) {
        shipmentService.markDelivered(id, principal);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }
}