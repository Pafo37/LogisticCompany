package com.logisticcompany.service.client;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.dto.UpdateClientDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;

import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Long id);

    void saveClient(Client client);

    Client getByUser(User user);

    Client findClientById(String id);

    Client getByUserKeycloakId(String keycloakId);

    void createClientFromRegistration(RegistrationDTO dto, User user);

    void updateClientAndKeycloak(Long id, UpdateClientDTO dto);

    void deleteClientAndKeycloak(Long id);

}