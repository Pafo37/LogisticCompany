package com.logisticcompany.controller;

import com.logisticcompany.data.entity.Company;
import com.logisticcompany.service.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping(value = "api/companies")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping(value = "api/companies/{id}")
    public Company getCompany(@PathVariable("id") long id) {
        return companyService.getCompany(id);
    }
}