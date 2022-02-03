package com.lorraine.jpa.config;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager",
        basePackages = {"com.lorraine.jpa.primary"}
)
@Slf4j
public class PrimaryConfig {

    private final DataSource primaryDataSource;

    private final JpaProperties jpaProperties;

    private final HibernateProperties hibernateProperties;

    public PrimaryConfig(@Qualifier("primaryDataSource") DataSource primaryDataSource, JpaProperties jpaProperties, HibernateProperties hibernateProperties) {
        this.primaryDataSource = primaryDataSource;
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
    }

    private Map<String, Object> getVendorProperties() {
        var properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
                new HibernateSettings());

        properties.put("hibernate.session_factory.interceptor", hibernateInterceptor());
        return properties;
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(primaryDataSource)
                .packages("com.lorraine.jpa.primary")
                .persistenceUnit("primaryPersistenceUnit")
                .properties(getVendorProperties())
                .build();
    }

    @Primary
    @Bean(name = "primaryEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return Objects.requireNonNull(primaryEntityManagerFactory(builder)
                        .getObject())
                .createEntityManager();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(primaryEntityManagerFactory(builder).getObject()));
    }

    @Bean
    public EmptyInterceptor hibernateInterceptor() {
        return new MyEmptyInterceptor();
    }

    /**
     * hibernate hooks
     */
    private static class MyEmptyInterceptor extends EmptyInterceptor {
        @Serial
        private static final long serialVersionUID = 3412086629961782369L;

        @Override
        public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
            log.info("{}", "Interceptor delete");
        }

        @Override
        public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
            log.info("{}", "Interceptor flushDirty");
            return false;
        }

        @Override
        public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
            log.info("{}", "Interceptor load");
            return false;
        }

        @Override
        public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
            log.info("{}", "Interceptor save");
            return false;
        }
    }
}
