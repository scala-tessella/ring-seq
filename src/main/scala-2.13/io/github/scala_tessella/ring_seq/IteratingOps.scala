package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Provides operations returning iterators of sequences for a `Seq` considered circular. */
object IteratingOps {

  /** Universal trait providing decorators returning iterators of sequences for a `Seq` considered circular.
    */
  trait IteratingDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]]
      extends Any
      with SlicingOps.SlicingDecorators[A, CC] {

    /** Groups elements in fixed size blocks by passing a "sliding window" over them
      *
      * @param size
      *   the number of elements per group
      * @param step
      *   the distance between the first elements of successive groups
      * @return
      *   An iterator producing sequences of size ''size''.
      * @example
      *   {{{Seq(0, 1, 2).slidingO(2) // Iterator(Seq(0, 1), Seq(1, 2), Seq(2, 0))}}}
      */
    def slidingO(size: Int, step: Int = 1): Iterator[CC[A]] =
      sliceO(0, step * (ring.size - 1) + size).sliding(size, step)

    /** Partitions this circular sequence into non-overlapping fixed-size blocks.
      *
      * Unlike standard `grouped`, the last block wraps across the seam between the last and first elements,
      * so '''every''' block has exactly `size` elements. Produces `ceil(n / size)` blocks for a ring of size
      * `n`, and no blocks for an empty ring.
      *
      * @param size
      *   the number of elements per block; must be positive
      * @return
      *   an iterator producing sequences of size ''size''; empty when the ring is empty.
      * @example
      *   {{{Seq(0, 1, 2, 3, 4).groupedO(2) // Iterator(Seq(0, 1), Seq(2, 3), Seq(4, 0))}}}
      */
    def groupedO(size: Int): Iterator[CC[A]] =
      if (ring.isEmpty) Iterator.empty
      else {
        val n     = ring.size
        val count = (n + size - 1) / size
        sliceO(0, count * size).grouped(size)
      }

    /** Iterates over the elements paired with their original (circular) index, starting at some circular
      * index.
      *
      * @param from
      *   [[IndexO]]
      * @return
      *   an iterator of `(element, index)` pairs of length `ring.size`. Indices are in `[0, ring.size)`.
      * @example
      *   {{{Seq('a', 'b', 'c').zipWithIndexO(1).toList // List(('b', 1), ('c', 2), ('a', 0))}}}
      */
    def zipWithIndexO(from: IndexO = 0): Iterator[(A, Index)] = {
      val n = ring.size
      if (n == 0) Iterator.empty
      else {
        val start = indexFrom(from)
        startAt(from).iterator.zipWithIndex.map { case (a, i) =>
          (a, (start + i) % n)
        }
      }
    }

    private def transformations(f: CC[A] => Iterator[CC[A]]): Iterator[CC[A]] =
      if (ring.isEmpty) Iterator(ring) else f(ring)

    /** Computes all the rotations of this circular sequence
      *
      * @return
      *   An iterator producing all the sequences obtained by rotating this circular sequence, starting from
      *   itself and moving one rotation step to the right, or just itself if empty.
      * @example
      *   {{{Seq(0, 1, 2).rotations // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1))}}}
      */
    def rotations: Iterator[CC[A]] =
      transformations(r => slidingO(r.size))

    /** Computes all the reflections of this circular sequence
      *
      * @return
      *   An iterator producing the 2 sequences obtained by reflecting this circular sequence, starting from
      *   itself, or just itself if empty.
      * @example
      *   {{{Seq(0, 1, 2).reflections // Iterator(Seq(0, 1, 2), Seq(0, 2, 1))}}}
      */
    def reflections: Iterator[CC[A]] =
      transformations(r => List(r, r.reflectAt()).iterator)

    /** Computes all the reversions of this circular sequence
      *
      * @return
      *   An iterator producing the 2 sequences obtained by reversing this circular sequence, starting from
      *   itself, or just itself if empty.
      * @example
      *   {{{Seq(0, 1, 2).reversions // Iterator(Seq(0, 1, 2), Seq(2, 1, 0))}}}
      */
    def reversions: Iterator[CC[A]] =
      transformations(r => List(r, r.reverse).iterator)

    /** Computes all the rotations and reflections of this circular sequence
      *
      * @return
      *   An iterator producing all the sequences obtained by rotating and reflecting this circular sequence,
      *   starting from itself and moving one rotation step to the right, then reflecting and doing the same,
      *   or just itself if empty.
      * @example
      *   {{{Seq(0, 1, 2).rotationsAndReflections // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(0, 2, 1), Seq(2, 1, 0), Seq(1, 0, 2))}}}
      */
    def rotationsAndReflections: Iterator[CC[A]] =
      transformations(_.reflections.flatMap(_.rotations))

  }

  implicit private class IteratingEnrichment[A, CC[B] <: SeqOps[B, CC, CC[B]]](val ring: CC[A])
      extends AnyVal
      with IteratingDecorators[A, CC]

}
