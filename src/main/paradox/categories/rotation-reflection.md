# Rotation and reflection

## `rotateRight`

Returns a sequence rotated to the right.

### Example

```scala
Seq(0, 1, 2).rotateRight(1) // Seq(2, 0, 1)
```

##`rotateLeft`

Returns a sequence rotated to the left.

### Example

```scala
Seq(0, 1, 2).rotateLeft(1) // Seq(1, 2, 0)
```

##`startAt`

Returns a sequence rotated to start at circular index.

_Note:_ is equivalent to [`rotateLeft`](rotation-reflection.html#rotateleft).

### Example

```scala
Seq(0, 1, 2).startAt(1) // Seq(1, 2, 0)
```

##`reflectAt`

Returns a sequence reversed and rotated to start at circular index.

_Note:_ `reflectAt(-1)` is equivalent to `reverse`.

### Example

```scala
Seq(0, 1, 2).reflectAt() // Seq(0, 2, 1)
```
