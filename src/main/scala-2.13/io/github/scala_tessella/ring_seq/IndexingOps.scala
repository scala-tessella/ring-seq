package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Provides indexing operations for a `Seq` considered circular */
object IndexingOps {

  /** For improved readability, the index of a `Seq`. */
  type Index = Int

  /** For improved readability, the index of a circular `Seq`.
    *
    * @note
    *   any value is a valid index, provided that `Seq` is not empty
    */
  type IndexO = Int

  /** Universal trait providing indexing decorators for a `Seq` considered circular. */
  trait IndexingDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]]
      extends Any {

    type Index = IndexingOps.Index

    type IndexO = IndexingOps.IndexO

    /** The circular sequence */
    def underlying: CC[A]

    def indexFrom(i: IndexO): Index =
      java.lang.Math.floorMod(i, underlying.size)

    /** Gets the element at some circular index.
      *
      * @param i
      *   [[IndexO]]
      * @throws java.lang.ArithmeticException
      *   if `Seq` is empty
      * @example
      *   {{{Seq(0, 1, 2).applyO(3) // 0}}}
      */
    def applyO(i: IndexO): A =
      underlying(indexFrom(i))

    /** Optionally gets the element at some circular index (the circular version of `lift`).
      *
      * Since every circular index is valid on a non-empty sequence, this returns `None` only when the
      * sequence is empty.
      *
      * @param i
      *   [[IndexO]]
      * @example
      *   {{{
      *   Seq(0, 1, 2).liftO(3)   // Some(0)
      *   Seq.empty[Int].liftO(0) // None
      *   }}}
      */
    def liftO(i: IndexO): Option[A] =
      if (underlying.isEmpty) None else Some(applyO(i))

    /** Finds the circular index of the first element equal to a given value, searching circularly from a
      * given circular index (the circular version of `indexOf`).
      *
      * The whole ring is searched, wrapping past the end back to the start.
      *
      * @param elem
      *   the element value to search for
      * @param from
      *   [[IndexO]]
      * @return
      *   the [[Index]] in `[0, size)` of the first element found at or after circular index ''from'', or -1
      *   if the element is not in the sequence.
      * @example
      *   {{{Seq(0, 1, 2).indexOfO(0, 1) // 0}}}
      */
    def indexOfO(elem: A, from: IndexO = 0): Index =
      if (underlying.isEmpty) -1
      else {
        val start = indexFrom(from)
        val found = underlying.indexOf(elem, start)
        if (found >= 0) found
        else {
          val wrapped = underlying.indexOf(elem)
          if (wrapped >= 0 && wrapped < start) wrapped else -1
        }
      }

  }

}
