# Comparisons

## `isReflection`

Tests if a sequence is a reflection of another one.

@@@ note

A sequence is always a reflection of itself.

@@@

### Example

```scala
Seq(0, 1, 2).isReflectionOf(Seq(0, 2, 1)) // true
```

## `isReversion`

Tests if a sequence is a reversion of another one.

@@@ note

A sequence is always a reversion of itself.

@@@

### Example

```scala
Seq(0, 1, 2).isReversionOf(Seq(2, 1, 0)) // true
```

## `isRotation`

Tests if a sequence is a rotation of another one.

@@@ note

A sequence is always a rotation of itself.

@@@

### Example

```scala
Seq(0, 1, 2).isRotationOf(Seq(1, 2, 0)) // true
```

## `isRotationOrReflection`

Tests if a sequence is a rotation or a reflection of another one.

@@@ note

A sequence is always a rotation and a reflection of itself.

@@@

### Example

```scala
Seq(0, 1, 2).isRotationOrReflection(Seq(2, 0, 1)) // true
```

## `alignTo`

Returns the rotation offset `k` such that `this.startAt(k) == that`, or `None` if `that` is not a rotation of `this` (including a size mismatch).

### Example

```scala
Seq(0, 1, 2).alignTo(Seq(2, 0, 1)) // Some(2)
Seq(0, 1, 2).alignTo(Seq(1, 0, 2)) // None
```

### Compared to `isRotationOf`

`isRotationOf` answers _whether_ two sequences are rotations; `alignTo` additionally tells you _by how much_.

## `hammingDistance`

Counts the positional mismatches between two circular sequences of the same size. Throws `IllegalArgumentException` if sizes differ.

### Example

```scala
Seq(1, 0, 1, 1).hammingDistance(Seq(1, 1, 0, 1)) // 2
Seq(1, 2, 3).hammingDistance(Seq(1, 2, 3))       // 0
```

## `minRotationalHammingDistance`

The minimum Hamming distance over all rotations of this sequence against `that`. Returns `0` iff `that` is a rotation of `this`.

### Example

```scala
// Rotating [0,0,1,1,0] by 2 gives [1,1,0,0,0]; vs [1,1,0,0,1] → 1 mismatch.
Seq(0, 0, 1, 1, 0).minRotationalHammingDistance(Seq(1, 1, 0, 0, 1)) // 1
```
