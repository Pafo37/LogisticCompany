package com.logisticcompany.service.employee;

import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.EmployeeRepository;
import com.logisticcompany.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee id" + id));
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> findEmployeesByRole(String role) {
        return employeeRepository.findByRole(role);
    }

    @Override
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    @Override
    public Employee findByUser(User user) {
        return employeeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found for user: " + user.getUsername()));
    }

    @Override
    public Employee getByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return employeeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found for user: " + username));
    }
}