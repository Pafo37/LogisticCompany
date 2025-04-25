package com.logisticcompany.service.employee;

import com.logisticcompany.data.dto.EmployeeDTO;
import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.User;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployeesDTOs();

    EmployeeDTO getEmployeeById(long id);

    EmployeeDTO saveEmployee(EmployeeDTO employee);

    void deleteEmployee(Long id);

    EmployeeDTO findByUser(User user);

    EmployeeDTO getByUser(String username);

    Employee findEntityByUser(User user);

    Employee findEntityByUsername(String username);


}
