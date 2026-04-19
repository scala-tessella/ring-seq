# Symmetry

## `rotationalSymmetry`

Computes the order of rotational symmetry,
the number >= 1 of rotations in which a circular sequence looks exactly the same.

### Example

```scala
Seq(0, 1, 2, 0, 1, 2).rotationalSymmetry // 2
```

## `symmetryIndices`

Finds the indices at which each axis of reflectional symmetry intersects the circular sequence —
a line of symmetry splits the sequence into two identical halves.

### Example

```scala
Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetryIndices // List(0, 3, 6, 9)
```

## `reflectionalSymmetryAxes`

Each axis of reflectional symmetry crosses the ring at **two points**, which can be vertices (through an element)
or edges (between two consecutive elements). `reflectionalSymmetryAxes` returns those pairs as
`(AxisLocation, AxisLocation)`, where `AxisLocation` is `Vertex(i)` or `Edge(i, n)` (`n` is the ring size;
the edge is between index `i` and `(i + 1) mod n`).

### Example

```scala
import io.github.scala_tessella.ring_seq.RingSeq._ // brings AxisLocation, Vertex and Edge into scope

Seq(1, 1, 1).reflectionalSymmetryAxes
// List(
//   (Vertex(1), Edge(2, 3)),
//   (Vertex(2), Edge(0, 3)),
//   (Vertex(0), Edge(1, 3))
// )
```

A sequence with no reflectional symmetry returns `Nil`; a sequence with mirror symmetry through two vertices
(e.g. a doubled triangle) returns vertex–vertex pairs.

```scala
Seq(1, 2, 1, 2, 1, 2).reflectionalSymmetryAxes
// List((Vertex(2), Vertex(5)), (Vertex(1), Vertex(4)), (Vertex(0), Vertex(3)))
```

## `symmetry`

Computes the order of reflectional (mirror) symmetry,
the number >= 0 of reflections in which a circular sequence looks exactly the same.

@@@ note

Reflectional symmetry is always lower or equal than rotational symmetry.

@@@

### Example

```scala
Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetry // 4
```
