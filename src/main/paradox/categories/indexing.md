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

## `indexFrom`

Converts a circular index into a standard index.

### Example

```scala
Seq(0, 1, 2).indexFrom(30001) // 1
```
