name := "Hasher"

scalaVersion := "2.10.0"

version := "0.3"

// append -deprecation to the options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-feature")

scalacOptions += "-feature"

// Repositories in which to find dependencies
resolvers ++= Seq(
    "Specs Repository" at "http://oss.sonatype.org/content/repositories/releases",
    "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"
)

// Application dependencies
libraryDependencies ++= Seq(
    "org.mindrot" % "jbcrypt" % "0.3m" % "optional",
    "org.specs2" %% "specs2" % "1.13" % "test"
)
