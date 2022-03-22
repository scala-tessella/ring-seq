# Reference

This page shows all the features provided by the library.

##  Scaladoc

Browse the @scaladoc[API documentation](io.github.scala_tessella.ring_seq.RingSeq$) for detailed information.

## Methods

For dealing with a circular sequence, **Ring Seq** adds

1. new operations.
2. alternative versions of some operations already existing for `Seq`,
   identified by an `O` suffix (meaning _ring_).
   (For example `applyO` is the circular version of `apply`).

They fall into the following categories:

@@@ index

* [Indexing](categories/indexing.md)
* [Slicing](categories/slicing.md)
* [Index search](categories/index-search.md)
* [Rotation and reflection](categories/rotation-reflection.md)
* [Comparisons](categories/comparisons.md)
* [Symmetry](categories/symmetry.md)

@@@

### [Indexing](categories/indexing.html)

* [`applyO`](categories/indexing.html#applyo)


### Slicing

* `sliceO`
* `slidingO`

### Index search

* `indexOfSliceO`
* `lastIndexOfSliceO`
* `segmentLengthO`

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
