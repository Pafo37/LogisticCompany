package com.logisticcompany.service.officeemployee;

import com.logisticcompany.data.dto.OfficeEmployeeDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.OfficeEmployee;
import com.logisticcompany.data.entity.User;

import java.util.List;
import java.util.Optional;

public interface OfficeEmployeeService {

    OfficeEmployeeDTO getById(Long id);

    List<OfficeEmployeeDTO> getAll();

    Optional<OfficeEmployee> findByUserKeycloakId(String keycloakId);

    OfficeEmployee getByUserKeycloakId(String keycloakId);

    OfficeEmployee createForUser(User user);

    OfficeEmployee createFromRegistration(RegistrationDTO dto, User user);

    OfficeEmployee getByUser(User user);

    OfficeEmployee createForUserKeycloakId(String keycloakId);

    void deleteById(Long id);
}
