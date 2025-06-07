package com.tien.multitenancy.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.tien.restaurant.repository","com.tien.tenant.repository"}, // nơi chứa tenant Repository
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager"
)
public class TenantDataSourceConfig {

    @Bean(name = "tenantDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tenant")
    public DataSource tenantDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SchemaMultiTenantConnectionProvider multiTenantConnectionProvider(@Qualifier("tenantDataSource")DataSource dataSource) {
        return new SchemaMultiTenantConnectionProvider(dataSource);
    }

    @Bean
    public SchemaCurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new SchemaCurrentTenantIdentifierResolver();
    }

    @Bean(name = "tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tenantDataSource") DataSource tenantDataSource,
            SchemaMultiTenantConnectionProvider multiTenantConnectionProvider,
            SchemaCurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.multiTenancy", "SCHEMA");
        props.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        props.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        props.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        props.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        props.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);

        return builder
                .dataSource(tenantDataSource)
                .packages("com.tien.restaurant.entity","com.tien.tenant.entity")// entity tenant schema
                .properties(props)
                .persistenceUnit("tenant")
                .build();
    }

    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManagerFactory) {
        return new JpaTransactionManager(tenantEntityManagerFactory);
    }
}
