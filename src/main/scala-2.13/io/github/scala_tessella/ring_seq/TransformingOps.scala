package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Provides rotation and reflection operations for a `Seq` considered circular. */
object TransformingOps {

  /** Universal trait providing rotation and reflection decorators for a `Seq` considered circular. */
  trait TransformingDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]]
      extends Any
      with IndexingOps.IndexingDecorators[A, CC] {

    /** Rotate the sequence to the right by some steps.
      *
      * @param step
      *   the circular distance between each new and old position
      * @return
      *   a sequence consisting of all elements rotated to the right by ''step'' places. If ''step'' is
      *   negative the rotation happens to the left.
      * @example
      *   {{{Seq(0, 1, 2).rotateRight(1) // Seq(2, 0, 1)}}}
      */
    def rotateRight(step: Int): CC[A] =
      if (ring.isEmpty) ring
      else {
        val j: Index = ring.size - indexFrom(step)
        ring.drop(j) ++ ring.take(j)
      }

    /** Rotates the sequence to the left by some steps.
      *
      * @param step
      *   the circular distance between each old and new position
      * @return
      *   a sequence consisting of all elements rotated to the left by ''step'' places. If ''step'' is
      *   negative the rotation happens to the right.
      * @example
      *   {{{Seq(0, 1, 2).rotateLeft(1) // Seq(1, 2, 0)}}}
      */
    def rotateLeft(step: Int): CC[A] =
      rotateRight(-step)

    /** Rotates the sequence to start at some circular index.
      *
      * @param i
      *   [[IndexO]]
      * @return
      *   a sequence consisting of all elements rotated to start at circular index ''i''. It is equivalent to
      *   [[rotateLeft]].
      * @example
      *   {{{Seq(0, 1, 2).startAt(1) // Seq(1, 2, 0)}}}
      */
    def startAt(i: IndexO): CC[A] =
      rotateLeft(i)

    /** Reflects the sequence to start at some circular index.
      *
      * @param i
      *   [[IndexO]]
      * @return
      *   a sequence consisting of all elements reversed and rotated to start at circular index ''i''.
      * @example
      *   {{{Seq(0, 1, 2).reflectAt() // Seq(0, 2, 1)}}}
      */
    def reflectAt(i: IndexO = 0): CC[A] =
      startAt(i + 1).reverse

  }

}
