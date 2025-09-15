package com.logisticcompany.api;

import com.logisticcompany.data.dto.ClientDTO;
import com.logisticcompany.data.dto.UpdateClientDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.service.client.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('OFFICE_EMPLOYEE')")
@RequestMapping("/api/clients")
public class ClientApiController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable Long id) {
        return mapToDTO(clientService.getClientById(id));
    }

    @PutMapping("/{id}")
    public void updateClient(@PathVariable Long id, @RequestBody UpdateClientDTO dto) {
        clientService.updateClientAndKeycloak(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClientAndKeycloak(id);
    }

    //TODO: extract to a class if you have the time
    private ClientDTO mapToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        if (client.getUser() != null) {
            dto.setUsername(client.getUser().getUsername());
        }
        dto.setEmail(client.getEmail());
        dto.setName(client.getName());
        return dto;
    }

}