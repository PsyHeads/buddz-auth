package com.buddz.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.buddz.auth.model.BuddzUser;

public interface BuddzUserRepository extends JpaRepository<BuddzUser, Long>{

}
