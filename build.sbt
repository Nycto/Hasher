name := "Hasher"

organization := "com.roundeights"

version := "1.2.0"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.11.8", "2.10.6")

// append -deprecation to the options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-feature")

// Repositories in which to find dependencies
resolvers ++= Seq(
    "Specs Repository" at "http://oss.sonatype.org/content/repositories/releases",
    // need this for scalaz transitive dependency of specs2 2.4.+  under scala 2.10 & 2.11
    "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases",
    "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"
)

publishTo := Some("Spikemark" at "https://spikemark.herokuapp.com/maven/roundeights")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

// Application dependencies
libraryDependencies ++= Seq(
    "org.mindrot" % "jbcrypt" % "0.3m" % "optional",
    "org.specs2" %% "specs2" % "2.4.+" % "test"
)
