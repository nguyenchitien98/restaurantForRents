package com.tien.multitenancy.config;

public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static void setTenant(String tenantId) {
        System.out.println("Set tenant " + tenantId + " on thread " + Thread.currentThread().getId());
        currentTenant.set(tenantId);
    }

    public static String getTenant() {
        String tenant = currentTenant.get();
        System.out.println("Get tenant " + tenant + " on thread " + Thread.currentThread().getId());
        return tenant;
    }

    public static void clear() {
        currentTenant.remove();
    }
}
