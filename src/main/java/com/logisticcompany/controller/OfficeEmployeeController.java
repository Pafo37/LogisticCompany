package com.logisticcompany.controller;

import com.logisticcompany.repository.CourierRepository;
import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/office-employees")
public class OfficeEmployeeController {

    private final ShipmentService shipmentService;
    private final CourierRepository courierRepository;

    @GetMapping("/shipments")
    public String all(Model model) {
        model.addAttribute("shipments", shipmentService.getAllShipments());
        model.addAttribute("couriers", courierRepository.findAll());
        return "shipments";
    }

    @PostMapping("/shipments/{id}/assign")
    public String assign(@PathVariable Long id,
                         @RequestParam Long courierId,
                         Principal principal,
                         Model model) {
        try {
            shipmentService.assignCourier(id, courierId, principal);
            return "redirect:/office-employees/shipments";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("shipments", shipmentService.getAllShipments());
            model.addAttribute("couriers", courierRepository.findAll());
            return "shipments";
        }
    }

}
