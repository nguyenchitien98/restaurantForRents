package com.tien.restaurant.central.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.tien.restaurant.central.repository", // hoặc nơi chứa CentralEmployeeAccountRepository
        entityManagerFactoryRef = "centralEntityManagerFactory",
        transactionManagerRef = "centralTransactionManager"
)
public class CentralDataSourceConfig {

    @Bean(name = "centralDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.central")
    public DataSource centralDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "centralEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean centralEntityManagerFactory(
            @Qualifier("centralDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.tien.restaurant.central.entity"); // nơi chứa EmployeeAccount
        emf.setPersistenceUnitName("central");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        props.put("hibernate.show_sql", true);

        emf.setJpaPropertyMap(props);
        return emf;
    }

    @Bean(name = "centralTransactionManager")
    @Primary
    public PlatformTransactionManager centralTransactionManager(
            @Qualifier("centralEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }
}