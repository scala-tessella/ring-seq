val scalatest = "org.scalatest" %% "scalatest" % "3.2.10" % "test"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.15.4" % "test"

ThisBuild / organization := "com.tbd"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "ring-vector",
    libraryDependencies ++= Seq(scalatest, scalacheck)
  )
