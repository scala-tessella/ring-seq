addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.11.1")
addSbtPlugin(
  ("com.typesafe.sbt" % "sbt-site" % "1.4.1")
    .exclude("org.scala-lang.modules", "scala-xml_2.12")
)
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.10.5")
addSbtPlugin(
  ("com.github.sbt" % "sbt-ghpages" % "0.8.0")
    .exclude("org.scala-lang.modules", "scala-xml_2.12")
)
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.10.0")
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.2.1")
addSbtPlugin("com.github.sbt" % "sbt-dynver" % "5.0.1")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.9")
