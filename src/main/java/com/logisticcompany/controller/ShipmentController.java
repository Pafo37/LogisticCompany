package com.logisticcompany.controller;

import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.office.OfficeService;
import com.logisticcompany.service.shipment.ShipmentService;
import com.logisticcompany.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private ShipmentService shipmentService;

    private ClientService clientService;

    private OfficeService officeService;

    private UserService userService;

    @GetMapping
    public String getAllShipments(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        //TODO: you need to get the role
        boolean isEmployee = false;

        if (isEmployee) {
            model.addAttribute("shipments", shipmentService.getAllShipments());
        } else {
            Client client = clientService.getByUser(user);
            model.addAttribute("shipments", shipmentService.getShipmentsByClient(client));
        }

        return "shipments";
    }

    @GetMapping("/add")
    public String showAddShipmentForm(Model model, Principal principal) {
        Client currentClient = clientService.findEntityByUsername(principal.getName());

        List<Client> allClients = clientService.getAllClients();
        List<Client> filteredClients = allClients.stream()
                .filter(client -> !Objects.equals(client.getId(), currentClient.getId()))
                .toList();

        model.addAttribute("shipment", new ShipmentDTO());
        model.addAttribute("clients", filteredClients);
        model.addAttribute("offices", officeService.getAllOffices());
        model.addAttribute("currentClient", currentClient);

        return "add_shipment";
    }

    @PostMapping("/add")
    public String addShipment(@ModelAttribute("shipment") ShipmentDTO shipmentDTO, Principal principal, Model model) {

        if ((shipmentDTO.isDeliveredToOffice() && shipmentDTO.getDeliveryOfficeId() == null)
                || (!shipmentDTO.isDeliveredToOffice() && (shipmentDTO.getDeliveryAddress() == null || shipmentDTO.getDeliveryAddress().isBlank()))) {

            model.addAttribute("errorMessage", "You must either select a delivery office or provide a delivery address.");
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("clients", clientService.getAllClients());
            return "add_shipment";
        }

        shipmentService.saveShipment(shipmentDTO, principal);
        return "redirect:/shipments";
    }


    @GetMapping("/edit/{id}")
    public String showEditShipmentForm(@PathVariable Long id, Model model) {
        model.addAttribute("shipment", shipmentService.getShipmentById(id));
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("offices", officeService.getAllOffices());
        model.addAttribute("shipmentId", id);
        return "edit_shipment";
    }

    @PostMapping("/edit/{id}")
    public String editShipment(@PathVariable Long id, @ModelAttribute("shipment") ShipmentDTO shipmentDTO, Principal principal, Model model) {
        if ((shipmentDTO.isDeliveredToOffice() && shipmentDTO.getDeliveryOfficeId() == null)
                || (!shipmentDTO.isDeliveredToOffice() && (shipmentDTO.getDeliveryAddress() == null || shipmentDTO.getDeliveryAddress().isBlank()))) {
            model.addAttribute("errorMessage", "You must either select a delivery office or provide a delivery address.");
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("shipmentId", id);
            return "edit_shipment";
        }

        shipmentService.updateShipmentFromDTO(id, shipmentDTO, principal);
        return "redirect:/shipments";
    }


    @GetMapping("/delete/{id}")
    public String deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return "redirect:/shipments";
    }

}