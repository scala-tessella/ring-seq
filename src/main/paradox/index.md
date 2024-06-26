# RingSeq

A library that adds new operations to Scala `Seq`
for when a sequence needs to be considered [**circular**](what-is.html),
its elements forming a ring.

It works on any immutable / mutable `Seq` and sub-types.

Available for Scala `3.3.3` and `2.13.13`, compatible with Scala.js

## Setup

Add the following dependency to your `build.sbt` file:

@@@vars
~~~ scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq" % "$project.version$" // Use %%% instead of %% if you're using ScalaJS
~~~
@@@

## Getting started

First, start with the following import:

```scala
import io.github.scala_tessella.ring_seq.RingSeq._
```

Where the [`RingSeq`](https://github.com/scala-tessella/ring-seq/blob/master/src/main/scala/io/github/scala_tessella/ring_seq/RingSeq.scala) object is imported, any collection under `Seq`
will access the new methods. You can write something like:

```scala
"RING".rotateRight(1).mkString // GRIN
List(0, 1, 2, 3).startAt(2) // List(2, 3, 0, 1)
ListBuffer(1, 3, 5, 7, 9).reflectAt(3) // ListBuffer(7, 5, 3, 1, 9)
```

@@@ index
* [What is a circular sequence](what-is.md)
* [Usage examples](usage.md)
* [Reference](reference.md)
* [GitHub repository](github.md)

@@@

## Need
Whenever data are structured in a circular sequence,
chances are you don't want to locally reinvent the wheel (pun intended).

## Solution
**RingSeq** is a small, purely functional, self-contained library,
where most of the circular use cases are already solved
and building blocks provided for the others.

Leveraging Scala 3 [`extension`](https://docs.scala-lang.org/scala3/book/ca-extension-methods.html)
or Scala 2 [`implicit class`](https://docs.scala-lang.org/overviews/core/implicit-classes.html),
it acts like a _decorator_,
providing new circular methods to any collection under `Seq`.

## Design

The same decorators could possibly be added to [scala-collection-contrib](https://github.com/scala/scala-collection-contrib) and exist just there,
but the underlying design has (at least for me) a very steep learning curve and the community help is weak.
One clear maintenance advantage would be that the source code is the same for Scala 2.13 and Scala 3.

So, for now, is a conscious decision to achieve the same results with a simpler design (even if the source code is different between Scala 2.13 and Scala 3) and to publish in this, concise and hopefully useful, separate library.

## Other languages

The same library is available also for the Python language, check [RingSeqPy (Python version)](https://github.com/scala-tessella/ring-seq-py/).
