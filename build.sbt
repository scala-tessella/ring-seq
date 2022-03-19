import xerial.sbt.Sonatype._

val scalatest = "org.scalatest" %% "scalatest" % "3.2.11" % "test"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.15.4" % "test"

enablePlugins(SiteScaladocPlugin)

ThisBuild / organization := "io.github.scala-tessella"
ThisBuild / crossScalaVersions := Seq("2.13.8", "2.12.15")
ThisBuild / scalaVersion := crossScalaVersions.value.head
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "ring-seq",
    licenses := Seq("APL2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    description := "Extends Scala2 Seq with ring (circular) methods",
    sonatypeProjectHosting := Some(GitHubHosting("scala-tessella", "ring-seq", "mario.callisto@gmail.com")),
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    publishTo := sonatypePublishToBundle.value,
    libraryDependencies ++= Seq(scalatest, scalacheck),
    coverageEnabled := true,
    Compile / scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) if n <= 12 => List("-Ywarn-unused:imports")
        case _ => List("-Wunused:imports")
      }
    }
  )
