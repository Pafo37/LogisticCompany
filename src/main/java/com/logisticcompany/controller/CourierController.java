package com.logisticcompany.controller;

import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/courier")
public class CourierController {

    private final ShipmentService shipmentService;

    @GetMapping("/shipments")
    public String myShipments(Model model, Principal principal) {
        model.addAttribute("shipments", shipmentService.getShipmentsForCourier(principal));
        return "shipments";
    }

    @PostMapping("/shipments/{id}/deliver")
    public String deliver(@PathVariable Long id, Principal principal, Model model) {
        try {
            shipmentService.markDelivered(id, principal);
            return "redirect:/courier/shipments";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("shipments", shipmentService.getShipmentsForCourier(principal));
            return "shipments";
        }
    }
}