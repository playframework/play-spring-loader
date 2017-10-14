name := """spring-java"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.3"

val SpringVersion = "4.3.11.RELEASE"

libraryDependencies ++= Seq(
  "com.lightbend.play" %% "play-spring-loader" % "0.0.1",
  "org.springframework" % "spring-core" % SpringVersion,
  "org.springframework" % "spring-expression" % SpringVersion,
  "org.springframework" % "spring-aop" % SpringVersion
)