package com.logisticcompany.service.user;

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
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Fetch and assign role entity
        String roleName = user.getRole(); // e.g., "ROLE_CLIENT" or "ROLE_EMPLOYEE"
        Role role = roleRepository.findByAuthority(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setAuthority(roleName);
                    return roleRepository.save(newRole);
                });

        user.setAuthorities(Set.of(role));

        // Security flags
        Client client = new Client();
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);

        // Save user first (so it gets an ID)
        User savedUser = userRepository.save(user);

        // Optional: create linked domain entity
        if ("ROLE_CLIENT".equals(roleName)) {
            client.setName(user.getUsername());
            client.setUser(savedUser);
            clientRepository.save(client);
        } else if ("ROLE_EMPLOYEE".equals(roleName)) {
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

}
