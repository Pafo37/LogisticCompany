package com.logisticcompany.controller;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.courier.CourierService;
import com.logisticcompany.service.keycloak.KeyCloakService;
import com.logisticcompany.service.officeemployee.OfficeEmployeeService;
import com.logisticcompany.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private KeyCloakService keyCloakService;
    private ClientService clientService;
    private final OfficeEmployeeService officeEmployeeService;
    private final CourierService courierService;
    private final UserService userService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegistrationDTO dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            String keycloakUserId = keyCloakService.registerUser(
                    dto.getUsername(),
                    dto.getPassword(),
                    dto.getEmail(),
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getRole()
            );

            User user = userService.createFromRegistration(dto, keycloakUserId);

            switch (dto.getRole()) {
                case "ROLE_CLIENT" -> clientService.createClientFromRegistration(dto, user);
                case "ROLE_OFFICE_EMPLOYEE" -> officeEmployeeService.createFromRegistration(dto, user);
                case "ROLE_COURIER" -> courierService.createFromRegistration(dto, user);
                default -> {
                    model.addAttribute("errorMessage", "Invalid role selected.");
                    return "register";
                }
            }

            return "redirect:/oauth2/authorization/keycloak";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}