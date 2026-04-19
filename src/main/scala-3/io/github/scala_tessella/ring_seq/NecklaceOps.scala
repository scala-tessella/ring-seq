package io.github.scala_tessella.ring_seq

import scala.collection.{IndexedSeq, SeqOps}

object NecklaceOps:

  /** Booth's O(n) algorithm: starting index of the lexicographically smallest rotation. */
  private[ring_seq] def leastRotationBooth[A](s: IndexedSeq[A])(using ord: Ordering[A]): Int =
    val n               = s.length
    val len             = 2 * n
    val f               = Array.fill(len)(-1)
    var k               = 0
    var j               = 1
    def at(idx: Int): A = s(idx % n)
    while j < len do
      val sj = at(j)
      var i  = f(j - k - 1)
      while i != -1 && !ord.equiv(sj, at(k + i + 1)) do
        if ord.lt(sj, at(k + i + 1)) then k = j - i - 1
        i = f(i)
      if i == -1 && !ord.equiv(sj, at(k + i + 1)) then
        if ord.lt(sj, at(k + i + 1)) then k = j
        f(j - k) = -1
      else
        f(j - k) = i + 1
      j += 1
    k

/** Provides canonical-form operations for a `Seq` considered circular. */
trait NecklaceOps extends ComparingOps:

  extension [A, CC[B] <: SeqOps[B, CC, CC[B]]](ring: CC[A])

    /** The starting index of the lexicographically smallest rotation (Booth's algorithm, O(n)).
      *
      * @return
      *   the index in `[0, ring.size)` such that `ring.startAt(canonicalIndex)` is the lex-smallest of all
      *   rotations. Returns `0` for empty or single-element sequences.
      */
    def canonicalIndex(using Ordering[A]): Index =
      val n = ring.size
      if n <= 1 then 0
      else
        val indexed: IndexedSeq[A] = ring match
          case is: IndexedSeq[A] => is
          case _                 => ring.toVector
        NecklaceOps.leastRotationBooth(indexed)

    /** The lexicographically smallest rotation of this circular sequence (necklace canonical form).
      *
      * Two circular sequences are rotations of each other iff their canonical forms are equal, making this
      * useful for hashing / deduplicating equivalent rings.
      *
      * @example
      *   {{{Seq(2, 0, 1).canonical // Seq(0, 1, 2)}}}
      */
    def canonical(using Ordering[A]): CC[A] =
      ring.startAt(ring.canonicalIndex)

    /** The lexicographically smallest representative under both rotation and reflection (bracelet canonical
      * form).
      *
      * Two circular sequences belong to the same bracelet equivalence class iff their bracelet forms are
      * equal — useful for problems where mirror images are considered identical.
      *
      * @return
      *   the smaller of `canonical` and `reflectAt().canonical`, by lexicographic ordering.
      */
    def bracelet(using ord: Ordering[A]): CC[A] =
      val a   = ring.canonical
      val b   = ring.reflectAt().canonical
      val ai  = a.iterator
      val bi  = b.iterator
      var cmp = 0
      while cmp == 0 && ai.hasNext do
        cmp = ord.compare(ai.next(), bi.next())
      if cmp <= 0 then a else b
