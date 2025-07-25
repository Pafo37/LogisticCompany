package com.logisticcompany.controller;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.employee.EmployeeService;
import com.logisticcompany.service.keycloak.KeyCloakService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private KeyCloakService keyCloakService;
    private ClientService clientService;
    private EmployeeService employeeService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegistrationDTO dto, Model model) {
        try {
            String keycloakUserId = keyCloakService.registerUser(
                    dto.getUsername(),
                    dto.getPassword(),
                    dto.getEmail(),
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getRole()
            );
            dto.setKeycloakUserId(keycloakUserId);

            if ("ROLE_CLIENT".equals(dto.getRole())) {
                clientService.createClientFromRegistration(dto);
            } else if ("ROLE_EMPLOYEE".equals(dto.getRole())) {
                employeeService.createEmployeeFromRegistration(dto);
            }

            return "redirect:/oauth2/authorization/keycloak";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}