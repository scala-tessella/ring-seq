package io.github.scala_tessella.ring_seq

import scala.collection.{IndexedSeq, SeqOps}

/** Provides canonical-form operations for a `Seq` considered circular. */
object NecklaceOps {

  /** Universal trait providing canonical-form decorators for a `Seq` considered circular. */
  trait NecklaceDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]]
      extends Any
      with ComparingOps.ComparingDecorators[A, CC] {

    /** The starting index of the lexicographically smallest rotation (two-pointer minimal rotation, O(n)
      * time, O(1) extra space).
      *
      * @return
      *   the index in `[0, ring.size)` such that `ring.startAt(canonicalIndex)` is the lex-smallest of all
      *   rotations. Returns `0` for empty or single-element sequences.
      */
    def canonicalIndex(implicit ord: Ordering[A]): Index = {
      val n = underlying.size
      if (n <= 1) 0
      else {
        val indexed: IndexedSeq[A] = underlying match {
          case is: IndexedSeq[A] => is
          case _                 => underlying.toVector
        }
        NecklaceOps.leastRotation(indexed)
      }
    }

    /** The lexicographically smallest rotation of this circular sequence (necklace canonical form).
      *
      * Two circular sequences are rotations of each other iff their canonical forms are equal, making this
      * useful for hashing / deduplicating equivalent rings.
      *
      * @example
      *   {{{Seq(2, 0, 1).canonical // Seq(0, 1, 2)}}}
      */
    def canonical(implicit ord: Ordering[A]): CC[A] =
      startAt(canonicalIndex)

    /** The lexicographically smallest representative under both rotation and reflection (bracelet canonical
      * form).
      *
      * Two circular sequences belong to the same bracelet equivalence class iff their bracelet forms are
      * equal — useful for problems where mirror images are considered identical.
      *
      * @return
      *   the smaller of `canonical` and `reflectAt().canonical`, by lexicographic ordering.
      */
    def bracelet(implicit ord: Ordering[A]): CC[A] = {
      import Ordering.Implicits.seqOrdering
      val a = canonical
      val b = NecklaceOps.canonicalOf(reflectAt())
      Seq(a, b).minBy(_.toSeq)
    }
  }

  /** Two-pointer minimal-rotation algorithm: starting index of the lexicographically smallest rotation, in
    * O(n) time and O(1) extra space.
    *
    * Maintains two candidate offsets `i` and `j` with a common matched prefix of length `k`; each mismatch
    * eliminates `k + 1` candidates at once.
    */
  private[ring_seq] def leastRotation[A](s: IndexedSeq[A])(implicit ord: Ordering[A]): Int = {
    val n = s.length
    var i = 0
    var j = 1
    var k = 0
    while (i < n && j < n && k < n) {
      val a = s((i + k) % n)
      val b = s((j + k) % n)
      if (ord.equiv(a, b)) k += 1
      else {
        if (ord.gt(a, b)) i += k + 1
        else j += k + 1
        if (i == j) j += 1
        k = 0
      }
    }
    math.min(i, j)
  }

  private[ring_seq] def canonicalOf[A, CC[B] <: SeqOps[B, CC, CC[B]]](
      seq: CC[A]
  )(implicit ord: Ordering[A]): CC[A] = {
    val n = seq.size
    if (n <= 1) seq
    else {
      val indexed: IndexedSeq[A] = seq match {
        case is: IndexedSeq[A] => is
        case _                 => seq.toVector
      }
      val k                      = leastRotation(indexed)
      if (k == 0) seq else seq.drop(k) ++ seq.take(k)
    }
  }

  implicit private class NecklaceEnrichment[A, CC[B] <: SeqOps[B, CC, CC[B]]](val underlying: CC[A])
      extends AnyVal
      with NecklaceDecorators[A, CC]

}
