# [**RingSeq**](https://scala-tessella.github.io/ring-seq/)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![CI](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml/badge.svg)](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.scala-tessella/ring-seq_3.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.scala-tessella%22%20AND%20a:%22ring-seq_3%22)

A library that adds new operations to Scala `Seq`
for when a sequence needs to be considered [**circular**](https://scala-tessella.github.io/ring-seq/what-is.html),
its elements forming a ring.

It works on any immutable / mutable `Seq` and sub-types.

Available for Scala `3.1.1`, Scala `2.13.8` and `2.12.15`.

## Setup

According to your Scala version, add one of the following dependencies to your `build.sbt` file:
```scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq_3" % "0.1.3"
```
```scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq_2.13" % "0.1.3"
```
```scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq_2.12" % "0.1.3"
```

Then just import the [`RingSeq`](/src/main/scala/io/github/scala_tessella/ring_seq/RingSeq.scala) object, any collection under `Seq`
will access the new methods.

```scala
import io.github.scala_tessella.ring_seq.RingSeq._

"RING".rotateRight(1).mkString // GRIN
List(0, 1, 2, 3).startAt(2) // List(2, 3, 0, 1)
ListBuffer(1, 3, 5, 7, 9).reflectAt(3) // ListBuffer(7, 5, 3, 1, 9)
```

## Documentation

### Scaladoc

The Scaladoc API documentation for 2.13 (valid for the other Scala versions too)
can be browsed and searched [here](https://scala-tessella.github.io/ring-seq/api/io/github/scala_tessella/ring_seq/RingSeq$.html).

### Website

Check the [RingSeq website](https://scala-tessella.github.io/ring-seq/) for more info.