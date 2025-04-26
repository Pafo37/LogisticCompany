package com.logisticcompany.service.employee;

import com.logisticcompany.data.dto.EmployeeDTO;
import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.EmployeeRepository;
import com.logisticcompany.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    @Override
    public List<EmployeeDTO> getAllEmployeesDTOs() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public EmployeeDTO getEmployeeById(long id) {
        return mapToDTO(employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found")));
    }

    @Override
    @Transactional
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employeeEntity = mapToEntity(employeeDTO);
        Employee saved = employeeRepository.save(employeeEntity);
        return mapToDTO(saved);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found for id: " + id));

        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setRole(employeeDTO.getRole());

        Employee updated = employeeRepository.save(employee);
        return mapToDTO(updated);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee findEntityByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return employeeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found for user: " + username));
    }

    //TODO: Extract to a mapper class
    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setRole(employee.getRole());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        return dto;
    }

    private Employee mapToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        return employee;
    }
}