name := "Hasher"

organization := "com.roundeights"

version := "0.3"

scalaVersion := "2.10.3"

// append -deprecation to the options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-feature")

// Repositories in which to find dependencies
resolvers ++= Seq(
    "Specs Repository" at "http://oss.sonatype.org/content/repositories/releases",
    "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"
)

publishTo := Some("Spikemark" at "https://spikemark.herokuapp.com/maven/roundeights")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

// Application dependencies
libraryDependencies ++= Seq(
    "org.mindrot" % "jbcrypt" % "0.3m" % "optional",
    "org.specs2" %% "specs2" % "1.13" % "test"
)
