# Reference

This page shows all the features provided by the library.

##  Scaladoc

Browse the @scaladoc[API documentation](io.github.scala_tessella.ring_seq.RingSeq$) for detailed information.

## Methods

For dealing with a circular sequence, **Ring Seq** adds:

1. new operations.
2. alternative versions of some operations already existing for `Seq`.

@@@ note

The alternative circular versions maintain the same name,
but with an additional `O` suffix, meaning _ring_.
(For example `applyO` is the circular version of `apply`).

@@@

They fall into the following categories:

@@@ index

* [Indexing](categories/indexing.md)
* [Index search](categories/index-search.md)
* [Rotation and reflection](categories/rotation-reflection.md)
* [Iterators](categories/iterators.md)
* [Comparisons](categories/comparisons.md)
* [Symmetry](categories/symmetry.md)

@@@

### [Indexing](categories/indexing.html)

* [`applyO`](categories/indexing.html#applyo)
* [`sliceO`](categories/indexing.html#sliceo)

### [Index search](categories/index-search.html)

* [`indexOfSliceO`](categories/index-search.html#indexofsliceo)
* [`lastIndexOfSliceO`](categories/index-search.html#lastindexofsliceo)
* [`segmentLengthO`](categories/index-search.html#segmentlenghto)

### [Rotation and reflection](categories/rotation-reflection.html)
* [`rotateRight`](categories/rotation-reflection.html#rotateright)
* [`rotateLeft`](categories/rotation-reflection.html#rotateleft)
* [`startAt`](categories/rotation-reflection.html#startat)
* [`reflectAt`](categories/rotation-reflection.html#reflectat)

### [Iterators](categories/iterators.html)
* [`slidingO`](categories/iterators.html#slidingo)
* [`rotations`](categories/iterators.html#rotations)
* [`reversions`](categories/iterators.html#reversions)
* [`reflections`](categories/iterators.html#reflections)
* [`rotationsAndReflections`](iterators.html#rotationsandreflections)

### [Comparisons](categories/comparisons.html)
* [`containsSliceO`](categories/comparisons.html#containssliceo)
* [`isReflection`](categories/comparisons.html#isreflection)
* [`isReversion`](categories/comparisons.html#isreversion)
* [`isRotation`](categories/comparisons.html#isrotation)
* [`isRotationOrReflection`](categories/comparisons.html#isrotationorreflection)

### [Symmetry](categories/symmetry.html)

* [`rotationalSymmetry`](categories/symmetry.html#rotationalsymmetry)
* [`symmetryIndices`](categories/symmetry.html#symmetryindices)
* [`symmetry`](categories/symmetry.html#symmetry)
