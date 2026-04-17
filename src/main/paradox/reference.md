# Reference

This page shows all the features provided by the library.

##  Scaladoc

Browse the Scala 3 (valid for Scala 2.13 as well) @scaladoc[API documentation](io.github.scala_tessella.ring_seq.RingSeq$) for detailed information.

## Methods

For dealing with a circular sequence, **RingSeq** adds:

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
* [Rotation and reflection](categories/rotation-reflection.md)
* [Slicing](categories/slicing.md)
* [Iterators](categories/iterators.md)
* [Comparisons](categories/comparisons.md)
* [Symmetry](categories/symmetry.md)

@@@

### [Indexing](categories/indexing.html)
* [`applyO`](categories/indexing.html#applyo)
* [`indexFrom`](categories/indexing.html#indexfrom)

### [Rotation and reflection](categories/rotation-reflection.html)
* [`rotateRight`](categories/rotation-reflection.html#rotateright)
* [`rotateLeft`](categories/rotation-reflection.html#rotateleft)
* [`startAt`](categories/rotation-reflection.html#startat)
* [`reflectAt`](categories/rotation-reflection.html#reflectat)

### [Slicing](categories/slicing.html)
* [`sliceO`](categories/slicing.html#sliceo)
* [`indexOfSliceO`](categories/slicing.html#indexofsliceo)
* [`lastIndexOfSliceO`](categories/slicing.html#lastindexofsliceo)
* [`segmentLengthO`](categories/slicing.html#segmentlenghto)
* [`containsSliceO`](categories/slicing.html#containssliceo)
* `takeWhileO`
* `dropWhileO`
* `spanO`

### [Iterators](categories/iterators.html)
* [`slidingO`](categories/iterators.html#slidingo)
* [`rotations`](categories/iterators.html#rotations)
* [`reversions`](categories/iterators.html#reversions)
* [`reflections`](categories/iterators.html#reflections)
* [`rotationsAndReflections`](categories/iterators.html#rotationsandreflections)
* `groupedO`
* `zipWithIndexO`

### [Comparisons](categories/comparisons.html)
* [`isReflection`](categories/comparisons.html#isreflection)
* [`isReversion`](categories/comparisons.html#isreversion)
* [`isRotation`](categories/comparisons.html#isrotation)
* [`isRotationOrReflection`](categories/comparisons.html#isrotationorreflection)
* `alignTo`
* `hammingDistance`
* `minRotationalHammingDistance`

### [Symmetry](categories/symmetry.html)
* [`rotationalSymmetry`](categories/symmetry.html#rotationalsymmetry)
* [`symmetryIndices`](categories/symmetry.html#symmetryindices)
* [`symmetry`](categories/symmetry.html#symmetry)

### Canonical forms (necklace / bracelet)
* `canonicalIndex` — starting index of the lexicographically smallest rotation (Booth's algorithm, O(n))
* `canonical` — the lex-smallest rotation; useful for hashing/deduplicating equivalent rings
* `bracelet` — the lex-smallest representative under both rotation and reflection
