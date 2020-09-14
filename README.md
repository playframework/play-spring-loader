# End of Life

The active Playframework contributors consider this repository has reached End of Life and archived it.

This repository is not being used anymore and won't get any further updates.

Thank you to all contributors that worked on this repository!


# Play Spring Loader

This is an application loader for Play applications that runs with Spring as the DI. It binds and allows injecting all Play-provided components in addition to any components provided by third-party Play modules (defined as a `play.api.inject.Module`)

The current version targets Play 2.6.x and Spring 4.3.x. It may work but has not been tested on other versions.

The application loader was originally authored by Remi Thieblin based on the original proof of concept by James Roper. It is now being maintained by the Play team. There are currently no plans to add new features, but we're happy to accept contributions from the community. This project still needs tests and also a Java API for the SpringApplicationBuilder (though the Scala API can be used from Java).

## Setup Instructions

To use in your Play SBT project, add the dependency to your `build.sbt`:

```scala
libraryDependencies += "com.lightbend.play" %% "play-spring-loader" % "0.0.2"
```

Then configure the loader in your `application.conf`:

```
play.application.loader = "com.lightbend.play.spring.SpringApplicationLoader"
````

### For a Scala-based app

```sh
play.application.loader = "com.lightbend.play.spring.SpringApplicationLoader"

# This works assuming the class is a play.api.inject.Module
#play.modules.enabled += "com.demo.spring.MyModule"

play.spring.configs += "config.AppConfig"
```

with the following configuration class:

```scala
package config

import org.springframework.context.annotation.{ComponentScan, Configuration}

@Configuration
@ComponentScan(Array("com.demo.spring", "controllers"))
class AppConfig  {

}
```

### For a Java-based app

```sh
play.application.loader = "com.lightbend.play.spring.SpringApplicationLoader"

# This works assuming the class is a play.api.inject.Module
#play.modules.enabled += "com.demo.spring.MyModule"

play.spring.configs = ["com.example.PlaySpringDIConfiguration"]
```

with the following configuration class:

```java
package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class PlaySpringDIConfiguration {

}
```

## Migrating from Guice

If you want to migrate your existing project from `guice` you should follow these steps

### For app without JPA

1. Remove `guice` from your `libraryDependencies` in `build.sbt` file
2. Make sure `PlaySpringDIConfiguration` is placed in your root package or you have to name specific packages in `@ComponentScan` annotation
3. Annotate all of your controllers as `@Component` and services as `@Service`
4. Replace all `com.google.inject.Injector` with `org.springframework.context.ApplicationContext` and `injector.getInstance(MyClass.class)` with `context.getBean(MyClass.class)`

### For app with JPA

Besides all of the above steps you must:

1. Remove `javaJpa` (`jpa`) from your `libraryDependencies` in `build.sbt` file
2. Add `"org.springframework" % "spring-orm" % "4.3.12.RELEASE"`, `"org.springframework" % "spring-aop" % "4.3.12.RELEASE"` and
`"org.springframework" % "spring-expression" % "4.3.12.RELEASE"` to your `libraryDependencies` in `build.sbt` file
3. Place this class next to the `PlaySpringDIConfiguration` (or inside it):
```
@Configuration
@EnableTransactionManagement
// @EnableJpaRepositories //For SpringData
public class PersistenceContext {

  @Bean
  DataSource dataSource(play.db.DBApi dbapi) {
    return dbapi.getDatabase("default").getDataSource();
  }

  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Config config) {
    Config hibernateConfig = config.getConfig("db.default.hibernate");

    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactoryBean.setPackagesToScan("com.example.domain.model");

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
```
4. Add these properties from your `persisttence.xml` in `db.default` section of your `application.conf`:
```
db {
  default {
    [... your current db config ...]
    hibernate.dialect="org.hibernate.dialect.X"
    hibernate.hbm2ddl.auto="validate"
    hibernate.show_sql=false
    hibernate.format_sql=true
    hibernate.connection.autocommit=false
  }
```
5. Delete `persisttence.xml` file
6. Annotate all of your repositories as `@Repository`
7. Replace all `@Inject JPAApi jpaApi` with `@PersistenceContext EntityManager entityManager` and `jpaApi.em()` with `entityManager`
8. Replace all `play.db.jpa.Transactional` with `javax.transaction.Transactional`

If you want to use SpringData, replace `"org.springframework" % "spring-orm" % "4.3.12.RELEASE"`, `"org.springframework" % "spring-aop" % "4.3.12.RELEASE"` and `"org.springframework" % "spring-expression" % "4.3.12.RELEASE"` with `"org.springframework.data" % "spring-data-jpa" % "1.11.8.RELEASE"` and uncomment `@EnableJpaRepositories` over `PersistenceContext` class

## Support

The play-spring-loader library is *[Community Driven][]*.

[Community Driven]: https://developer.lightbend.com/docs/lightbend-platform/introduction/getting-help/support-terminology.html#community-driven
