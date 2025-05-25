package com.tien.tenant.repository;

import com.tien.tenant.entity.Tenant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Tenant t SET t.tenantId = :tenantId, t.schemaName = :schemaName WHERE t.id = :id")
    void updateTenantIdAndSchema(@Param("tenantId") String tenantId,
                                 @Param("schemaName") String schemaName,
                                 @Param("id") Long id);
}
