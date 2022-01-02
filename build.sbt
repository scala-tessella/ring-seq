
val scalatest =
 "org.scalatest" % "scalatest" % "3.2.9" % "test"

ThisBuild / organization := "com.edugenia"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "3.0.2"

lazy val root = (project in file("."))
  .settings(
    name := "ring-vector",
    libraryDependencies += scalatest
  )
