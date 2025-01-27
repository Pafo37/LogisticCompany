package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    List<Office> findByNameContaining(String name);

    List<Office> findByAddressContaining(String address);
}
