package com.logisticcompany.repository;

import com.logisticcompany.data.entity.OfficeEmployee;
import com.logisticcompany.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeEmployeeRepository extends JpaRepository<OfficeEmployee, Long> {

    Optional<OfficeEmployee> findByUser(User user);

}