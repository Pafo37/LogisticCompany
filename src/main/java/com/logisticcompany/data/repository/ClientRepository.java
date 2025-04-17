package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findByNameContaining(String name);

    List<Client> findByPhone(String phone);

    List<Client> findByAddressContaining(String address);

    Optional<Client> findByUser(User user);
}
