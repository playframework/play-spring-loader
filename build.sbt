
organization := "com.lightbend.play"
name := "play-spring-loader"

playBuildRepoName in ThisBuild := "play-spring-loader"

organizationName := "Lightbend"
startYear := Some(2017)

val PlayVersion = "2.6.6"
val SpringVersion = "4.3.11.RELEASE"

lazy val root = (project in file(".")).enablePlugins(PlayLibrary)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % PlayVersion,
  "org.springframework" % "spring-context" % SpringVersion
)

import ReleaseTransformations._
releaseCrossBuild := true
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
  pushChanges
)

lazy val checkCodeFormat = taskKey[Unit]("Check that code format is following Scalariform rules")

checkCodeFormat := {
  val exitCode = "git diff --exit-code".!
  if (exitCode != 0) {
    sys.error(
      """
        |ERROR: Scalariform check failed, see differences above.
        |To fix, format your sources using sbt scalariformFormat test:scalariformFormat before submitting a pull request.
        |Additionally, please squash your commits (eg, use git commit --amend) if you're going to update this pull request.
      """.stripMargin)
  }
}

addCommandAlias("validateCode", ";scalariformFormat;test:scalariformFormat;headerCheck;test:headerCheck;checkCodeFormat")