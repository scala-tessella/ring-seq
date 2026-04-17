package io.github.scala_tessella.ring_seq

import scala.collection.{Seq, SeqOps}

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
