package com.logisticcompany.data.repository;

import com.logisticcompany.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
