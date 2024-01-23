# Symmetry

## `rotationalSymmetry`

Computes the order of rotational symmetry,
the number >= 1 of rotations in which a circular sequence looks exactly the same.

### Example

```scala
Seq(0, 1, 2, 0, 1, 2).rotationalSymmetry // 2
```

## `symmetryIndices`

Finds the indices of each element of a circular sequence close to an axis of reflectional symmetry,
that is a line of symmetry that splits the sequence in two identical halves.

### Example

```scala
Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetryIndices // List(1, 4, 7, 10)
```

## `symmetry`

Computes the order of reflectional (mirror) symmetry,
the number >= 0 of reflections in which a circular sequence looks exactly the same.

@@@ note

Reflectional symmetry is always lower or equal than rotational symmetry.

@@@

### Example

```scala
Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetry // 4
```
