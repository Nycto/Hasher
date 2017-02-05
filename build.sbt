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

publishMavenStyle := true

publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
    else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
    <url>https://github.com/Nycto/Hasher</url>
    <licenses>
        <license>
            <name>MIT</name>
            <url>https://opensource.org/licenses/MIT</url>
            <distribution>repo</distribution>
        </license>
    </licenses>
    <scm>
        <url>git@github.com:Nycto/Hasher.git</url>
        <connection>scm:git:git@github.com:Nycto/Hasher.git</connection>
    </scm>
    <developers>
        <developer>
            <id>Nycto</id>
            <name>James Frasca</name>
            <url>http://roundeights.com</url>
        </developer>
    </developers>)

// Application dependencies
libraryDependencies ++= Seq(
    "org.mindrot" % "jbcrypt" % "0.3m" % "optional",
    "org.specs2" %% "specs2" % "2.4.+" % "test"
)
