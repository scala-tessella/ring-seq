import xerial.sbt.Sonatype.*

enablePlugins(SitePreviewPlugin, ParadoxSitePlugin)
enablePlugins(SiteScaladocPlugin)
enablePlugins(GhpagesPlugin)

ThisBuild / organization := "io.github.scala-tessella"
ThisBuild / crossScalaVersions := Seq("3.3.5", "2.13.16")
ThisBuild / scalaVersion := crossScalaVersions.value.head
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / licenses := Seq("APL2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / developers := List(Developer("scala-tessella", "scala-tessella", "mario.callisto@gmail.com", url("https://github.com/scala-tessella")))
ThisBuild / homepage := Some(url("https://github.com/scala-tessella"))

lazy val root =
  project
    .in(file("."))
    .aggregate(ringSeq.js, ringSeq.jvm, ringSeq.native)
    .settings(
      name := "ringSeq-root",
      sonatypeProjectHosting := Some(GitHubHosting("scala-tessella", "ring-seq", "mario.callisto@gmail.com")),
      sonatypeCredentialHost := "s01.oss.sonatype.org",
      SiteScaladoc / siteSubdirName := "api",
      paradoxProperties += ("scaladoc.base_url" -> "api"),
      git.remoteRepo := sonatypeProjectHosting.value.get.scmUrl,
      ghpagesNoJekyll := true,
      Compile / scalacOptions ++= {
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((3, _)) => Nil
          case Some((2, n)) if n <= 12 => List("-Ywarn-unused:imports")
          case _ => List("-Wunused:imports")
        }
      }
    )

lazy val ringSeq =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .crossType(CrossType.Pure)
    .in(file("."))
    .settings(
      name := "ring-seq",
      description := "Extends Scala Seq with ring (circular) methods",
      libraryDependencies ++= Seq(
        "org.scalatest" %%% "scalatest" % "3.2.19" % "test",
        "org.scalacheck" %%% "scalacheck" % "1.18.1" % "test"
      )
    )
    .jvmSettings(
      // Add JVM-specific settings here
    )
    .jsSettings(
      // Add JS-specific settings here
    )
    .nativeSettings(
      // Add native-specific settings here
    )
