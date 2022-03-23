# Comparisons

## `containsSliceO`

The circular equivalent of `containsSlice`.

_Note:_ given the [definition of circular sequence](../what-is.html), a slice can contain more elements than the sequence itself.

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

## `isReflection`

Tests if a sequence is a reflection of another one.

_Note:_ a sequence is always a reflection of itself.

### Example

```scala
Seq(0, 1, 2).isReflectionOf(Seq(0, 2, 1)) // true
```

## `isReversion`

Tests if a sequence is a reversion of another one.

_Note:_ a sequence is always a reversion of itself.

### Example

```scala
Seq(0, 1, 2).isReversionOf(Seq(2, 1, 0)) // true
```

## `isRotation`

Tests if a sequence is a rotation of another one.

_Note:_ a sequence is always a rotation of itself.

### Example

```scala
Seq(0, 1, 2).isRotationOf(Seq(1, 2, 0)) // true
```

## `isRotationOrReflection`

Tests if a sequence is a rotation or a reflection of another one.

_Note:_ a sequence is always a rotation and a reflection of itself.

### Example

```scala
Seq(0, 1, 2).isRotationOrReflection(Seq(2, 0, 1)) // true
```
