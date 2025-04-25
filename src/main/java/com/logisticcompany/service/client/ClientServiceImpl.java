package com.logisticcompany.service.client;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ClientRepository;
import com.logisticcompany.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
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
    public Client getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return clientRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Client not found for user: " + username));
    }
}
