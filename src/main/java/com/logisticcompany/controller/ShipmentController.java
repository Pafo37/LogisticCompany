package com.logisticcompany.controller;

import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.*;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.employee.EmployeeService;
import com.logisticcompany.service.office.OfficeService;
import com.logisticcompany.service.shipment.ShipmentService;
import com.logisticcompany.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private ShipmentService shipmentService;

    private ClientService clientService;

    private OfficeService officeService;

    private UserService userService;

    private EmployeeService employeeService;

    @GetMapping
    public String getAllShipments(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        boolean isEmployee = user.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        if (isEmployee) {
            model.addAttribute("shipments", shipmentService.getAllShipments());
        } else {
            Client client = clientService.getByUser(user);
            model.addAttribute("shipments", shipmentService.getShipmentsByClient(client));
        }

        return "shipments";
    }

    @GetMapping("/add")
    public String showAddShipmentForm(Model model) {
        model.addAttribute("shipment", new ShipmentDTO());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("offices", officeService.getAllOffices());
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

        Shipment shipment = new Shipment();
        shipment.setDeliveryAddress(shipmentDTO.getDeliveryAddress());
        shipment.setWeight(shipmentDTO.getWeight());
        shipment.setDeliveredToOffice(shipmentDTO.isDeliveredToOffice());

        Client sender = clientService.getClientById(shipmentDTO.getSenderId());
        Client receiver = clientService.getClientById(shipmentDTO.getReceiverId());
        shipment.setSender(sender);
        shipment.setReceiver(receiver);

        // Fetch Office only if deliveredToOffice = true
        if (shipmentDTO.isDeliveredToOffice() && shipmentDTO.getDeliveryOfficeId() != null) {
            Office office = officeService.getOfficeById(shipmentDTO.getDeliveryOfficeId());
            shipment.setDeliveryOffice(office);
        }

        // Price calculation (if you have it)
        double price = calculatePrice(shipment.getWeight(), shipment.isDeliveredToOffice());
        shipment.setPrice(price);

        shipment.setRegisteredBy(getEmployeeFromPrincipal(principal));
        shipmentService.saveShipment(shipment);
        return "redirect:/shipments";
    }



    @GetMapping("/edit/{id}")
    public String showEditShipmentForm(@PathVariable Long id, Model model) {
        Shipment shipment = shipmentService.getShipmentById(id);

        ShipmentDTO shipmentDTO = new ShipmentDTO();
        shipmentDTO.setSenderId(shipment.getSender().getId());
        shipmentDTO.setReceiverId(shipment.getReceiver().getId());
        shipmentDTO.setDeliveryAddress(shipment.getDeliveryAddress());
        shipmentDTO.setWeight(shipment.getWeight());
        shipmentDTO.setDeliveredToOffice(shipment.isDeliveredToOffice());

        model.addAttribute("shipment", shipmentDTO);
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("shipmentId", id); // used in form action
        model.addAttribute("offices", officeService.getAllOffices());
        return "edit_shipment";
    }

    @PostMapping("/edit/{id}")
    public String editShipment(@PathVariable Long id, @ModelAttribute ShipmentDTO shipmentDTO, Principal principal) {
        Shipment shipment = shipmentService.getShipmentById(id);

        Client sender = clientService.getClientById(shipmentDTO.getSenderId());
        Client receiver = clientService.getClientById(shipmentDTO.getReceiverId());

        shipment.setSender(sender);
        shipment.setReceiver(receiver);
        shipment.setDeliveryAddress(shipmentDTO.getDeliveryAddress());
        shipment.setWeight(shipmentDTO.getWeight());
        shipment.setDeliveredToOffice(shipmentDTO.isDeliveredToOffice());

        double price = calculatePrice(shipment.getWeight(), shipment.isDeliveredToOffice());
        shipment.setPrice(price);
        shipment.setRegisteredBy(getEmployeeFromPrincipal(principal));

        shipmentService.saveShipment(shipment);
        return "redirect:/shipments";
    }

    @GetMapping("/delete/{id}")
    public String deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return "redirect:/shipments";
    }

    private double calculatePrice(double weight, boolean deliveredToOffice) {
        double baseRatePerKg = 0.5;
        double deliveryFee = deliveredToOffice ? 0.0 : 5.0;
        return (weight * baseRatePerKg) + deliveryFee;
    }

    private Employee getEmployeeFromPrincipal(Principal principal) {
        String username = principal.getName(); // comes from Spring Security
        User user = userService.findByUsername(username);
        return employeeService.findByUser(user);
    }

}