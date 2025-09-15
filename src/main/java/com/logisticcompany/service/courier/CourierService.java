package com.logisticcompany.service.courier;

import com.logisticcompany.data.dto.CourierDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.User;

import java.util.List;

public interface CourierService {

    CourierDTO getById(Long id);

    List<CourierDTO> getAll();

    Courier createFromRegistration(RegistrationDTO dto, User user);

}