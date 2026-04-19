# Canonical forms (necklaces and bracelets)

A **necklace** is an equivalence class of circular sequences under rotation;
a **bracelet** is the equivalence class under rotation *and* reflection.

These operations return a canonical representative of the class, suitable for hashing, deduplication,
or comparing "is this ring the same pattern as that one?" in one step.

All three use Booth's O(n) algorithm where relevant.

## `canonicalIndex`

The starting index of the lexicographically smallest rotation.

### Example

```scala
Seq(2, 0, 1).canonicalIndex // 1
```

## `canonical`

The lexicographically smallest rotation (necklace canonical form).

Two sequences are rotations of each other iff their `canonical` forms are equal.

### Example

```scala
Seq(2, 0, 1).canonical // Seq(0, 1, 2)
Seq(1, 2, 0).canonical // Seq(0, 1, 2)
```

### Use as a rotation test

```scala
a.canonical == b.canonical // true iff a.isRotationOf(b)  (and sizes match)
```

## `bracelet`

The lexicographically smallest representative under **both** rotation and reflection.

Two sequences belong to the same bracelet equivalence class iff their `bracelet` forms are equal.

### Example

```scala
// Seq(3, 1, 2): rotations → [3,1,2],[1,2,3],[2,3,1]; add reflections → [3,2,1],[2,1,3],[1,3,2].
// The lex-smallest of all six is [1,2,3].
Seq(3, 1, 2).bracelet // Seq(1, 2, 3)
```
