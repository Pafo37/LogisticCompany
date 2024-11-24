package com.logisticcompany.controller;

import com.logisticcompany.data.entity.User;
import com.logisticcompany.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "api/user/register")
    User registerUser(User user) {
        return userService.saveUser(user);
    }
}
