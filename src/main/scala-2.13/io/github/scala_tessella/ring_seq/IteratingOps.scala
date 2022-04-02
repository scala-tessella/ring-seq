package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

/** Provides operations returning iterators of sequences for a `Seq` considered circular. */
object IteratingOps {

  /** Universal trait providing decorators returning iterators of sequences for a `Seq` considered circular. */
  trait IteratingDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]] extends Any with SlicingOps.SlicingDecorators[A, CC] {

    /** Groups elements in fixed size blocks by passing a "sliding window" over them
     *
     * @param size the number of elements per group
     * @param step the distance between the first elements of successive groups
     * @return An iterator producing sequences of size ''size''.
     * @example {{{Seq(0, 1, 2).slidingO(2) // Iterator(Seq(0, 1), Seq(1, 2), Seq(2, 0))}}}
     */
    def slidingO(size: Int, step: Int = 1): Iterator[CC[A]] =
      sliceO(0, step * (ring.size - 1) + size).sliding(size, step)

    private def transformations(f: CC[A] => Iterator[CC[A]]): Iterator[CC[A]] =
      if (ring.isEmpty) Iterator(ring) else f(ring)

    /** Computes all the rotations of this circular sequence
     *
     * @return An iterator producing all the sequences obtained by rotating this circular sequence,
     *         starting from itself and moving one rotation step to the right,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).rotations // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1))}}}
     */
    def rotations: Iterator[CC[A]] =
      transformations(r => slidingO(r.size))

    /** Computes all the reflections of this circular sequence
     *
     * @return An iterator producing the 2 sequences obtained by reflecting this circular sequence,
     *         starting from itself,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).reflections // Iterator(Seq(0, 1, 2), Seq(0, 2, 1))}}}
     */
    def reflections: Iterator[CC[A]] =
      transformations(r => List(r, r.reflectAt()).iterator)

    /** Computes all the reversions of this circular sequence
     *
     * @return An iterator producing the 2 sequences obtained by reversing this circular sequence,
     *         starting from itself,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).reversions // Iterator(Seq(0, 1, 2), Seq(2, 1, 0))}}}
     */
    def reversions: Iterator[CC[A]] =
      transformations(r => List(r, r.reverse).iterator)

    /** Computes all the rotations and reflections of this circular sequence
     *
     * @return An iterator producing all the sequences obtained by rotating and reflecting this circular sequence,
     *         starting from itself and moving one rotation step to the right, then reflecting and doing the same,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).rotationsAndReflections // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(0, 2, 1), Seq(2, 1, 0), Seq(1, 0, 2))}}}
     */
    def rotationsAndReflections: Iterator[CC[A]] =
      transformations(_.reflections.flatMap(_.rotations))

  }

  private implicit class IteratingEnrichment[A, CC[B] <: SeqOps[B, CC, CC[B]]](val ring: CC[A]) extends AnyVal with IteratingDecorators[A, CC]

}