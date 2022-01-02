val scalatest = "org.scalatest" %% "scalatest" % "3.2.10" % "test"

ThisBuild / organization := "com.edugenia"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "3.1.0"

lazy val root = (project in file("."))
  .settings(
    name := "ring-vector",
    libraryDependencies += scalatest
  )
