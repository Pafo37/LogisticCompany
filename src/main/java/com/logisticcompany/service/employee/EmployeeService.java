package com.logisticcompany.service.employee;

import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.User;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(long id);

    Employee saveEmployee(Employee employee);

    void deleteEmployee(Long id);

    List<Employee> findEmployeesByRole(String role);

    List<Employee> searchEmployeesByName(String name);

    Employee findByUser(User user);
}
