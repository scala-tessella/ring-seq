package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.IndexingOps.IndexingDecorators

import scala.collection.{Seq, SeqOps}

/** Adds implicit methods to `[[https://www.scala-lang.org/api/current/scala/collection/Seq.html Seq]]`
 * (immutable / mutable and subtypes) for when a sequence needs to be considered '''circular''', its elements forming a ring.
 *
 * @author Mario Càllisto
 */
object RingSeq {

  type Index = IndexingOps.Index

  type IndexO = IndexingOps.IndexO

  /** Universal trait providing decorators for a `Seq` considered circular. */
  trait RingDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]] extends Any
    with ComparingOps.ComparingDecorators[A, CC]
    with SymmetryOps.SymmetryDecorators[A, CC] {}

  /** Value class providing methods for a generic `Seq` considered circular. */
  implicit class RingSeqEnrichment[A, CC[B] <: SeqOps[B, CC, CC[B]]](val ring: CC[A]) extends AnyVal
    with RingDecorators[A, CC]

  /** Value class providing methods for a `String` considered circular. */
  implicit class RingStringEnrichment(private val s: String) extends AnyVal
    with RingDecorators[Char, Seq] {

    /** Converts this string into a circular `Seq`.
     *
     * @return the string as a sequence of `Char`.
     */
    def ring: Seq[Char] = s.toSeq

  }

  /** Value class providing methods for a `StringBuilder` considered circular. */
  implicit class RingStringBuilderEnrichment(private val sb: StringBuilder) extends AnyVal
    with RingDecorators[Char, Seq] {

    /** Converts this string builder into a circular `Seq`.
     *
     * @return the string builder as a sequence of `Char`.
     */
    def ring: Seq[Char] = sb.toSeq

  }

}
