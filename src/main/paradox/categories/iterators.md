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

## `rotations`

All possible rotations.

@@@ note

Starting from itself and moving one rotation step to the left.

@@@

### Example

```scala
Seq(0, 1, 2).rotations // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1))
```

### On empty seq

Just itself on empty `Seq`.

```scala
Seq.empty.rotations // Iterator(Seq())
```

## `reversions`

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

## `reflections`

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

## `rotationsAndReflections`

All possible rotations and reflections.

@@@ note

Starting from itself and moving one rotation step to the right, then reversing and doing the same.

@@@

### Example

```scala
Seq(0, 1, 2).rotationsAndReflections // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(0, 2, 1), Seq(2, 1, 0), Seq(1, 0, 2))
```

### On empty seq

Just itself on empty `Seq`.

```scala
Seq.empty.rotationsAndReflections // Iterator(Seq())
```

## `groupedO`

The circular equivalent of `grouped` — partitions the ring into fixed-size blocks. Equivalent to `slidingO(size, size)`.

Unlike standard `grouped`, the final block wraps around the seam, so every block has exactly `size` elements.

### Example

```scala
"ABCDE".groupedO(2).toList.map(_.mkString) // List("AB", "CD", "EA")
```

### On empty seq

```scala
Seq.empty.groupedO(2).toList // Nil
```

## `zipWithIndexO`

The circular equivalent of `zipWithIndex`, pairing each element with its **original** (circular) index rather than its position in the iterator. Accepts an optional starting circular index.

### Example

```scala
Seq('a', 'b', 'c').zipWithIndexO(1).toList // List(('b', 1), ('c', 2), ('a', 0))
```

### Default

Starting at index `0` produces the same output as `zipWithIndex`.

```scala
Seq('a', 'b', 'c').zipWithIndexO().toList // List(('a', 0), ('b', 1), ('c', 2))
```
