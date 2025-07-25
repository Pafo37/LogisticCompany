package com.logisticcompany.controller;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.employee.EmployeeService;
import com.logisticcompany.service.keycloak.KeyCloakService;
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
    private KeyCloakService keyCloakService;
    private ClientService clientService;
    private EmployeeService employeeService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationDTO") RegistrationDTO dto, Model model) {
        try {
            // Register the user in Keycloak
            keyCloakService.registerUserInKeycloak(dto);

            // Sync user locally (e.g., as Client or Employee)
            if ("ROLE_CLIENT".equalsIgnoreCase(dto.getRole())) {
                clientService.createClientFromRegistration(dto);
            } else if ("ROLE_EMPLOYEE".equalsIgnoreCase(dto.getRole())) {
                employeeService.createEmployeeFromRegistration(dto);
            }

            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("registrationDTO", dto);
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}