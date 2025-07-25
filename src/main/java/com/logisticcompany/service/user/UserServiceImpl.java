package com.logisticcompany.service.user;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Employee;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ClientRepository;
import com.logisticcompany.data.repository.EmployeeRepository;
import com.logisticcompany.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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