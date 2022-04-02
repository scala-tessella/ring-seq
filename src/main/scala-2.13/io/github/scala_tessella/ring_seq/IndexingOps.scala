package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

object IndexingOps {

  /** Universal trait providing decorators for a `Seq` considered circular. */
  trait IndexingDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]] extends Any {

    /** For improved readability, the index of a `Seq`. */
    type Index = Int

    /** For improved readability, the index of a circular `Seq`.
     *
     * @note any value is a valid index, provided that `Seq` is not empty
     */
    type IndexO = Int

    /** The circular sequence */
    def ring: CC[A]

    def indexFrom(i: IndexO): Index =
      java.lang.Math.floorMod(i, ring.size)

    /** Gets the element at some circular index.
     *
     * @param i [[IndexO]]
     * @throws java.lang.ArithmeticException if `Seq` is empty
     * @example {{{Seq(0, 1, 2).applyO(3) // 0}}}
     */
    def applyO(i: IndexO): A =
      ring(indexFrom(i))

  }

}