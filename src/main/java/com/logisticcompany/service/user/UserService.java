package com.logisticcompany.service.user;

import com.logisticcompany.data.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User saveUser(User user);

    User findByUsername(String username);
}
