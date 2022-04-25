# **RingSeq**
[![ring-seq Scala version support](https://index.scala-lang.org/scala-tessella/ring-seq/ring-seq/latest.svg?platform=jvm)](https://index.scala-lang.org/scala-tessella/ring-seq/ring-seq)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.scala-tessella/ring-seq_3.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.scala-tessella%22%20AND%20a:%22ring-seq_3%22)
[![CI](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml/badge.svg)](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

A library that adds new operations to Scala `Seq`
for when a sequence needs to be considered [**circular**](https://scala-tessella.github.io/ring-seq/what-is.html),
its elements forming a ring.

It works on any immutable / mutable `Seq` and sub-types.

Available for Scala `3.2.12` and `2.13.8`.

## Setup

Add the following dependency to your `build.sbt` file:
```scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq" % "0.4.0"
```

Then just import the [`RingSeq`](/src/main/scala-3/io/github/scala_tessella/ring_seq/RingSeq.scala) object, any collection under `Seq`
will access the new methods.

```scala
import io.github.scala_tessella.ring_seq.RingSeq._

"RING".rotateRight(1).mkString // GRIN
List(0, 1, 2, 3).startAt(2) // List(2, 3, 0, 1)
ListBuffer(1, 3, 5, 7, 9).reflectAt(3) // ListBuffer(7, 5, 3, 1, 9)
```

## Documentation

### Usage

One usage example is provided and documented [here](https://scala-tessella.github.io/ring-seq/usage.html).

### Scaladoc

The Scaladoc API documentation for Scala 3 (valid for Scala 2.13 as well)
can be browsed and searched [here](https://scala-tessella.github.io/ring-seq/api/io/github/scala_tessella/ring_seq/RingSeq$.html).

### Website

Check the [RingSeq website](https://scala-tessella.github.io/ring-seq/) for more info.