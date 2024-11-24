package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {


}
