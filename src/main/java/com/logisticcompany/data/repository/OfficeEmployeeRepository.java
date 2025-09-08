package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Office;
import com.logisticcompany.data.entity.OfficeEmployee;
import com.logisticcompany.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfficeEmployeeRepository extends JpaRepository<OfficeEmployee, Long> {

    Optional<OfficeEmployee> findByUser(User user);

    Optional<OfficeEmployee> findByUser_KeycloakId(String keycloakId);

    List<OfficeEmployee> findByOffice(Office office);
}