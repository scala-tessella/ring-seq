# **RingSeq**
Extends Scala2 [`immutable.Seq`](https://www.scala-lang.org/api/current/scala/collection/immutable/Seq.html) with ring (circular) methods.

```scala
import RingSeq._

"RING".rotateRight(1) // GRIN
List(0, 1, 2, 3).startAt(2) // List(2, 3, 0, 1)
```

## How to use
Import the [`RingSeq`](/src/main/scala/RingSeq.scala) object
where a `Seq` has to be considered circular.

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

### Rotation and reflection
Rotate and reflect a circular `Seq`
(see [test cases](/src/test/scala/RotationsReflectionsSpec.scala))

### Symmetry
Calculate rotational and reflectional symmetries of a circular `Seq`
(see [test cases](/src/test/scala/SymmetriesSpec.scala))
