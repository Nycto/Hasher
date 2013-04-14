name := "Hasher"

organization := "com.roundeights"

version := "1.0.0"

scalaVersion := "2.10.1"

// append -deprecation to the options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-feature")

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
