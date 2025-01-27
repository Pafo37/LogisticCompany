package com.logisticcompany.service.client;

import com.logisticcompany.data.entity.Client;

import java.util.List;

public interface ClientService {

   List<Client> getAllClients();

   Client getClientById(Long id);

   void saveClient(Client client);

   void deleteClient(Client client);
}
