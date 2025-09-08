package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Long> {

    Optional<Courier> findByUser(User user);

    Optional<Courier> findByUser_KeycloakId(String keycloakId);

    List<Courier> findAll();
}
