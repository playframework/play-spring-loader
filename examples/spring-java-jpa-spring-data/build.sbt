name := """spring-java-jpa-spring-data"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.3"

val SpringVersion = "4.3.11.RELEASE"

libraryDependencies ++= Seq(
  javaJdbc,
  "com.lightbend.play" %% "play-spring-loader" % "0.0.1",
  "org.springframework.data" % "spring-data-jpa" % "1.11.8.RELEASE",
  "org.hibernate" % "hibernate-entitymanager" % "5.2.12.Final",
  "com.h2database" % "h2" % "1.4.196"
)
