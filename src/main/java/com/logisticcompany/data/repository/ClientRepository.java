package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUser(User user);

        Optional<Client> findByUser_KeycloakId(String keycloakId);

}
