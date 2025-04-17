package com.logisticcompany.controller;

import com.logisticcompany.data.entity.User;
import com.logisticcompany.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.saveUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }
}