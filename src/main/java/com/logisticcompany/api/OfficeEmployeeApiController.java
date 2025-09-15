package com.logisticcompany.api;

import com.logisticcompany.data.entity.OfficeEmployee;
import com.logisticcompany.service.officeemployee.OfficeEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/office-employees")
public class OfficeEmployeeApiController {

    private final OfficeEmployeeService officeEmployeeService;

    @GetMapping
    public List<OfficeEmployee> getAllOfficeEmployees() {
        return officeEmployeeService.getAll();
    }

    @GetMapping("/{id}")
    public OfficeEmployee getOfficeEmployeeById(@PathVariable Long id) {
        return officeEmployeeService.getById(id);
    }
}
