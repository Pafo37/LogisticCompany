package com.logisticcompany.service.user;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.User;

public interface UserService {

    User findByUsername(String username);

    User createUser(RegistrationDTO dto);
}
