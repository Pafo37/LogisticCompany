package com.logisticcompany.controller;

import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.service.employee.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(value = "api/employees")
    List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping(value = "api/employees/{id}")
    Employee getEmployeeById(@PathVariable("id") long id){
        return employeeService.getEmployeeById(id);
    }
}
