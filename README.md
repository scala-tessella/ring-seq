# **RingSeq**
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

Adds implicit methods to Scala 2 [`immutable.Seq`](https://www.scala-lang.org/api/current/scala/collection/immutable/Seq.html) (and subtypes) for when a sequence needs to be considered **circular**, its elements forming a ring.

```scala
import RingSeq._

"RING".rotateRight(1).mkString // GRIN
List(0, 1, 2, 3).startAt(2) // List(2, 3, 0, 1)
```

## How to use
Just import the [`RingSeq`](/src/main/scala/RingSeq.scala) object, any collection under `Seq`
will access the new methods.

## Need
Whenever data are structured in a circular sequence,
chances are you don't want to locally reinvent the wheel (pun intended).

## Solution
**RingSeq** is a small, purely functional, self-contained library,
where most of the circular use cases are already solved
and building blocks provided for the others.

Leveraging Scala2 [`implicit class`](https://docs.scala-lang.org/overviews/core/implicit-classes.html),
it acts like a _decorator_,
providing new circular methods to any collection under `Seq`.

## Methods

### Transformations
Rotate and reflect a circular `Seq`
(see [test cases](/src/test/scala/RotationsReflectionsSpec.scala)).

### Circular version of existing ones
Named as their standard non-circular `Seq` alternatives,
but with an `O` suffix (meaning _ring_).

They are (see [test cases](/src/test/scala/OMethodsSpec.scala)):
* `applyO`
* `segmentLengthO`
* `sliceO`
* `containsSliceO`
* `indexOfSliceO`
* `lastIndexOfSliceO`
* `slidingO`

### Symmetry
Calculate rotational and reflectional symmetries of a circular `Seq`
(see [test cases](/src/test/scala/SymmetriesSpec.scala)).
