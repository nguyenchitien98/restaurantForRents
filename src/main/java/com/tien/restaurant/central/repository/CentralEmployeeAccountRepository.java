package com.tien.restaurant.central.repository;

import com.tien.restaurant.central.entity.EmployeeAccount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class CentralEmployeeAccountRepository {
    @PersistenceContext(unitName = "central") // ✅ phải trùng với .persistenceUnit("central")
    private EntityManager centralEntityManager;

    @Transactional(transactionManager = "centralTransactionManager")
    public void save(EmployeeAccount account) {
        centralEntityManager.persist(account);
    }

    public Optional<EmployeeAccount> findByEmail(String email) {
        try {
            EmployeeAccount account = centralEntityManager.createQuery(
                            "SELECT e FROM EmployeeAccount e WHERE e.email = :email", EmployeeAccount.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(account);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
