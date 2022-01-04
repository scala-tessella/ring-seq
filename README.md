# **RingVector**
Extends Scala3 [`immutable.Vector`](https://dotty.epfl.ch/api/scala/collection/immutable/Vector.html) with ring (circular) methods.

```
"RING".toVector.rotateRight(1) // Vector(G,R,I,N))
```

## How to use
Use the `RingVector` trait to extend the classes or objects containing circular `Vector`s.

## Need
Whenever data are structured in a circular sequence,
chances are you don't want to locally reinvent the wheel 😉.

## Solution
**RingVector** is a small, purely functional, self-contained library,
where most of the circular use cases are already solved
and building blocks provided for the others.

One possible idea has been to create a dedicated collection.
But the differences with `Vector` are few,
so a Scala3 [`extension`](https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html) seems a better fit.

## Methods

### Circular version of existing ones
Named as their plain non-circular `Vector` versions,
but with an `O` suffix (meaning _ring_).

They are (see [test cases](/src/test/scala/AlternativeMethodsSpec.scala)):
* `applyO`
* `segmentLengthO`
* `sliceO`
* `containsSliceO`
* `indexOfSliceO`
* `lastIndexOfSliceO`
* `slidingO`

### Rotation and reflection
Rotate and reflect a circular `Vector`
(see [test cases](/src/test/scala/RotationsReflectionsSpec.scala))

### Symmetry
Calculate rotational and reflectional symmetries of a circular `Vector`
(see [test cases](/src/test/scala/SymmetriesSpec.scala))


