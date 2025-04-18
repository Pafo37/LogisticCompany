package com.logisticcompany.service.client;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

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
    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByNameContaining(name);
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public List<Client> findClientsByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    @Override
    public Client getByUser(User user) {
        return clientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Client not found for user: " + user.getUsername()));
    }
}
