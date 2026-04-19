# Slicing

## `sliceO`

The circular equivalent of `slice`.

@@@ note

Given the [definition of circular sequence](../what-is.html), a slice can contain more elements than the sequence itself.

@@@

### Example

```scala
Seq(0, 1, 2).sliceO(-1, 4) // Seq(2, 0, 1, 2, 0)
```

### Compared to standard

In the same example the standard version behaves differently,
it is equivalent to `slice(0, 2)`, does not return the "unrolled" slice.

```scala
Seq(0, 1, 2).slice(-1, 4) // Seq(0, 1, 2)
```

## `indexOfSliceO`

The circular equivalent of `indexOfSlice`.

@@@ note

Given the [definition of circular sequence](../what-is.html), a slice can contain more elements than the sequence itself.

@@@

### Example

```scala
Seq(0, 1, 2).indexOfSliceO(Seq(2, 0, 1, 2, 0)) // 2
```

### Compared to standard

In the same example the standard version behaves differently,
cannot find the "unrolled" slice.

```scala
Seq(0, 1, 2).indexOfSlice(Seq(2, 0, 1, 2, 0)) // -1
```

## `lastIndexOfSliceO`

The circular equivalent of `lastIndexOfSlice`.

### Example

```scala
Seq(0, 1, 2, 0, 1, 2).lastIndexOfSliceO(Seq(2, 0)) // 5
```

### Compared to standard

In the same example the standard version behaves differently,
does not find the slice containing the last and first elements.

```scala
Seq(0, 1, 2, 0, 1, 2).lastIndexOfSlice(Seq(2, 0)) // 2
```

## `segmentLengthO`

The circular equivalent of `segmentLength`.

### Example

```scala
Seq(0, 1, 2).segmentLengthO(_ % 2 == 0, 2) // 2
```

### Compared to standard

In the same example the standard version behaves differently,
does not find the segment containing the last and first elements.

```scala
Seq(0, 1, 2).segmentLength(_ % 2 == 0, 2) // 1
```

## `containsSliceO`

The circular equivalent of `containsSlice`.

@@@ note

Given the [definition of circular sequence](../what-is.html), a slice can contain more elements than the sequence itself.

@@@

### Example

```scala
Seq(0, 1, 2).containsSliceO(Seq(2, 0, 1, 2, 0)) // true
```

### Compared to standard

In the same example the standard version behaves differently,
does not find the "unrolled" slice.

```scala
Seq(0, 1, 2).containsSlice(Seq(2, 0, 1, 2, 0)) // false
```

## `takeWhileO`

The circular equivalent of `takeWhile`, starting at a given circular index.

### Example

```scala
Seq(0, 1, 2, 3, 4).takeWhileO(_ < 3, 1) // Seq(1, 2)
```

### Wrapping around

The prefix can cross the seam between the last and first elements.

```scala
// startAt(3) gives Seq(3, 4, 0, 1, 2); takeWhile(_ != 1) stops at index 0
Seq(0, 1, 2, 3, 4).takeWhileO(_ != 1, 3) // Seq(3, 4, 0)
```

## `dropWhileO`

The circular equivalent of `dropWhile`, starting at a given circular index.

### Example

```scala
Seq(0, 1, 2, 3, 4).dropWhileO(_ < 3, 1) // Seq(3, 4, 0)
```

## `spanO`

The circular equivalent of `span` — splits into the pair `(takeWhileO(p), dropWhileO(p))` without evaluating the predicate twice.

### Example

```scala
Seq(0, 1, 2, 3, 4).spanO(_ < 3, 1) // (Seq(1, 2), Seq(3, 4, 0))
```
