package com.logisticcompany.service.employee;

import com.logisticcompany.data.dto.EmployeeDTO;
import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.User;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployeesDTOs();

    EmployeeDTO getEmployeeById(long id);

    EmployeeDTO saveEmployee(EmployeeDTO employee);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);

    Employee findEntityByUsername(String username);

}
