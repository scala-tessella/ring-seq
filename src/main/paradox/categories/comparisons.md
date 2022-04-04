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
