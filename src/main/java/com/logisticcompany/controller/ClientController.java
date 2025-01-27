package com.logisticcompany.controller;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.service.client.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ClientController {

    ClientService clientService;

    @GetMapping(value = "api/clients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping(value = "api/clients/{id}")
    public Client getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

}