val scalatest = "org.scalatest" %% "scalatest" % "3.2.11" % "test"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.15.4" % "test"

enablePlugins(SiteScaladocPlugin)

ThisBuild / organization := "com.tbd"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "ring-seq",
    libraryDependencies ++= Seq(scalatest, scalacheck),
    scalacOptions += "-Wunused:imports" // required by `RemoveUnused` rule
  )
