# Iterators

## `slidingO`

The circular equivalent of `sliding`.

### Example

```scala
Seq(0, 1, 2).slidingO(2) // Iterator(Seq(0, 1), Seq(1, 2), Seq(2, 0))```
```

### Compared to standard

In the same example the standard version behaves differently,
does not pass the "sliding window" on the last and first elements.

```scala
Seq(0, 1, 2).sliding(2) // Iterator(Seq(0, 1), Seq(1, 2))```
```

##`rotations`

All possible rotations.

_Note:_ Starting from itself and moving one rotation step to the right.

### Example

```scala
Seq(0, 1, 2).rotations // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1))
```

### On empty seq

Just itself on empty `Seq`.

```scala
Seq.empty.rotations // Iterator(Seq())
```

##`reversions`

2 sequences, the sequence itself and reversed.

### Example

```scala
Seq(0, 1, 2).reversions // Iterator(Seq(0, 1, 2), Seq(2, 1, 0))
```

### On empty seq

Just itself on empty `Seq`.

```scala
Seq.empty.reversions // Iterator(Seq())
```

##`reflections`

2 sequences, the sequence itself and reflected at the start.

### Example

```scala
Seq(0, 1, 2).reflections // Iterator(Seq(0, 1, 2), Seq(0, 2, 1))
```

### On empty seq

Just itself on empty `Seq`.

```scala
Seq.empty.reflections // Iterator(Seq())
```

##`rotationsAndReflections`

All possible rotations and reflections.

_Note:_ Starting from itself and moving one rotation step to the right, then reversing and doing the same.

### Example

```scala
Seq(0, 1, 2).rotationsAndReflections // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(0, 2, 1), Seq(2, 1, 0), Seq(1, 0, 2))
```

### On empty seq

Just itself on empty `Seq`.

```scala
Seq.empty.rotationsAndReflections // Iterator(Seq())
```
