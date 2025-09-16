package com.logisticcompany.service.officeemployee;

import com.logisticcompany.data.dto.OfficeEmployeeDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.OfficeEmployee;
import com.logisticcompany.data.entity.User;

import java.util.List;

public interface OfficeEmployeeService {

    OfficeEmployeeDTO getById(Long id);

    List<OfficeEmployeeDTO> getAll();

    OfficeEmployee createFromRegistration(RegistrationDTO dto, User user);

    OfficeEmployee getByUser(User user);

}
