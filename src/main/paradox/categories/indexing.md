# Indexing

## `applyO`

The circular equivalent of `apply`.

@@@ note

Given the [definition of circular sequence](../what-is.html), it returns an element for any possible integer.

@@@

### Example

```scala
Seq(0, 1, 2).applyO(3) // 0
```

### Compared to standard

In the same example the standard version behaves differently,
does not return an element, it throws.

```scala
Seq(0, 1, 2).apply(3) // IndexOutOfBoundsException
```

### Not a total function

It does not return a value for an empty sequence.

```scala
Seq.empty.applyO(0) // ArithmeticException
```

## `liftO`

The circular equivalent of `lift`: like `applyO`, but total.
Since any integer is a valid circular index of a non-empty sequence,
it returns `None` only when the sequence is empty.

### Example

```scala
Seq(0, 1, 2).liftO(3)   // Some(0)
Seq.empty[Int].liftO(0) // None
```

## `indexOfO`

The circular equivalent of `indexOf`:
finds the index of the first element equal to a given value,
searching circularly from a circular index (default `0`) and wrapping past the end.

### Example

```scala
Seq(0, 1, 2).indexOfO(0, 1) // 0 — found by wrapping, indexOf(0, 1) would be -1
```

## `indexFrom`

Converts a circular index into a standard index.

### Example

```scala
Seq(0, 1, 2).indexFrom(30001) // 1
```
