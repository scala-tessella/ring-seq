package io.github.scala_tessella.ring_seq

import scala.collection.{IndexedSeq, Seq, SeqOps}

/** Provides comparison operations for a `Seq` considered circular. */
trait ComparingOps extends IteratingOps:

  extension [A, CC[B] <: SeqOps[B, CC, CC[B]]](ring: CC[A])
    private def isSameSize(that: CC[A]): Boolean =
      ring.sizeCompare(that.size) == 0

    private def containsAsRotation(seq: CC[A], that: CC[A]): Boolean =
      // Any rotation of `seq` is a contiguous slice of `seq ++ seq.init`,
      // so a single substring search beats enumerating all rotations.
      // `that.toSeq` is a no-op for immutable Seq inputs.
      seq.isEmpty || (seq ++ seq.init).containsSlice(that.toSeq)

    /** Tests whether this circular sequence is a rotation of a given sequence.
      *
      * @param that
      *   the sequence to test
      * @return
      *   true if this circular sequence is a rotation of ''that'', otherwise false.
      * @example
      *   {{{Seq(0, 1, 2).isRotationOf(Seq(1, 2, 0)) // true}}}
      */
    def isRotationOf(that: CC[A]): Boolean =
      isSameSize(that) && containsAsRotation(ring, that)

    /** Tests whether this circular sequence is a reflection of a given sequence.
      *
      * @param that
      *   the sequence to test
      * @return
      *   true if this circular sequence is a reflection of ''that'', otherwise false.
      * @example
      *   {{{Seq(0, 1, 2).isReflectionOf(Seq(0, 2, 1)) // true}}}
      */
    def isReflectionOf(that: CC[A]): Boolean =
      isSameSize(that) && (ring == that || ring.reflectAt() == that)

    /** Tests whether this circular sequence is a reversion of a given sequence.
      *
      * @param that
      *   the sequence to test
      * @return
      *   true if this circular sequence is a reversion of ''that'', otherwise false.
      * @example
      *   {{{Seq(0, 1, 2).isReversionOf(Seq(2, 1, 0)) // true}}}
      */
    def isReversionOf(that: CC[A]): Boolean =
      isSameSize(that) && (ring == that || ring.reverse == that)

    /** Tests whether this circular sequence is a rotation or a reflection of a given sequence.
      *
      * @param that
      *   the sequence to test
      * @return
      *   true if this circular sequence is a rotation or a reflection of ''that'', otherwise false.
      * @example
      *   {{{Seq(0, 1, 2).isRotationOrReflectionOf(Seq(2, 0, 1)) // true}}}
      */
    def isRotationOrReflectionOf(that: CC[A]): Boolean =
      isSameSize(that) && (containsAsRotation(ring, that) || containsAsRotation(ring.reflectAt(), that))

    /** Finds the rotation offset that aligns this circular sequence with a given sequence.
      *
      * @param that
      *   the sequence to align to
      * @return
      *   `Some(k)` such that `ring.startAt(k) == that`, or `None` if no rotation matches (or sizes differ).
      * @example
      *   {{{Seq(0, 1, 2).alignTo(Seq(2, 0, 1)) // Some(2)}}}
      */
    def alignTo(that: CC[A]): Option[Index] =
      if !isSameSize(that) then None
      else if ring.isEmpty then Some(0)
      else
        val idx = (ring ++ ring.init).indexOfSlice(that.toSeq)
        Option.when(idx >= 0)(idx)

    /** The number of positions at which corresponding elements differ (Hamming distance).
      *
      * @param that
      *   the sequence to compare against, must have the same size
      * @example
      *   {{{Seq(1, 0, 1, 1).hammingDistance(Seq(1, 1, 0, 1)) // 2}}}
      */
    def hammingDistance(that: CC[A]): Int =
      require(isSameSize(that), "sequences must have the same size")
      hammingOf(ring, that)

    /** The minimum Hamming distance over all rotations of this circular sequence.
      *
      * @param that
      *   the sequence to compare against, must have the same size
      * @return
      *   `0` iff `that` is a rotation of `this`, otherwise the smallest number of positional mismatches over
      *   any rotation. Useful for "how close are these two rings, up to rotation?".
      * @example
      *   {{{Seq(1, 0, 1, 1).minRotationalHammingDistance(Seq(1, 1, 0, 1)) // 0}}}
      */
    def minRotationalHammingDistance(that: CC[A]): Int =
      require(isSameSize(that), "sequences must have the same size")
      val n = ring.size
      if n == 0 then 0
      else
        // Materialize once; compare by index without allocating `n` rotations.
        val a: IndexedSeq[A] = ring match
          case is: IndexedSeq[A] => is
          case _                 => ring.toVector
        val b: IndexedSeq[A] = that match
          case is: IndexedSeq[A] => is
          case _                 => that.toVector
        var best             = Int.MaxValue
        var k                = 0
        while k < n && best != 0 do
          var count = 0
          var i     = 0
          var ai    = k
          while i < n && count < best do
            if a(ai) != b(i) then count += 1
            ai += 1
            if ai == n then ai = 0
            i += 1
          if count < best then best = count
          k += 1
        best

  private def hammingOf[A, CC[B] <: SeqOps[B, CC, CC[B]]](a: CC[A], b: CC[A]): Int =
    var count = 0
    val ai    = a.iterator
    val bi    = b.iterator
    while ai.hasNext do
      if ai.next() != bi.next() then count += 1
    count
