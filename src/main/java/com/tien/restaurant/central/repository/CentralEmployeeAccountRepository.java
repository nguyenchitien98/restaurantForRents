package com.tien.restaurant.central.repository;

import com.tien.restaurant.central.entity.EmployeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// Central repository (dùng central datasource)
@Repository
@Transactional(transactionManager = "centralTransactionManager")
public interface CentralEmployeeAccountRepository extends JpaRepository<EmployeeAccount, Long> {
    Optional<EmployeeAccount> findByEmail(String email);
}
