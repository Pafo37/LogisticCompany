package com.logisticcompany.service.company;

import com.logisticcompany.data.entity.Company;
import com.logisticcompany.data.repository.CompanyRepository;
import com.logisticcompany.service.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompany(long id) {
        return companyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid company id" + id));
    }
}
