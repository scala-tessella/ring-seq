package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Provides symmetry operations for a `Seq` considered circular. */
trait SymmetryOps extends TransformingOps:

  extension [A, CC[B] <: SeqOps[B, CC, CC[B]]](ring: CC[A])

    /** Computes the order of rotational symmetry possessed by this circular sequence, that is the number of
      * times the sequence matches itself as it makes a full rotation.
      *
      * @return
      *   An integer between 1 and the size of the sequence. 1 means no symmetry (only identity), max means a
      *   perfect circle (e.g., all elements equal).
      * @example
      *   {{{Seq(0, 1, 2, 0, 1, 2).rotationalSymmetry // 2}}}
      */
    def rotationalSymmetry: Int =
      val n = ring.size
      if n < 2 then 1
      else
        // Find the smallest shift that makes the list equal to itself
        val smallestPeriod =
          (1 to n).find: shift =>
            // Optimization: We only need to check shifts that divide n
            n % shift == 0 && ring.rotateRight(shift) == ring

        n / smallestPeriod.getOrElse(n)

    private def greaterHalfRange: Range =
      0 until Math.ceil(ring.size / 2.0).toInt

    private def checkReflectionAxis(gap: Int): Boolean =
      greaterHalfRange.forall(j => ring.applyO(j + 1) == ring.applyO(-(j + gap)))

    private def hasHeadOnAxis: Boolean =
      checkReflectionAxis(1)

    private def hasAxisBetweenHeadAndNext: Boolean =
      checkReflectionAxis(0)

    private def findReflectionSymmetry: Option[Index] =
      greaterHalfRange.find(j =>
        val rotation = ring.startAt(j)
        rotation.hasHeadOnAxis || rotation.hasAxisBetweenHeadAndNext
      )

    /** Finds the indices of each element of this circular sequence close to an axis of reflectional symmetry.
      *
      * @return
      *   the indices of each element of this circular sequence close to an axis of reflectional symmetry,
      *   that is a line of symmetry that splits the sequence in two identical halves.
      * @example
      *   {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetryIndices // List(1, 4, 7, 10)}}}
      */
    def symmetryIndices: List[Index] =
      if ring.isEmpty then Nil
      else
        val folds    = rotationalSymmetry
        val foldSize = ring.size / folds
        ring.take(foldSize).findReflectionSymmetry match
          case None    => Nil
          case Some(j) => (0 until folds).toList.map(_ * foldSize + j)

    /** Computes the order of reflectional (mirror) symmetry possessed by this circular sequence.
      *
      * @return
      *   the number >= 0 of reflections in which this circular sequence looks exactly the same.
      * @example
      *   {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetry // 4}}}
      */
    def symmetry: Int =
      symmetryIndices.size
