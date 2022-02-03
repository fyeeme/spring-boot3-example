package com.lorraine.jpa.config;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager",
        basePackages = {"com.lorraine.jpa.secondary"}
)
public class SecondaryConfig {
    private final DataSource secondaryDataSource;

    private final JpaProperties jpaProperties;

    private final HibernateProperties hibernateProperties;

    public SecondaryConfig(@Qualifier("secondaryDataSource") DataSource secondaryDataSource, JpaProperties jpaProperties, HibernateProperties hibernateProperties) {
        this.secondaryDataSource = secondaryDataSource;
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
    }

    private Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(secondaryDataSource)
                .packages("com.lorraine.jpa.secondary")
                .persistenceUnit("secondaryPersistenceUnit")
                .properties(getVendorProperties())
                .build();
    }

    @Bean(name = "secondaryEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return secondaryEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "secondaryTransactionManager")
    PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(secondaryEntityManagerFactory(builder).getObject());
    }
}
