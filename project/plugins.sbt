logLevel := Level.Warn

val InterplayVersion = sys.props.get("interplay.version").getOrElse("1.3.12")

addSbtPlugin("com.typesafe.play" % "interplay" % InterplayVersion)
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.1")
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "3.0.2")