package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUser(User user);

}