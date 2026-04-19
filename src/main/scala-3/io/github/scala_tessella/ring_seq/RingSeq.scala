package io.github.scala_tessella.ring_seq

/** Adds implicit methods to
  * [`scala.collection.Seq`](https://scala-lang.org/api/3.x/scala/collection/Seq.html) (immutable / mutable
  * and subtypes) for when a sequence needs to be considered **circular**, its elements forming a ring.
  *
  * @author
  *   Mario Càllisto
  */
object RingSeq extends NecklaceOps with SymmetryOps:

  /** Re-exports the axis-location types so that `import RingSeq.*` brings both the extension methods
    * and the `AxisLocation`/`Vertex`/`Edge` types into scope in one go.
    */
  export SymmetryOps.{AxisLocation, Vertex, Edge}
