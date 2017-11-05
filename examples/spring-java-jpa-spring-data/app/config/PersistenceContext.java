package config;

import java.util.Properties;
import model.AbstractEntity;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("repositories")
public class PersistenceContext {

  @Bean(destroyMethod = "close")
  DataSource dataSource(Config config) {
    Config dbConfig = config.getConfig("db.default");
    HikariConfig dataSourceConfig = new HikariConfig();
    dataSourceConfig.setDriverClassName(dbConfig.getString("driver"));
    dataSourceConfig.setJdbcUrl(dbConfig.getString("url"));
    dataSourceConfig.setUsername(dbConfig.getString("username"));
    dataSourceConfig.setPassword(dbConfig.getString("password"));
    return new HikariDataSource(dataSourceConfig);
  }

  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Config config) {
    Config dbConfig = config.getConfig("db.default");

    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactoryBean.setPackagesToScan(ClassUtils.getPackageName(AbstractEntity.class));

    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.dialect", dbConfig.getString("hibernate.dialect"));
    jpaProperties.put("hibernate.hbm2ddl.auto", dbConfig.getString("hibernate.hbm2ddl.auto"));
    jpaProperties.put("hibernate.show_sql", dbConfig.getString("hibernate.show_sql"));
    jpaProperties.put("hibernate.format_sql", dbConfig.getString("hibernate.format_sql"));
    jpaProperties.put("hibernate.connection.autocommit", dbConfig.getString("hibernate.connection.autocommit"));
    entityManagerFactoryBean.setJpaProperties(jpaProperties);

    entityManagerFactoryBean.setPersistenceUnitName(config.getString("jpa.default"));

    return entityManagerFactoryBean;
  }

  @Bean
  JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }
}