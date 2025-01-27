package com.logisticcompany.controller;

import com.logisticcompany.data.entity.Office;
import com.logisticcompany.service.office.OfficeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class OfficeController {

    private OfficeService officeService;

    @GetMapping(value = "api/offices")
    public List<Office> getAllOffices() {
        return officeService.getAllOffices();
    }

    @GetMapping("api/offices/{id}")
    public Office getOfficeById(@PathVariable Long id) {
        return officeService.getOfficeById(id);
    }

}