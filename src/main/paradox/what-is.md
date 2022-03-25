# What is a circular sequence

For our purposes, a circular sequence is a sequence composed by a finite number of elements forming a ring.

![circular](circular_plain.svg)

Being circular, the first element of the sequence can be considered as also placed just after the last element.

```scala
Seq(0, 1, 2).applyO(3) // 0
```

And the last just before the first.

```scala
Seq(0, 1, 2).applyO(-1) // 2
```

So the "unrolling" of a circular sequence, both forth and backwards, can be assumed as theoretically infinite.

```scala
Seq(0, 1, 2).applyO(30001) // 1
```
