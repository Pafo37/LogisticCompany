package com.logisticcompany.service.client;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Long id);

    void saveClient(Client client);

    void deleteClient(Long id);

    Client getByUser(User user);

    List<Client> searchClientsByName(String name);

    Optional<Client> findClientByEmail(String email);

    List<Client> findClientsByPhone(String phone);
}
