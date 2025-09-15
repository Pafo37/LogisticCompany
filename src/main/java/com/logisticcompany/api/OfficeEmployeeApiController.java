package com.logisticcompany.api;

import com.logisticcompany.data.dto.OfficeEmployeeDTO;
import com.logisticcompany.service.officeemployee.OfficeEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('OFFICE_EMPLOYEE')")
@RequestMapping("/api/office-employees")
public class OfficeEmployeeApiController {

    private final OfficeEmployeeService officeEmployeeService;

    @GetMapping
    public List<OfficeEmployeeDTO> getAllOfficeEmployees() {
        return officeEmployeeService.getAll();
    }

    @GetMapping("/{id}")
    public OfficeEmployeeDTO getOfficeEmployeeById(@PathVariable Long id) {
        return officeEmployeeService.getById(id);
    }
}
