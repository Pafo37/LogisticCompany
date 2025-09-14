package com.logisticcompany.service.officeemployee;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.OfficeEmployee;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.OfficeEmployeeRepository;
import com.logisticcompany.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OfficeEmployeeServiceImpl implements OfficeEmployeeService {

    private final OfficeEmployeeRepository officeEmployeeRepository;

    private final UserRepository userRepository;

    @Override
    public OfficeEmployee getById(Long id) {
        return officeEmployeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OfficeEmployee not found: " + id));
    }

    @Override
    public List<OfficeEmployee> getAll() {
        return officeEmployeeRepository.findAll();
    }

    @Override
    public Optional<OfficeEmployee> findByUserKeycloakId(String keycloakId) {
        return officeEmployeeRepository.findByUser_KeycloakId(keycloakId);
    }

    @Override
    public OfficeEmployee getByUserKeycloakId(String keycloakId) {
        return officeEmployeeRepository.findByUser_KeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "OfficeEmployee profile not found for user sub: " + keycloakId));
    }

    @Override
    public OfficeEmployee createForUser(User user) {
        Optional<OfficeEmployee> existing = officeEmployeeRepository.findByUser(user);
        if (existing.isPresent()) {
            return existing.get(); // or throw new IllegalStateException("Profile already exists");
        }
        OfficeEmployee profile = new OfficeEmployee();
        profile.setUser(user);
        return officeEmployeeRepository.save(profile);
    }

    @Override
    public OfficeEmployee createFromRegistration(RegistrationDTO dto, User user) {
        OfficeEmployee oe = new OfficeEmployee();
        oe.setUser(user);
        oe.setFirstName(dto.getFirstName());
        oe.setLastName(dto.getLastName());
        oe.setEmail(dto.getEmail());
        return officeEmployeeRepository.save(oe);
    }

    @Override
    public OfficeEmployee createForUserKeycloakId(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("User not found by sub: " + keycloakId));
        return createForUser(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!officeEmployeeRepository.existsById(id)) {
            throw new EntityNotFoundException("OfficeEmployee not found: " + id);
        }
        officeEmployeeRepository.deleteById(id);
    }

    @Override
    public OfficeEmployee getByUser(User user) {
        return officeEmployeeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Office employee not found for user " + user.getUsername()));
    }
}