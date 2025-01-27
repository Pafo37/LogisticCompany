package com.logisticcompany.controller;

import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private ShipmentService shipmentService;

    private ClientService clientService;

    @GetMapping
    public String getAllShipments(Model model) {
        model.addAttribute("shipments", shipmentService.getAllShipments());
        return "shipments";
    }

    @GetMapping("/add")
    public String showAddShipmentForm(Model model) {
        model.addAttribute("shipment", new Shipment());
        model.addAttribute("clients", clientService.getAllClients());
        return "add_shipment";
    }

    @PostMapping("/add")
    public String addShipment(@ModelAttribute Shipment shipment) {
        shipmentService.saveShipment(shipment);
        return "redirect:/shipments";
    }

    @GetMapping("/edit/{id}")
    public String showEditShipmentForm(@PathVariable Long id, Model model) {
        model.addAttribute("shipment", shipmentService.getShipmentById(id));
        model.addAttribute("clients", clientService.getAllClients());
        return "edit_shipment";
    }

    @PostMapping("/edit/{id}")
    public String ediShipment(@PathVariable Long id, @ModelAttribute Shipment shipment) {
        shipment.setId(id);
        shipmentService.saveShipment(shipment);
        return "redirect:/shipments";
    }

    @GetMapping("/delete/{id}")
    public String deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return "redirect:/shipments";
    }
}