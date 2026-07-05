# The RingView layer

`.ring` turns any `Seq` (or `String` / `StringBuilder`) into a **lazy view** of the circular sequence:

```scala
import io.github.scala_tessella.ring_seq.RingSeq._

val ring = Vector(0, 1, 2, 3).ring // RingView[Int] — no copy
```

A `RingView` wraps the underlying elements together with a rotation offset and a reflection flag.
All the positional bookkeeping goes through a single internal translation,
so transformations are pure index arithmetic:

```scala
ring.rotateRight(1)  // O(1), still a view
ring.reflectAt()     // O(1), still a view
ring.reverse         // O(1), still a view
ring.startAt(2)      // O(1), still a view
```

This is the same design as the `Circular` view of the
[Rust port](https://github.com/scala-tessella/ring-seq-rs).

## Views all the way down

`rotations`, `reflections`, `reversions` and `rotationsAndReflections` return iterators of
**views**, produced in O(1) each — so searches short-circuit without materializing anything:

```scala
Vector(0, 1, 2, 3).ring.rotations.exists(_.iterator.sameElements(Seq(2, 3, 0, 1))) // true
```

## Plain names

Because `RingView` is its own type, there is no `Seq` method to clash with:
the circular operations carry their plain names, with no `O` suffix.

```scala
val v = Vector(0, 1, 2).ring
v(3)             // 0 — indexing wraps in both directions
v.lift(3)        // Some(0)
v.slice(-1, 4)   // Iterator(2, 0, 1, 2, 0)
v.sliding(2)     // Iterator(Seq(0, 1), Seq(1, 2), Seq(2, 0))
```

All the [comparison](categories/comparisons.html), [necklace](categories/necklaces.html)
and [symmetry](categories/symmetry.html) operations are available too, under the same plain names:
`isRotationOf`, `alignTo`, `canonical`, `bracelet`, `rotationalSymmetry`, `reflectionalSymmetryAxes`, …

## Materialize at the boundary

A view stays a view until you ask for a collection:

```scala
val v = Vector(0, 1, 2, 3).ring.rotateRight(1)

v.toVector  // Vector(3, 0, 1, 2)
v.toSeq     // Seq(3, 0, 1, 2)
v.to(List)  // List(3, 0, 1, 2)
v.iterator  // Iterator(3, 0, 1, 2)
```

@@@ note

A view over a **mutable** sequence reflects later mutations of it, exactly like the standard library views.
A non-indexed source (e.g. `List`) is copied once to a `Vector` when the view is created.

@@@

## Views or decorators?

Both layers expose the same operations:

- use the **decorator methods** (`rotateRight`, `sliceO`, …, directly on your `Seq`) for one-shot
  operations where you want the result in the same collection type you started with;
- use **`.ring`** for chained transformations, for enumerating rotations/reflections cheaply,
  or when you want lazy, short-circuiting pipelines.
