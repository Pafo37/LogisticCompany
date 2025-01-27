package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {


}
