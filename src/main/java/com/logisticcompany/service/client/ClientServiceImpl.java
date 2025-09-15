package com.logisticcompany.service.client;

import com.logisticcompany.data.dto.ClientDTO;
import com.logisticcompany.data.dto.ClientUpdateDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ClientRepository;
import com.logisticcompany.data.repository.UserRepository;
import com.logisticcompany.service.keycloak.KeyCloakService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private KeyCloakService keyCloakService;
    private UserRepository userRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client getByUser(User user) {
        return clientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Client not found for user: " + user.getUsername()));
    }

    @Override
    public Client getByUserKeycloakId(String keycloakId) {
        return clientRepository.findByUser_KeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found for keycloakId: " + keycloakId));
    }

    @Override
    public Client findClientById(String id) {
        return clientRepository.findByUser_KeycloakId(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found for user id: " + id));
    }

    @Override
    public void createClientFromRegistration(RegistrationDTO dto, User user) {
        Client client = new Client();
        client.setUser(user);
        client.setName(dto.getFirstName() + " " + dto.getLastName());
        client.setEmail(dto.getEmail());
        clientRepository.save(client);
    }

    @Override
    public void updateClientAndKeycloak(Long id, ClientUpdateDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));

        User user = client.getUser();
        if (user == null) {
            throw new IllegalStateException("Client " + id + " has no linked user");
        }

        if (dto.getEmail() != null) client.setEmail(dto.getEmail());
        if (dto.getName() != null) client.setName(dto.getName());
        clientRepository.save(client);

        keyCloakService.updateUser(
                user.getKeycloakId(),
                dto.getEmail(),
                dto.getName(),
                dto.getUsername()
        );
    }

    @Override
    public void deleteClientAndKeycloak(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));

        User user = client.getUser();
        if (user == null) {
            throw new IllegalStateException("Client " + id + " has no linked user");
        }

        keyCloakService.deleteUser(user.getKeycloakId());

        clientRepository.delete(client);
        userRepository.delete(user);
    }

}