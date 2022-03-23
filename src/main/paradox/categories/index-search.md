# Index search

## `indexOfSliceO`

The circular equivalent of `indexOfSlice`.

_Note:_ given the [definition of circular sequence](../what-is.html), a slice can contain more elements than the sequence itself.

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

##`lastIndexOfSliceO`

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

##`segmentLengthO`

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
