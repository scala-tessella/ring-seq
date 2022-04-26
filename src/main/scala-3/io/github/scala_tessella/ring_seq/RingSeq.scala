package io.github.scala_tessella.ring_seq

/** Adds implicit methods to [`scala.collection.Seq`](https://scala-lang.org/api/3.x/scala/collection/Seq.html)
 * (immutable / mutable and subtypes) for when a sequence needs to be considered **circular**, its elements forming a ring.
 *
 * @author Mario Càllisto
 */
object RingSeq extends ComparingOps with SymmetryOps
