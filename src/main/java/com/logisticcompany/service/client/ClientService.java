package com.logisticcompany.service.client;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;

import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Long id);

    void saveClient(Client client);

    void deleteClient(Long id);

    Client getByUser(User user);

    Client findClientById(String id);

    void createClientFromRegistration(RegistrationDTO dto, User user);

}
