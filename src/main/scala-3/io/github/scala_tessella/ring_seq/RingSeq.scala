package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Adds implicit methods to
  * [`scala.collection.Seq`](https://scala-lang.org/api/3.x/scala/collection/Seq.html) (immutable / mutable
  * and subtypes) for when a sequence needs to be considered **circular**, its elements forming a ring.
  *
  * @author
  *   Mario Càllisto
  */
object RingSeq extends NecklaceOps with SymmetryOps:

  /** Re-exports the axis-location types so that `import RingSeq.*` brings both the extension methods and the
    * `AxisLocation`/`Vertex`/`Edge` types into scope in one go.
    */
  export SymmetryOps.{AxisLocation, Vertex, Edge}

  extension [A, CC[B] <: SeqOps[B, CC, CC[B]]](self: CC[A])

    /** This sequence as a circular [[RingView]]: rotations and reflections become O(1) views over the same
      * elements, and the circular operations carry their plain names (no `O` suffix).
      *
      * @example
      *   {{{Vector(0, 1, 2, 3).ring.rotateRight(1).toVector // Vector(3, 0, 1, 2)}}}
      */
    def ring: RingView[A] = RingView.from(self)
