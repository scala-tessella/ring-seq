package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Provides comparing operations for a `Seq` considered circular */
object ComparingOps {

  /** Universal trait providing comparing decorators for a `Seq` considered circular. */
  trait ComparingDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]]
    extends Any
      with IteratingOps.IteratingDecorators[A, CC] {

    private def isTransformationOf(that: CC[A], f: CC[A] => Iterator[CC[A]]): Boolean = {
      ring.sizeCompare(that.size) == 0 && f(ring).contains(that)
    }

    /** Tests whether this circular sequence is a rotation of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a rotation of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isRotationOf(Seq(1, 2, 0)) // true}}}
     */
    def isRotationOf(that: CC[A]): Boolean =
      isTransformationOf(that, _.rotations)

    /** Tests whether this circular sequence is a reflection of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a reflection of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isReflectionOf(Seq(0, 2, 1)) // true}}}
     */
    def isReflectionOf(that: CC[A]): Boolean =
      isTransformationOf(that, _.reflections)

    /** Tests whether this circular sequence is a reversion of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a reversion of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isReversionOf(Seq(2, 1, 0)) // true}}}
     */
    def isReversionOf(that: CC[A]): Boolean =
      isTransformationOf(that, _.reversions)

    /** Tests whether this circular sequence is a rotation or a reflection of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a rotation or a reflection of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isRotationOrReflectionOf(Seq(2, 0, 1)) // true}}}
     */
    def isRotationOrReflectionOf(that: CC[A]): Boolean =
      isTransformationOf(that, _.rotationsAndReflections)

  }

  private implicit class ComparingEnrichment[A, CC[B] <: SeqOps[B, CC, CC[B]]](val ring: CC[A])
    extends AnyVal
      with ComparingDecorators[A, CC]

}