package config;

import java.util.Properties;

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

import model.AbstractEntity;
import play.db.DBApi;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("repositories")
public class PersistenceContext {

  @Bean
  DataSource dataSource(DBApi dbapi) {
    return dbapi.getDatabase("default").getDataSource();
  }

  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Config config) {
    Config hibernateConfig = config.getConfig("db.default.hibernate");

    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactoryBean.setPackagesToScan(ClassUtils.getPackageName(AbstractEntity.class));

    Properties jpaProperties = new Properties();
    hibernateConfig.entrySet().forEach(entry -> {
      jpaProperties.put("hibernate."+entry.getKey(), entry.getValue().unwrapped());
    });
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