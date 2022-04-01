package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Adds implicit methods to `[[https://www.scala-lang.org/api/current/scala/collection/Seq.html Seq]]`
 * (immutable / mutable and subtypes) for when a sequence needs to be considered '''circular''', its elements forming a ring.
 *
 * @author Mario Càllisto
 */
object RingSeq extends ComparisonOps with SymmetryOps
