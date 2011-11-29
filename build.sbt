name := "Hasher"

scalaVersion := "2.9.1"

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"

// Repositories in which to find dependencies
resolvers ++= Seq(
    "Specs Repository" at "http://scala-tools.org/repo-releases",
    "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"
)

// Application dependencies
libraryDependencies ++= Seq(
    "org.mindrot" % "jbcrypt" % "0.3m" % "provided->default",
    "junit" % "junit" % "4.9" % "test",
    "org.specs2" %% "specs2" % "1.6.1" % "test",
    "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test"
)
