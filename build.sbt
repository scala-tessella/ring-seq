import xerial.sbt.Sonatype.*

enablePlugins(SitePreviewPlugin, ParadoxSitePlugin)
enablePlugins(SiteScaladocPlugin)
enablePlugins(GhpagesPlugin)

ThisBuild / organization := "io.github.scala-tessella"
ThisBuild / crossScalaVersions := Seq("3.3.3", "2.13.13")
ThisBuild / scalaVersion := crossScalaVersions.value.head
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / versionScheme := Some("early-semver")

lazy val root =
  project
    .in(file("."))
    .aggregate(ringSeq.js, ringSeq.jvm)
    .settings(
      sonatypeProjectHosting := Some(GitHubHosting("scala-tessella", "ring-seq", "mario.callisto@gmail.com")),
      sonatypeCredentialHost := "s01.oss.sonatype.org",
      publishTo := sonatypePublishToBundle.value,
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
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("."))
    .settings(
      name := "ring-seq",
      licenses := Seq("APL2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
      description := "Extends Scala Seq with ring (circular) methods",
      libraryDependencies ++= Seq(
        "org.scalatest" %%% "scalatest" % "3.2.18" % "test",
        "org.scalacheck" %%% "scalacheck" % "1.17.1" % "test"
      )
    )
    .jvmSettings(
      // Add JVM-specific settings here
    )
    .jsSettings(
    // Add JS-specific settings here
  )
