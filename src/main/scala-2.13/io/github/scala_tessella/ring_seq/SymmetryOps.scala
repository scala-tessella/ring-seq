package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Provides symmetry operations for a `Seq` considered circular */
object SymmetryOps {

  private def greaterHalfRange(size: Int): Range =
    0 until Math.ceil(size / 2.0).toInt

  private def checkReflectionAxis[A, CC[B] <: SeqOps[B, CC, CC[B]]](seq: CC[A], gap: Int): Boolean =
    greaterHalfRange(seq.size).forall(j => seq.applyO(j + 1) == seq.applyO(-(j + gap)))

  private def hasHeadOnAxis[A, CC[B] <: SeqOps[B, CC, CC[B]]](seq: CC[A]): Boolean =
    checkReflectionAxis(seq, 1)

  private def hasAxisBetweenHeadAndNext[A, CC[B] <: SeqOps[B, CC, CC[B]]](seq: CC[A]): Boolean =
    checkReflectionAxis(seq, 0)

  /** Universal trait providing symmetry decorators for a `Seq` considered circular. */
  trait SymmetryDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]]
      extends Any
      with TransformingOps.TransformingDecorators[A, CC] {

    /** Computes the order of rotational symmetry possessed by this circular sequence, that is the number of
      * times the sequence matches itself as it makes a full rotation.
      *
      * @return
      *   An integer between 1 and the size of the sequence. 1 means no symmetry (only identity), max means a
      *   perfect circle (e.g., all elements equal).
      * @example
      *   {{{Seq(0, 1, 2, 0, 1, 2).rotationalSymmetry // 2}}}
      */
    def rotationalSymmetry: Int = {
      val n = ring.size
      if (n < 2)
        1
      else {
        // Find the smallest shift that makes the list equal to itself
        val smallestPeriod =
          (1 to n).find { shift =>
            // Optimization: We only need to check shifts that divide n
            n % shift == 0 && ring.rotateRight(shift) == ring
          }

        n / smallestPeriod.getOrElse(n)
      }
    }

    private def findReflectionSymmetry(seq: CC[A]): Option[Index] =
      greaterHalfRange(seq.size).find { j =>
        val rotation = seq.startAt(j)
        hasHeadOnAxis(rotation) || hasAxisBetweenHeadAndNext(rotation)
      }

    /** Finds the indices of each element of this circular sequence close to an axis of reflectional symmetry.
      *
      * @return
      *   the indices of each element of this circular sequence close to an axis of reflectional symmetry,
      *   that is a line of symmetry that splits the sequence in two identical halves.
      * @example
      *   {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetryIndices // List(1, 4, 7, 10)}}}
      */
    def symmetryIndices: List[Index] =
      if (ring.isEmpty) Nil
      else {
        val folds    = rotationalSymmetry
        val foldSize = ring.size / folds
        findReflectionSymmetry(ring.take(foldSize)) match {
          case None    => Nil
          case Some(j) => (0 until folds).toList.map(_ * foldSize + j)
        }
      }

    /** Computes the order of reflectional (mirror) symmetry possessed by this circular sequence.
      *
      * @return
      *   the number >= 0 of reflections in which this circular sequence looks exactly the same.
      * @example
      *   {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetry // 4}}}
      */
    def symmetry: Int =
      symmetryIndices.size

  }

  implicit private class SymmetryEnrichment[A, CC[B] <: SeqOps[B, CC, CC[B]]](val ring: CC[A])
      extends AnyVal
      with SymmetryDecorators[A, CC]

}
