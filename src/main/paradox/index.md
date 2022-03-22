# Ring Seq

A library that adds new operations to Scala to Scala `Seq` for when a sequence needs to be considered **circular**, its elements forming a ring.

It works on any immutable / mutable `Seq` and sub-types.

Available for Scala `3.1.1`, Scala `2.13.8` and `2.12.15`.

## Setup

Add the following dependency to your `build.sbt` file:

@@@vars
~~~ scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq_2.13" % "$project.version$"
~~~
@@@

Then just import the [`RingSeq`](https://github.com/scala-tessella/ring-seq/blob/master/src/main/scala/io/github/scala_tessella/ring_seq/RingSeq.scala) object, any collection under `Seq`
will access the new methods.

```scala
import io.github.scala_tessella.ring_seq.RingSeq._

"RING".rotateRight(1).mkString // GRIN
List(0, 1, 2, 3).startAt(2) // List(2, 3, 0, 1)
ListBuffer(1, 3, 5, 7, 9).reflectAt(3) // ListBuffer(7, 5, 3, 1, 9)
```


@@@ index
* [What is a circular sequence](what-is.md)
* [Reference](reference.md)
@@@
