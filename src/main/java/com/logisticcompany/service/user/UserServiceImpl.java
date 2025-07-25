package com.logisticcompany.service.user;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.Role;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ClientRepository;
import com.logisticcompany.data.repository.EmployeeRepository;
import com.logisticcompany.data.repository.RoleRepository;
import com.logisticcompany.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public User saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Fetch or create role
        Role role = roleRepository.findByAuthority(user.getRole())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setAuthority(user.getRole());
                    return roleRepository.save(newRole);
                });

        user.setAuthorities(Set.of(role));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);

        User savedUser = userRepository.save(user);

        // Optional: create Client/Employee
        if ("ROLE_CLIENT".equals(user.getRole())) {
            Client client = new Client();
            client.setName(user.getUsername());
            client.setUser(savedUser);
            clientRepository.save(client);
        } else if ("ROLE_EMPLOYEE".equals(user.getRole())) {
            Employee employee = new Employee();
            employee.setName(user.getUsername());
            employee.setUser(savedUser);
            employeeRepository.save(employee);
        }

        return savedUser;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public User createUser(RegistrationDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setKeycloakId(dto.getKeycloakUserId());
        return userRepository.save(user);
    }

}
