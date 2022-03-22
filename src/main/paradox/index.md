# Ring Seq

A library that adds implicit methods to Scala `Seq` for when a sequence needs to be considered **circular**, its elements forming a ring.

It works on any immutable / mutable `Seq` and sub-types.

Available for Scala `3.1.1`, Scala `2.13.8` and `2.12.15`.

## Setup

Add the following dependency to your `build.sbt` file:

@@@vars
~~~ scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq_2.13" % "$project.version$"
~~~
@@@

[//]: # (Browse the @scaladoc[API documentation]&#40;www.google.com&#41; for more information.)

## What is a circular sequence

For our purposes, a circular sequence is a sequence composed by a finite number of elements.

But being circular, the first element of the sequence can be considered as also placed just after the last element
(and the last just before the first).

So the "unrolling" of a circular sequence, both forth and backwards, can be assumed as theoretically infinite.

## Methods

For dealing with a circular sequence, **Ring Seq** adds

1. new operations (methods).
2. alternative versions of some operations already existing for `Seq`,
   identified by an `O` suffix (meaning _ring_).
   (For example `applyO` is the circular version of `apply`).

They fall into the following categories:

### Indexing

* `applyO`

### Index search

* `indexOfSliceO`
* `lastIndexOfSliceO`
* `segmentLengthO`

### Slicing

* `sliceO`
* `slidingO`

### Rotation and reflection
* `rotateRight`
* `rotateLeft`
* `startsAt`
* `reflectAt`
* `rotations`
* `reversions`
* `reflections`
* `rotationsAndReflections`

### Comparisons
* `containsSliceO`
* `isReflection`
* `isReversion`
* `isRotation`
* `isRotationOrReflection`

### Symmetry

* `reflectionalSymmetry`
* `simmetryIndices`
* `simmetry`
