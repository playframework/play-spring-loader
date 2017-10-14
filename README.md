# Play Spring Loader

This is an application loader for Play applications that runs with Spring as the DI. It binds and allows injecting all Play-provided components in addition to any components provided by third-party Play modules (defined as a `play.api.inject.Module`)

The current version targets Play 2.6.x and Spring 4.3.x. It may work but has not been tested on other versions.

There are currently no plans to add new features, but we're happy to accept contributions from the community.

## Setup Instructions

To use in your Play SBT project, add the dependency to your `build.sbt`:

```scala
libraryDependencies += "com.lightbend.play" %% "play-spring-loader" % "0.0.1"
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
