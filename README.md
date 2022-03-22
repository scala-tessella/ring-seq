# **RingSeq**
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![CI](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml/badge.svg)](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.scala-tessella/ring-seq_2.13.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.scala-tessella%22%20AND%20a:%22ring-seq_2.13%22)

A library that adds new operations to Scala `Seq` for when a sequence needs to be considered **circular**, its elements forming a ring.

It works on any immutable / mutable `Seq` and sub-types.

Available for Scala `3.1.1`, Scala `2.13.8` and `2.12.15`.

## Setup

Add the following dependency to your `build.sbt` file:
```scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq" % "0.1.2+1-1449b9d8"
```

Then just import the [`RingSeq`](/src/main/scala/io/github/scala_tessella/ring_seq/RingSeq.scala) object, any collection under `Seq`
will access the new methods.

```scala
import io.github.scala_tessella.ring_seq.RingSeq._

"RING".rotateRight(1).mkString // GRIN
List(0, 1, 2, 3).startAt(2) // List(2, 3, 0, 1)
ListBuffer(1, 3, 5, 7, 9).reflectAt(3) // ListBuffer(7, 5, 3, 1, 9)
```

## Need
Whenever data are structured in a circular sequence,
chances are you don't want to locally reinvent the wheel (pun intended).

### What is a circular sequence

For our purposes, a circular sequence is a sequence composed by a finite number of elements.

But being circular, the first element of the sequence can be considered as also placed just after the last element

```scala
Seq(0, 1, 2).applyO(3) // 0
```

(and the last just before the first).

```scala
Seq(0, 1, 2).applyO(-1) // 2
```

So the "unrolling" of a circular sequence, both forth and backwards, can be assumed as theoretically infinite.

```scala
Seq(0, 1, 2).applyO(30001) // 1
```

## Solution
**RingSeq** is a small, purely functional, self-contained library,
where most of the circular use cases are already solved
and building blocks provided for the others.

Leveraging Scala [`implicit class`](https://docs.scala-lang.org/overviews/core/implicit-classes.html),
it acts like a _decorator_,
providing new circular methods to any collection under `Seq`.

## Methods

### Transformations
Rotate and reflect a circular `Seq`
(see [test cases](/src/test/scala/io/github/scala_tessella/ring_seq/RotationsReflectionsSpec.scala)).

### Circular version of existing ones
Named as their standard non-circular `Seq` alternatives,
but with an `O` suffix (meaning _ring_).

They are (see [test cases](/src/test/scala/io/github/scala_tessella/ring_seq/OMethodsSpec.scala)):
* `applyO`
* `segmentLengthO`
* `sliceO`
* `containsSliceO`
* `indexOfSliceO`
* `lastIndexOfSliceO`
* `slidingO`

### Symmetry
Calculate rotational and reflectional symmetries of a circular `Seq`
(see [test cases](/src/test/scala/io/github/scala_tessella/ring_seq/SymmetriesSpec.scala)).
