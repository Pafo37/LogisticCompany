package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}