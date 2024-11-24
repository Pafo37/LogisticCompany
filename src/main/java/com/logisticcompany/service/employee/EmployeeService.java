package com.logisticcompany.service.employee;

import com.logisticcompany.data.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(long id);

}
