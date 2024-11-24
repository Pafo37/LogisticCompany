package com.logisticcompany.service.company;

import com.logisticcompany.data.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompanies();

    Company getCompany(long id);
}
