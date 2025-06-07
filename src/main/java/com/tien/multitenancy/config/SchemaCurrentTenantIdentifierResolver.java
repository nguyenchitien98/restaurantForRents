package com.tien.multitenancy.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class SchemaCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    private static final String DEFAULT_TENANT_ID = "central_db"; // fallback schema

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenant();
        System.out.println("Resolve tenant: " + tenantId + " on thread " + Thread.currentThread().getId());
        return (tenantId != null) ? tenantId : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
